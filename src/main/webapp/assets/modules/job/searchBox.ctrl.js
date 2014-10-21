angular.module('Jobs').controller('searchBoxController',
    ["$scope", "jsonValue", "$location", "connectionFactory", function($scope, jsonValue, $location, connectionFactory) {
    connectionFactory.initialize($scope);
    var hWin = $(window).height();
    openSearchForm($(window).height());
    $(window).resize(function() {
        openSearchForm($(window).height());
    });

    function openSearchForm(h){
        $('.search-block').animate({
            'height': h,
            bottom: 0
        }, {
            duration: '10000',
            easing: 'easeOutQuad'
        });
    }
    $('.btn-close').click(function(){
        $('.search-block').animate({
            'height': 0,
            'bottom': '50%'
        }, {
            duration: '10000',
            easing: 'easeOutQuad'
        });
        $('body').css("background-color","#2e272a");
        
    });
    // load data for auto dropdown list and show technical skill
    var dataSkill = jsonValue.technicalSkill,
    	options = '', skills = '';
    $.each(dataSkill, function(index, skill){
    	options = options + '<option value="'+ index +'" data-left="<img src=images/'+ skill.logo +'>">'+ skill.name +'</option>';
        skills = skills +'<li><img src=images/'+ skill.logo +' title="'+ skill.name +'">';
    });
    $('.termsList').append(options);
    $('.technical-Skill-List ul').append(skills);
    

    (function($) {
        $.selectator = function(element, options) {
            var defaults = {
                prefix: 'selectator_',
                height: 'auto',
                useDimmer: false,
                useSearch: true,
                showAllOptionsOnFocus: false,
                selectFirstOptionOnSearch: true,
                keepOpen: false,
                searchCallback: function() {},
                labels: {
                    search: 'Enter Terms Title...'
                }
            };

            var plugin = this;
            plugin.settings = {};
            var $source_element = $(element);
            var $box_element = null;
            var $chosenitems_element = null;
            var $input_element = null;
            var $textlength_element = null;
            var $options_element = null;
            var multiple = $source_element.attr('multiple') !== undefined;
            var selected_index = multiple ? -1 : 0;
            var key = {
                backspace: 8,
                enter: 13,
                escape: 27,
                left: 37,
                up: 38,
                right: 39,
                down: 40
            };



            // INITIALIZE PLUGIN
            plugin.init = function() {
                plugin.settings = $.extend({}, defaults, options);

                //// ================== CREATE ELEMENTS ================== ////
                // dimmer
                if (plugin.settings.useDimmer) {
                    if ($('#' + plugin.settings.prefix + 'dimmer').length === 0) {
                        var $dimmer_element = $(document.createElement('div'));
                        $dimmer_element.attr('id', plugin.settings.prefix + 'dimmer');
                        $dimmer_element.hide();
                        $(document.body).prepend($dimmer_element);
                    }
                }
                // box element
                $box_element = $(document.createElement('div'));
                if ($source_element.attr('id') !== undefined) {
                    $box_element.attr('id', plugin.settings.prefix + $source_element.attr('id'));
                }
                $box_element.addClass('selectator ' + (multiple ? 'multiple ' : 'single ') + 'options-hidden');
                if (!plugin.settings.useSearch) {
                    $box_element.addClass('disable_search');
                }
                $box_element.css({
                    width: '80%',//$source_element.css('width'),
                    minHeight: '43px',//$source_element.css('height'),
                    padding: $source_element.css('padding'),
                    position: 'relative'
                });
                if (plugin.settings.height === 'element') {
                    $box_element.css({
                        height: $source_element.outerHeight() + 'px'
                    });
                }
                $source_element.after($box_element);
                $source_element.hide();
                // textlength element
                $textlength_element = $(document.createElement('span'));
                $textlength_element.addClass(plugin.settings.prefix + 'textlength');
                $textlength_element.css({
                    position: 'absolute',
                    visibility: 'hidden'
                });
                $box_element.append($textlength_element);
                // chosen items element
                $chosenitems_element = $(document.createElement('div'));
                $chosenitems_element.addClass(plugin.settings.prefix + 'chosen_items');
                $box_element.append($chosenitems_element);
                // input element
                $input_element = $(document.createElement('input'));
                $input_element.addClass(plugin.settings.prefix + 'input');
                if (!plugin.settings.useSearch) {
                    $input_element.attr('readonly', true);
                    $input_element.css({
                        'width': '0px',
                        'height': '0px',
                        'overflow': 'hidden',
                        'border': 0,
                        'padding': 0,
                        'position': 'absolute'
                    });
                } else {
                    if (!multiple) {
                        $input_element.attr('placeholder', plugin.settings.labels.search);
                    } else {
                        $input_element.width(20);
                    }
                }
                $input_element.attr('autocomplete', 'false');
                $box_element.append($input_element);
                // options element
                $options_element = $(document.createElement('ul'));
                $options_element.addClass(plugin.settings.prefix + 'options');

                $box_element.append($options_element);

                //// ================== BIND ELEMENTS EVENTS ================== ////
                // source element
                $source_element.change(function() {
                    refreshSelectedOptions();
                });
                // box element
                $box_element.bind('focus', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    showOptions();
                    $input_element.focus();
                });
                $box_element.bind('mousedown', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    $input_element.focus();
                    if ($input_element[0].setSelectionRange) {
                        $input_element.focus();
                        $input_element[0].setSelectionRange($input_element.val().length, $input_element.val().length);
                    } else if ($input_element[0].createTextRange) {
                        var range = $input_element[0].createTextRange();
                        range.collapse(true);
                        range.moveEnd('character', $input_element.val().length);
                        range.moveStart('character', $input_element.val().length);
                        range.select();
                    }
                });
                $box_element.bind('mouseup', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                });
                $box_element.bind('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if (!multiple || plugin.settings.showAllOptionsOnFocus || !plugin.settings.useSearch) {
                        //showOptions();
                        search();
                    }
                    $input_element.focus();
                });
                $box_element.bind('dblclick', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    $input_element.focus();
                    $input_element.select();
                });
                // input element
                $input_element.bind('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    console.log(2)
                });
                $input_element.bind('dblclick', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                });
                $input_element.bind('keydown', function(e) {
                    e.stopPropagation();
                    var keyCode = e.keyCode || e.which;
                    switch (keyCode) {
                        case key.up:
                            e.preventDefault();
                            if (selected_index > (multiple ? -1 : 0)) {
                                selected_index = selected_index - 1;
                            } else {
                                selected_index = $options_element.find('.' + plugin.settings.prefix + 'option').length - 1;
                            }
                            refreshActiveOption();
                            scrollToActiveOption();
                            break;
                        case key.down:
                            e.preventDefault();
                            if (selected_index < $options_element.find('.' + plugin.settings.prefix + 'option').length - 1) {
                                selected_index = selected_index + 1;
                            } else {
                                selected_index = (multiple ? -1 : 0);
                            }
                            refreshActiveOption();
                            scrollToActiveOption();
                            break;
                        case key.escape:
                            e.preventDefault();
                            break;
                        case key.enter:
                            e.preventDefault();
                            if (selected_index !== -1) {
                                selectOption();
                            } else {
                                if ($input_element.val() !== '') {
                                    plugin.settings.searchCallback();
                                }
                            }
                            resizeInput();
                            break;
                        case key.backspace:
                            if ($input_element.val() === '') {
                                $source_element.find('option:selected').last()[0].selected = false;
                                $source_element.trigger('change');
                                refreshSelectedOptions();
                                search();
                            }
                            resizeInput();
                            break;
                        default:
                            resizeInput();
                            break;
                    }
                });
                $input_element.bind('keyup', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    var keyCode = e.keyCode || e.which;
                    if (keyCode === key.escape || keyCode === key.enter) {
                        hideOptions();
                    } else if (keyCode < 37 || keyCode > 40) {
                        search();
                    }
                    if ($box_element.hasClass('options-hidden') && (keyCode === key.left || keyCode === key.right || keyCode === key.up || keyCode === key.down)) {
                        search();
                    }
                    resizeInput();
                });
                $input_element.bind('focus', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    if (!$options_element.is(':empty') || !multiple || plugin.settings.showAllOptionsOnFocus || !plugin.settings.useSearch) {
                        search();
                        showOptions();
                    }
                });
                $input_element.bind('blur', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    hideOptions();
                });
                refreshSelectedOptions();
            };



            // RESIZE INPUT
            var resizeInput = function() {
                $textlength_element.text($input_element.val());
                if (multiple) {
                    $input_element.css({
                        width: ($textlength_element.width() + 20) + 'px'
                    });
                }
            };



            // REFRESH SELECTED OPTIONS
            plugin.refresh = function() {
                refreshSelectedOptions();
            };
            var refreshSelectedOptions = function() {
                $chosenitems_element.empty();
                $source_element.find('option').each(function() {
                    var $option = $(this);
                    if (this.selected) {
                        var $item_element = $(document.createElement('div'));
                        $item_element.addClass(plugin.settings.prefix + 'chosen_item');
                        //$item_element.html($option.html());

                        // left
                        if ($option.data('left') !== undefined) {
                            var $left_element = $(document.createElement('div'));
                            $left_element.addClass(plugin.settings.prefix + 'chosen_item_left').html($option.attr('data-left'));
                            $item_element.append($left_element);
                        }
                        // right
                        if ($option.data('right') !== undefined) {
                            var $right_element = $(document.createElement('div'));
                            $right_element.addClass(plugin.settings.prefix + 'chosen_item_right').html($option.attr('data-right'));
                            $item_element.append($right_element);
                        }
                        // title
                        var $title_element = $(document.createElement('div'));
                        $title_element.addClass(plugin.settings.prefix + 'chosen_item_title').html($option.html());
                        $item_element.append($title_element);
                        // subtitle
                        if ($option.data('subtitle') !== undefined) {
                            var $subtitle_element = $(document.createElement('div'));
                            $subtitle_element.addClass(plugin.settings.prefix + 'chosen_item_subtitle').html($option.attr('data-subtitle'));
                            $item_element.append($subtitle_element);
                        }
                        // remove button
                        var $button_remove_element = $(document.createElement('div'));
                        $button_remove_element.data('element', this);
                        $button_remove_element.addClass(plugin.settings.prefix + 'chosen_item_remove');
                        $button_remove_element.bind('mousedown', function(e) {
                            e.preventDefault();
                            e.stopPropagation();
                            var nameSkill = $button_remove_element.prev().text();
                            $('.technical-Skill-List ul').find('img[title="'+ nameSkill +'"]').parent().removeClass('active');
                        });
                        $button_remove_element.bind('mouseup', function(e) {
                            e.preventDefault();
                            e.stopPropagation();
                            $(this).data('element').selected = false;
                            $source_element.trigger('change');
                            refreshSelectedOptions();
                            var h = $('.selectator').height();
                            $('.btn-search').height(h);
                            $('.search-form-container').css('min-height', h);
                        });
                        $button_remove_element.html('X');
                        $item_element.append($button_remove_element);
                        // clear
                        var $clear_element = $(document.createElement('div'));
                        $clear_element.css('clear', 'both');
                        $item_element.append($clear_element);

                        $chosenitems_element.append($item_element);
                    }
                });
                search();
            };



            // OPTIONS SEARCH METHOD
            var search = function() {
                selected_index = plugin.settings.selectFirstOptionOnSearch ? ($input_element.val().replace(/\s/g, '') !== '' ? 0 : -1) : -1;
                $options_element.empty();
                if ($input_element.val().replace(/\s/g, '') !== '' || !multiple || plugin.settings.showAllOptionsOnFocus || !plugin.settings.useSearch) {
                    var optionsArray = [];
                    $source_element.children().each(function() {
                        if ($(this).prop('tagName').toLowerCase() === 'optgroup') {
                            var $group = $(this);
                            if ($group.children('option').length !== 0) {
                                var hasMatches = false;
                                $group.children('option').each(function() {
                                    if ((!this.selected || !multiple) && $(this).html().toLowerCase().indexOf($input_element.val().toLowerCase()) !== -1) {
                                        hasMatches = true;
                                    }
                                });
                                if (hasMatches) {
                                    var groupOptionsArray = [];
                                    $group.children('option').each(function() {
                                        if ((!this.selected || !multiple) && $(this).html().toLowerCase().indexOf($input_element.val().toLowerCase()) !== -1) {
                                            groupOptionsArray.push({
                                                type: 'option',
                                                text: $(this).html(),
                                                element: this
                                            });
                                        }
                                    });
                                    optionsArray.push({
                                        type: 'group',
                                        text: $group.attr('label'),
                                        options: groupOptionsArray,
                                        element: $group
                                    });
                                }
                            }
                        } else {
                            if ($(this).html().toLowerCase().indexOf($input_element.val().toLowerCase()) !== -1) {
                                if (!this.selected || !multiple) {
                                    optionsArray.push({
                                        type: 'option',
                                        text: $(this).html(),
                                        element: this
                                    });
                                }
                            }
                        }
                    });
                    generateOptions(optionsArray);
                }
                if ($input_element.is(':focus')) {
                    if (!$options_element.is(':empty') || !multiple) {
                        showOptions();
                    } else {
                        hideOptions();
                    }
                } else {
                    hideOptions();
                }
            };

            // GENERATE OPTIONS
            var generateOptions = function(optionsArray) {
                var index = -1;
                $(optionsArray).each(function() {
                    if (this.type === 'group') {
                        var $group_header_element = $(document.createElement('li'));
                        $group_header_element.addClass(plugin.settings.prefix + 'group_header');
                        $group_header_element.html($(this.element).attr('label'));
                        $options_element.append($group_header_element);

                        var $group = $(document.createElement('ul'));
                        $group.addClass(plugin.settings.prefix + 'group');
                        $options_element.append($group);

                        $(this.options).each(function() {
                            index++;
                            var option = createOption.call(this.element, index);
                            $group.append(option);
                        });

                        $options_element.append($group);
                    } else {
                        index++;
                        var option = createOption.call(this.element, index);
                        $options_element.append(option);
                    }
                });
                refreshActiveOption();
            };

            // CREATE RESULT OPTION
            var createOption = function(index) {
                // holder li
                var $option = $(document.createElement('li'));
                $option.data('index', index);
                $option.data('element', this);
                $option.addClass(plugin.settings.prefix + 'option');
                if (this.selected) {
                    $option.addClass('active');
                }
                // left
                if ($(this).data('left') !== undefined) {
                    var $left_element = $(document.createElement('div'));
                    $left_element.addClass(plugin.settings.prefix + 'option_left').html($(this).attr('data-left'));
                    $option.append($left_element);
                }
                // right
                if ($(this).data('right') !== undefined) {
                    var $right_element = $(document.createElement('div'));
                    $right_element.addClass(plugin.settings.prefix + 'option_right').html($(this).attr('data-right'));
                    $option.append($right_element);
                }
                // title
                var $title_element = $(document.createElement('div'));
                $title_element.addClass(plugin.settings.prefix + 'option_title').html($(this).html());
                $option.append($title_element);
                // subtitle
                if ($(this).data('subtitle') !== undefined) {
                    var $subtitle_element = $(document.createElement('div'));
                    $subtitle_element.addClass(plugin.settings.prefix + 'option_subtitle').html($(this).attr('data-subtitle'));
                    $option.append($subtitle_element);
                }
                // clear
                var $clear_element = $(document.createElement('div'));
                $clear_element.css('clear', 'both');
                $option.append($clear_element);

                // BIND EVENTS
                $option.bind('mouseover', function(e) {
                    e.stopPropagation();
                    e.preventDefault();
                    selected_index = index;
                    refreshActiveOption();
                });
                $option.bind('mousedown', function(e) {
                    e.stopPropagation();
                    e.preventDefault();
                });
                $option.bind('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    selectOption();
                });
                return $option;
            };

            // SHOW OPTIONS AND DIMMER
            var showOptions = function() {
                $box_element.removeClass('options-hidden').addClass('options-visible');  // add title... default
                if (plugin.settings.useDimmer) {
                    $('#' + plugin.settings.prefix + 'dimmer').show();
                }
                setTimeout(function() {
                    $options_element.css('top', ($box_element.outerHeight() + (multiple ? 0 : $input_element.outerHeight()) - 2) + 'px');
                }, 1);
                if ($box_element.hasClass('single')) {
                    selected_index = $options_element.find('.' + plugin.settings.prefix + 'option').index($options_element.find('.' + plugin.settings.prefix + 'option.active'));
                }
                scrollToActiveOption();
            };

            // HIDE OPTIONS AND DIMMER
            var hideOptions = function() {
                $box_element.removeClass('options-visible').addClass('options-hidden');
                if (plugin.settings.useDimmer) {
                    $('#' + plugin.settings.prefix + 'dimmer').hide();
                }
            };

            // REFRESH ACTIVE IN OPTIONS METHOD
            var refreshActiveOption = function() {
                $options_element.find('.active').removeClass('active');
                if (selected_index !== -1) {
                    $options_element.find('.' + plugin.settings.prefix + 'option').eq(selected_index).addClass('active');
                }
            };

            // SCROLL TO ACTIVE OPTION IN OPTIONS LIST
            var scrollToActiveOption = function() {
                var $active_element = $options_element.find('.' + plugin.settings.prefix + 'option.active');
                if ($active_element.length > 0) {
                    $options_element.scrollTop($options_element.scrollTop() + $active_element.position().top - $options_element.height() / 2 + $active_element.height() / 2);
                }

            };

            // SELECT ACTIVE OPTION
            var selectOption = function() {
                $options_element.find('.' + plugin.settings.prefix + 'option').eq(selected_index).data('element').selected = true;
                var nameSkill = $options_element.find('.' + plugin.settings.prefix + 'option').eq(selected_index).find('.selectator_option_title').text();
                $('.technical-Skill-List ul').find('img[title="'+ nameSkill +'"]').parent().addClass('active');
                $source_element.trigger('change');
                refreshSelectedOptions();
                $input_element.val('');
                $box_element.focus();
                if (!plugin.settings.keepOpen) {
                    hideOptions();
                }
                // var h = $('.selectator').height();
                // $('.btn-search').height(h);
                // $('.search-form-container').css('height', h);
            };



            // REMOVE PLUGIN AND REVERT SELECT ELEMENT TO ORIGINAL STATE
            plugin.destroy = function() {
                $box_element.remove();
                $.removeData(element, 'selectator');
                $source_element.show();
                if ($('.selectator').length === 0) {
                    $('#' + plugin.settings.prefix + 'dimmer').remove();
                }
            };

            // Initialize plugin
            plugin.init();
        };

        $.fn.selectator = function(options) {
            options = options !== undefined ? options : {};
            return this.each(function() {
                if (typeof(options) === 'object') {
                    if (undefined === $(this).data('selectator')) {
                        var plugin = new $.selectator(this, options);
                        $(this).data('selectator', plugin);
                    }
                } else if ($(this).data('selectator')[options]) {
                    $(this).data('selectator')[options].apply(this, Array.prototype.slice.call(arguments, 1));
                } else {
                    $.error('Method ' + options + ' does not exist in $.selectator');
                }
            });
        };

    }(jQuery));
	
	var $select = $('.termsList');
    $select.selectator({
        showAllOptionsOnFocus: true
    });
    // search result .... 
    var pageNumber = 0,
        termKeys = {},
        oldKeys ="",
        newKeys = "",
        totalPage = 0,
        countItems = 0,
        jobItems = '';
    $('.btn-search').click(function(){
        if(!$('.selectator_chosen_items').is(':empty')){
            newKeys = "";
            pageNumber = 0;
            getKeyWords();
            oldKeys = newKeys;
            if(newKeys != ''){
                $('.search-result-container-block').empty();
                showSearchResult();
            }
            //loadingMore();
        }
    });
    $scope.$on(jsonValue.events.foundJobs, function(event, data){
        $('.jobs-total').show().find('strong').text(data.total);
        addDataResult(data);
        totalPage = Math.ceil(data.total/countItems);
        $('.loading-paging').fadeOut();
    });
    function getKeyWords(){
        var $this = $('.selectator_chosen_items').find('.selectator_chosen_item_title');
        $this.each(function(){
             newKeys = newKeys + $(this).text() + ' ';
        });
        if(newKeys == oldKeys){
            newKeys ='';
        }
        termKeys["terms"] = newKeys;
        termKeys["pageNumber"] = '' + (++pageNumber);
    }
    function showSearchResult(){
        if(!$('.technical-Skill-List').hasClass('hide')){
            var sForm = $('.search-form-container');
            sForm.parent().animate({
                'padding': '20px 40px'
            },{
                duration: '10000',
                easing: 'easeOutQuad'
            });

            sForm.animate({
                'width': '100%'
            },{
                duration: '10000',
                easing: 'easeOutQuad'
            });
            $('.technical-Skill-List').addClass('hide');
            $('body').css("background-color","#201d1e");
            $('.search-block').css({
                "position":"inherit",
                "height": "auto"
            });
            $('.search-form-container').css("max-width","100%");
        }
        connectionFactory.findJobs(termKeys);
    }
    function addDataResult(json){
        countItems = 0;
        jobItems ="";
        var video = "";
        $.each(json.jobs, function(index, job){
            if(job.logoUrl ==''){
                companyInfo = job.company;
            }else{
                companyInfo = '<img src="'+ job.logoUrl +'" title="'+ job.company +'">';
            }
            if(job.videoUrl == null){
                video = '';
            }else{
                video = '<p><a class="play-video" href="'+ job.videoUrl +'" data-toggle="modal" data-target="#myModal"><i class="fa fa-play-circle"></i></a></p>';
            }
            jobItems = jobItems + '<div class="job-item"><div class="job-infor"><div class="job-title">';
            jobItems = jobItems + '<a href="'+ job.url +'" target="_blank">'+ job.title +'</a></div>';
            jobItems = jobItems + '<ul><li><i class="fa fa-money"></i>Negotiable</li><li><i class="fa fa-map-marker"></i>'+ job.location +'</li><li><i class="fa fa-male"></i>'+ job.level +'';
            jobItems = jobItems + '</li><li><i class="fa fa-calendar"></i>Posted: '+ job.postedOn +'</li></ul></div>';
            jobItems = jobItems + '<div class="company-logo">'+ companyInfo + video +'</div>';
            jobItems = jobItems + '<div class="view-more"><a href="'+ job.url +'" target="_blank">View More</a></div></div></div>';
            countItems = ++countItems;
        });
        $('.search-result-container-block').append(jobItems);
        $('.play-video').on("click",function(){
            var url = '//www.youtube.com/embed/' + $(this).attr('href').substr($(this).attr('href').indexOf("=") + 1) + '?autoplay=1';
            
            $('.modal-body').find('iframe').attr('src',url);
        });
    }
     $(window).scroll(function() {
       if($(window).scrollTop() + $(window).height() == $(document).height()) {
           addMoreData();
       }
    });
    function addMoreData(){
        if (pageNumber >= totalPage) {
            $('.loading-paging').addClass('hide');
            return;
        }
        $('.loading-paging').fadeIn();
        termKeys["pageNumber"] = '' + (++pageNumber);
        connectionFactory.findJobs(termKeys);
    }
}]);