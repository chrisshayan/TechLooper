$(document).ready(function() {
  menuManagement();
  languagesManagetment.init();
  menuAnimate();
  settingSelect();
  bannerSlider.init();
  //cbpFixedScrollLayout.init();
  $('.kz-slider-run').kzSlider();
  loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
  countDownday();
  validationFeedback();
  swapMap();
  contentAnimate();
});
function menuManagement(){
  var contentMenu = $('.navbar ul'),
      ctrlMenu = $('.menu-bar');
  ctrlMenu.bind('click', function(){
    var hScreen = $(window).height();
    var wScreen = $(window).width();
    contentMenu.css('height', 'auto');
    $('.languages-list').removeClass('active');
    contentMenu.toggleClass('active');
    if(wScreen > 319 && wScreen < 720){
      contentMenu.animate({
        height: hScreen
      });
    }
  });
}
var languagesManagetment = (function(){
  var ctrlLang = $('.languages'),
      conLang = $('.languages-list'),
  init = function(){
    langManager();
    settingLang();
  },
  langManager = function(){
    ctrlLang.bind('click', function(){
      var hScreen = $(window).height();
      var wScreen = $(window).width();
      conLang.css('height', 'auto');
      conLang.toggleClass('active');
      if(wScreen > 319 && wScreen < 720){
        conLang.animate({
          height: hScreen
        });
      }
    });
  },
  settingLang = function(){
    var item = conLang.find('.language-item a');
    item.on('click', function(){
      var lang = $(this).text();
      $('.languages').find('span.text').text(lang);
      conLang.removeClass('active');
    });
  };
  return { init: init };
})();
function settingSelect() {
    $("select").on('change', function() {
        if ($(this).val() == "0") $(this).addClass("empty");
        else $(this).removeClass("empty")
    });
}
var bannerSlider = (function() {

    var $navArrows = $('#nav-arrows'),
        $nav = $('#nav-dots > span'),
        slitslider = $('#slider').slitslider({
            autoplay: true,
            speed: 1500,
            optOpacity: true,
            onBeforeChange: function(slide, pos) {

                $nav.removeClass('nav-dot-current');
                $nav.eq(pos).addClass('nav-dot-current');

            }
        }),

        init = function() {

            initEvents();

        },
        initEvents = function() {

            // add navigation events
            $navArrows.children(':last').on('click', function() {

                slitslider.next();
                return false;

            });

            $navArrows.children(':first').on('click', function() {

                slitslider.previous();
                return false;

            });

            $nav.each(function(i) {

                $(this).on('click', function(event) {

                    var $dot = $(this);

                    if (!slitslider.isActive()) {

                        $nav.removeClass('nav-dot-current');
                        $dot.addClass('nav-dot-current');

                    }

                    slitslider.jump(i + 1);
                    return false;

                });

            });

        };

    return {
        init: init
    };
})();

var cbpFixedScrollLayout = (function() {
    var config = {
        $sections: $('#cbp-fbscroller > section'),
        $navlinks: $('#cbp-fbscroller > nav:first > a'),
        currentLink: 0,
        $body: $('html, body'),
        animspeed: 650,
        animeasing: 'easeInOutExpo'
    };

    function init() {
        config.$navlinks.on('click', function() {
            scrollAnim(config.$sections.eq($(this).index()).offset().top);
            return false;
        });
        config.$sections.waypoint(function(direction) {
            if (direction === 'down') {
                changeNav($(this));
            }
        }, {
            offset: '30%'
        }).waypoint(function(direction) {
            if (direction === 'up') {
                changeNav($(this));
            }
        }, {
            offset: '-30%'
        });

        // on window resize: the body is scrolled to the position of the current section
        $(window).on('debouncedresize', function() {
            scrollAnim(config.$sections.eq(config.currentLink).offset().top);
        });

    }

    // update the current navigation link
    function changeNav($section) {
        config.$navlinks.eq(config.currentLink).removeClass('cbp-fbcurrent');
        config.currentLink = $section.index('section');
        config.$navlinks.eq(config.currentLink).addClass('cbp-fbcurrent');
    }

    // function to scroll / animate the body
    function scrollAnim(top) {
        config.$body.stop().animate({
            scrollTop: top
        }, config.animspeed, config.animeasing);
    }

    return {
        init: init
    };
})();

