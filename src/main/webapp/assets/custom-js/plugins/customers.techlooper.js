;(function(){
    var win = $(window);
    var KzSlider = function (element, options) {
        this.$element = $(element);
        this.options = options;
        this.init();
    };

    KzSlider.prototype = {
        constructor: KzSlider,
        init: function(){
            var _this = this,
                opts = this.options,
                $el = this.$element;

            _this.$bulletCtrls = $('<div class="bullets-control-kz-slide"></div>').appendTo($el);
            _this.$navCtrl = $('<ul class="bullets"></ul>').appendTo(_this.$bulletCtrls);

            _this.$btnCtrls = $('<div class="btn-control-kz-slide"></div>').appendTo(_this.$bulletCtrls);
            _this.$prev = $('<a href="javascript:void(0);" class="btn-prev"><i class="fa fa-arrow-circle-left"></i></a>').appendTo(_this.$btnCtrls);
            _this.$next = $('<a href="javascript:void(0);" class="btn-next"><i class="fa fa-arrow-circle-right"></i></a>').appendTo(_this.$btnCtrls);

            _this.$trans = $el.find(opts.transSelector);
            _this.$preview = _this.$trans.parent();

            _this.curItemIdx = opts.activeIndex;
            _this.refresh();

            _this.$prev.on("click", function(e){
                e.preventDefault();
                e.stopPropagation();

                _this.prev.call(_this, true);
            });

            _this.$next.on("click", function(e){
                e.preventDefault();
                e.stopPropagation();

                _this.next.call(_this, true);
            });

            if ('ontouchstart' in window){
                var el = _this.$preview[0];
                var moveX, moveY;
                var drag = false;
                el.addEventListener('touchstart',function(e){
                    moveX = e.touches[0].pageX;
                    moveY = e.touches[0].pageY;
                    drag = false;
                }, true);
                el.addEventListener('touchmove',function(e){
                    var deltaX = moveX - e.changedTouches[0].pageX;
                    if (Math.abs(deltaX) > 0){
                        if (Math.abs(deltaX) > 10){
                            e.preventDefault();
                        }
                    }
                    drag = true;
                }, false);
                el.addEventListener('touchend',function(e){
                    if(drag){
                        moveX = moveX - e.changedTouches[0].pageX;
                        moveY = moveY - e.changedTouches[0].pageY;
                        if(Math.abs(moveX)>80){
                            if(moveX<0) _this.prev.call(_this, true);
                            else _this.next.call(_this, true);
                        }
                    }
                    drag = false;
                }, true);
            }
        },
        updateItem: function(){
            var _this = this,
                opts = this.options,
                $el = this.$element;

            var fItem, $item, i, l,
                elmW = $el.width();

            _this.items = _this.$trans.children();

            fItem = _this.items.eq(1);
            _this.itemHeight = fItem.outerHeight(true);
            _this.slideWidth = fItem.outerWidth(true);

            _this.noShowItem = Math.floor(elmW/_this.slideWidth);

            if (_this.noShowItem <= opts.minItem){
                _this.noShowItem = opts.minItem;
            }
            else if (_this.noShowItem >= opts.maxItem){
                _this.noShowItem = opts.maxItem;
            }

            var previewHeight = _this.$preview.height(),
                rows = Math.ceil(previewHeight/_this.itemHeight),
                l = _this.items.length,
                r = l % rows;

            _this.itemsOnRow = Math.floor(l/rows);

            if (r > 0){
                _this.itemsOnRow += 1;
            }

            i = 0;
            for (; i <= l; i++) {
                $item = _this.items.eq(i);
                $item.off('.hover').on({
                    'mouseover.hover': function(){
                        _this.pause.call(_this);
                        $(this).addClass('active');
                    },
                    'mouseout.hover': function(){
                        $(this).removeClass('active');
                        opts.auto && _this.play.call(_this);
                    }
                });
            };
        },
        updatePreview: function(){
            var _this = this,
                opts = this.options;

            var preview = _this.$preview[0],
                items = _this.items;

            var i = _this.noShowItem,
                w = 0;

            while (--i >= 0){
                w += items.eq(i).outerWidth(true);
            }
            preview.style.width = w + 'px';
        },
        updateTrans: function(){
            var _this = this,
                opts = this.options;

            var items = _this.items;

            var i = _this.itemsOnRow,
                w = 0;

            while (--i >= 0){
                w += items.eq(i).outerWidth(true);
            }
            _this.$trans.width(w);
            _this.activeIndex = Math.max(0, Math.floor((_this.curItemIdx/_this.itemsOnRow) - (_this.noShowItem - 1)));

            _this.$trans.css('marginLeft', -(_this.activeIndex * _this.slideWidth));
        },
        updateControls: function(){
            var _this = this,
                opts = this.options,
                $el = this.$element;

            var i = _this.noShowItem,
                l = _this.itemsOnRow,
                w = 0,
                elmW = $el.width();

            var strHTML = "";
            for (; i <= l; i++) {
                strHTML += '<li><a class="'+(i==(_this.activeIndex + _this.noShowItem)?"active":"")+'" href="#"></a></li>';
            };

            _this.$navCtrl.html(strHTML);

            _this.$navCtrl.children().each(function(){
                w += $(this).outerWidth(true);
            });

            if (elmW < w){
                w = elmW/2;
            }
            _this.$navCtrl.width(w);

            _this.ctrls = _this.$navCtrl.find('a');
            _this.ctrls.each(function(idx){
                var $ctrl = $(this);

                $ctrl.off('click')
                    .on('click', ctrlClick(idx));
            });

            function ctrlClick(index){
                return function(e){
                    e.preventDefault();

                    _this.paused = true;
                    _this.slide.call(_this, index);
                }
            };
        },
        update: function(){
            var _this = this,
                opts = this.options;

            _this.updateItem.call(_this);
            _this.updatePreview.call(_this);
            _this.updateTrans.call(_this);
            _this.updateControls.call(_this);
        },
        slide: function(index){
            var _this = this,
                opts = _this.options;

            _this.$trans.stop().animate({'marginLeft': -(index * _this.slideWidth)}, opts.duration, opts.easing, function(){
                if (opts.auto && _this.paused){
                    _this.play.call(_this);
                }
            });

            _this.ctrls.removeClass('active');
            _this.ctrls.eq(index).addClass('active');

            _this.activeIndex = index;
            _this.curItemIdx = index * _this.noShowItem;
        },
        next: function(noloop){
            var _this = this;

            var len = _this.itemsOnRow - _this.noShowItem,
                index = _this.activeIndex + 1;

            if (index > len){
                index = noloop ? len : 0;
            }
            _this.paused = true;
            _this.slide.call(_this, index);
        },
        prev: function(noloop){
            var _this = this;

            var len = _this.itemsOnRow - _this.noShowItem,
                index = _this.activeIndex - 1;

            if (index < 0){
                index = noloop ? 0 : len;
            }
            _this.paused = true;
            _this.slide.call(_this, index);
        },
        play: function(){
            var _this = this,
                opts = this.options;

            _this.paused = false;
            this.timeInterval && clearInterval(this.timeInterval);
            this.timeInterval = setInterval(function(){
                if (_this.paused) return;
                var index = _this.activeIndex + 1;
                if (index >= _this.noShowRow){
                    index = 0;
                }
                _this.slide.call(_this, index);
            }, opts.timeLoop);
        },
        pause: function(){
            clearInterval(this.timeInterval);
        },
        refresh: function(){
            var _this = this,
                opts = this.options;

            opts.auto && _this.play.call(_this);

            _this.update.call(_this);
            win.on('resize.'+opts.namespace, $.proxy(_this.resize,_this));
            if ("onorientationchange" in window){
                win.on('orientationchange.'+opts.namespace, $.proxy(_this.resize,_this));
            }
        },
        resize: function(){
            var _this = this,
                opts = this.options;
            _this.pause.call(_this);
            _this.update.call(_this);
            if (opts.auto){
                this.afterTimeResize && clearTimeout(this.afterTimeResize);
                this.afterTimeResize = setTimeout(function(){
                    _this.play.call(_this);
                }, opts.afterTimeResize);
            }
        },
        stop:function(){
            this.pause.call(this);
            win.off('resize.'+this.options.namespace);
        },
        destroy: function(){
            this.stop();
            this.$element.removeData('kzSlider');
        }
    };

    $.fn.kzSlider = function (option) {
        return this.each(function () {
            var $this = $(this)
                , data = $this.data('kzSlider')
                , options = $.extend({}, $.fn.kzSlider.defaults, $this.data(), typeof option == 'object' && option);
            if (!data) $this.data('kzSlider', (data = new KzSlider(this, options)));
            if (typeof option == 'string') data[option]();
        });
    };

    $.fn.kzSlider.defaults = {
        namespace: 'kzSlider',
        transSelector: '.kz-slide-trans',
        activeIndex: 0,
        easing: 'easeOutQuint',
        duration: 700,
        timeLoop: 3500,
        auto: false,
        maxItem: 20,
        minItem: 1,
        afterTimeResize: 500
    };

    $.fn.kzSlider.Constructor = KzSlider;

})(window.jQuery);