function loadMap(lp, rp, title) {
    var mapCanvas = document.getElementById('location-map');
    var myLatlng = new google.maps.LatLng(lp, rp);
    var myOptions = {
        zoom: 17,
        center: myLatlng,
        scrollwheel: false,
        navigationControl: false,
        mapTypeControl: false,
        scaleControl: false,
        styles: [{
            "featureType": "landscape",
            "stylers": [{
                "saturation": -100
            }, {
                "lightness": 65
            }, {
                "visibility": "on"
            }]
        }, {
            "featureType": "poi",
            "stylers": [{
                "saturation": -100
            }, {
                "lightness": 51
            }, {
                "visibility": "simplified"
            }]
        }, {
            "featureType": "road.highway",
            "stylers": [{
                "saturation": -100
            }, {
                "visibility": "simplified"
            }]
        }, {
            "featureType": "road.arterial",
            "stylers": [{
                "saturation": -100
            }, {
                "lightness": 30
            }, {
                "visibility": "on"
            }]
        }, {
            "featureType": "road.local",
            "stylers": [{
                "saturation": -100
            }, {
                "lightness": 40
            }, {
                "visibility": "on"
            }]
        }, {
            "featureType": "transit",
            "stylers": [{
                "saturation": -100
            }, {
                "visibility": "simplified"
            }]
        }, {
            "featureType": "administrative.province",
            "stylers": [{
                "visibility": "off"
            }]
        }, {
            "featureType": "water",
            "elementType": "labels",
            "stylers": [{
                "visibility": "on"
            }, {
                "lightness": -25
            }, {
                "saturation": -100
            }]
        }, {
            "featureType": "water",
            "elementType": "geometry",
            "stylers": [{
                "hue": "#ffff00"
            }, {
                "lightness": -25
            }, {
                "saturation": -97
            }]
        }],
        mapTypeId: google.maps.MapTypeId.ROADMAP
    }
    map = new google.maps.Map(mapCanvas, myOptions);
    var marker = new google.maps.Marker({
        position: myLatlng,
        map: map,
        title: title
    });
}

function swapMap() {
    var lmap = $('.address-info').find('.head-name');

    var lpHN = '21.017329',
        rpHN = '105.848996',
        titleHN = '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi';

    var lpHCM = '10.770850',
        rpHCM = '106.6880500',
        titleHCM = 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City';

    lmap.on('click', function() {
        lmap.parent().removeClass('active');
        if ($(this).attr('data-map') == 'HN') {
            loadMap('21.017329', '105.848996', '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi');
        } else {
            loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
        }
        $(this).parent().addClass('active');
    });
}

function countDownday() {
    today = new Date();

    BigDay = new Date("March 2, 2015");
    msPerDay = 24 * 60 * 60 * 1000;
    timeLeft = (BigDay.getTime() - today.getTime());
    e_daysLeft = timeLeft / msPerDay;
    daysLeft = Math.floor(e_daysLeft);
    e_hrsLeft = (e_daysLeft - daysLeft) * 24;
    hrsLeft = Math.floor(e_hrsLeft);
    minsLeft = Math.floor((e_hrsLeft - hrsLeft) * 60);
    $('.count-down-day span').text(daysLeft);
}

function validationFeedback() {
    $('.send-feedback').click(function(event) {
        event.preventDefault();
        var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
            feedBack = $('#txtFeedback').val(),
            email = $('#txtEmail').val();
        var errorContent = '';
        $('.error-messages').text('');
        var inputVal = new Array(email, feedBack);
        var inputMessage = new Array("Email", "Your Message");
        $.each(inputVal, function(index, value) {
            if (value == "") {
                if (errorContent == '') {
                    errorContent = inputMessage[index];
                } else {
                    errorContent = errorContent + ', ' + inputMessage[index];
                }
            }
        });
        if (email != '' && !emailReg.test(email)) {
            if (errorContent == '') {
                errorContent = 'Email address is not valid';
            } else {
                errorContent = errorContent + ', Email address is not valid';
            }
        }
        if (errorContent != '') {
            $('.error-messages').append('Please enter your <strong>' + errorContent + '</strong>');
        } else {
            alert('Thank you for your feedback')
        }
    });
}

function menuAnimate() {
    var windscroll = $(window).scrollTop();
    if ($('.reasons').position().top > -60 <= windscroll) {
        $('header').addClass('changed');
    }
    $(window).scroll(function() {
        windscroll = $(window).scrollTop();
        if (windscroll > 0) {
            if ($('.reasons').position().top - 60 <= windscroll) {
                $('header').addClass('changed');
            } else {
                $('header').removeClass('changed');
            }
        }
    });
}

function contentAnimate() {
  $(window).scroll(function() {
    var windscroll = $(window).scrollTop();
    if (windscroll > 0) {
      if ($('.reasons').position().top - 60 <= windscroll) {
        $('.reasons-content li').addClass('transition');
      } else {
        $('.reasons-content li').removeClass('transition');
      }
    }
  });
}