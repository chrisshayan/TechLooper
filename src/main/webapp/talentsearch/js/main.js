$(document).ready(function () {
  menuManagement.init();
  languagesManagetment.init();
  styleSearchForm.init();
  if ($('header').hasClass('home')) {
    menuAnimate();
    loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
    swapMap();
    validationFeedback();
    $('.kz-slider-run').kzSlider();
    countDownday();
  }
  else {
    $('header').addClass('changed');
  }
  settingSelect();
  $(window).resize(function () {
    $('.full-menu').animate({
      height: $(window).height()
    });
  });
  if ($('.talent-results').length > 0) {
    talentItemManager.init();
  }
});
var menuManagement = (function () {
  var hWin = $(window).height();
  $(window).resize(function () {
    hWin = $(window).height();
  });
  var ctrlClose = $('.close-menu'),
    ctrlMenu = $('.menu-icon'),
    init = function () {
      managerClose();
      managerMenu();
    },
    managerClose = function () {
      ctrlClose.find('span').click(function () {
        $(this).animate({
          opacity: 0
        }, 500);
        $('.full-menu').find('ul').animate({
          opacity: 0
        }, 500);
        $('.full-menu').animate({
          opacity: 0,
          height: 0,
          width: 0,
          left: '50%',
          top: '50%',
          'z-index': '-9'
        }, 1000);
        $('body').removeClass('noscroll');
        $('.languages-list').removeClass('active');
      });
    },
    managerMenu = function () {
      ctrlMenu.click(function () {
        $('.full-menu').css('z-index', '99999');
        $('.full-menu').animate({
          opacity: 1,
          height: hWin,
          width: '100%',
          left: 0,
          top: 0
        }, 1000);
        ctrlClose.find('span').animate({
          opacity: 1,
        }, 1500);
        $('.full-menu').find('ul').animate({
          opacity: 1
        }, 500);
        $('body').addClass('noscroll');
      });
    };
  return {
    init: init
  };
})();
var languagesManagetment = (function () {
  var ctrlLang = $('.languages'),
    conLang = $('.languages-list'),
    init = function () {
      langManager();
      settingLang();
    },
    langManager = function () {
      ctrlLang.bind('click', function () {
        conLang.css('height', 'auto');
        conLang.toggleClass('active');
      });
    },
    settingLang = function () {
      var item = conLang.find('.language-item a');
      item.on('click', function () {
        var lang = $(this).text();
        $('.languages').find('span.text').text(lang);
        conLang.removeClass('active');
      });
    };
  return {
    init: init
  };
})();

function settingSelect() {
  $("select").on('change', function () {
    if ($(this).val() == "0") $(this).addClass("empty");
    else $(this).removeClass("empty")
  });
}

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

  // var lpHN = '21.017329',
  //     rpHN = '105.848996',
  //     titleHN = '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi';

  // var lpHCM = '10.770850',
  //     rpHCM = '106.6880500',
  //     titleHCM = 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City';

  lmap.on('click', function () {
    lmap.parent().removeClass('active');
    if ($(this).attr('data-map') == 'HN') {
      loadMap('21.017329', '105.848996', '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi');
    }
    else {
      loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
    }
    $(this).parent().addClass('active');
  });
}

function countDownday() {
  today = new Date();

  BigDay = new Date("April 1, 2015");
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
  $('.send-feedback').click(function (event) {
    event.preventDefault();
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
      feedBack = $('#txtFeedback').val(),
      email = $('#txtEmail').val();
    var errorContent = '';
    $('.error-messages').text('');
    var inputVal = new Array(email, feedBack);
    var inputMessage = new Array("Email", "Your Message");
    $.each(inputVal, function (index, value) {
      if (value == "") {
        if (errorContent == '') {
          errorContent = inputMessage[index];
        }
        else {
          errorContent = errorContent + ', ' + inputMessage[index];
        }
      }
    });
    if (email != '' && !emailReg.test(email)) {
      if (errorContent == '') {
        errorContent = 'Email address is not valid';
      }
      else {
        errorContent = errorContent + ', Email address is not valid';
      }
    }
    if (errorContent != '') {
      $('.error-messages').append('Please enter your <strong>' + errorContent + '</strong>');
    }
    else {
      alert('Thank you for your feedback')
    }
  });
}

function menuAnimate() {
  var windscroll = $(window).scrollTop();
  if ($('.search-form-block').position().top > -60 <= windscroll) {
    $('header').addClass('changed');
  }
  $(window).scroll(function () {
    windscroll = $(window).scrollTop();
    if (windscroll > 0) {
      if ($('.search-form-block').position().top - 60 <= windscroll) {
        $('header').addClass('changed');
      }
      else {
        $('header').removeClass('changed');
      }
    }
  });
}

var styleSearchForm = (function () {
  var init = function () {
    // skill
    $('#input-skill').selectize({
      persist: false,
      createOnBlur: true,
      create: true
    });
    // Job title
    $('#input-job-title').selectize({
      persist: false,
      createOnBlur: true,
      create: true
    });
    // company name
    $('#input-company-name').selectize({
      persist: false,
      createOnBlur: true,
      create: true
    });

    // location
    $('#select-location').selectize({
      maxItems: null,
      valueField: 'id',
      searchField: 'title',
      options: [
        {id: 0, title: 'Ho Chi Minh'},
        {id: 1, title: 'Ha Noi'},
        {id: 2, title: 'Da Nang'},
        {id: 3, title: 'Ba Ria - Vung Tau'},
        {id: 4, title: 'Dong Nai'},
        {id: 5, title: 'Tay Ninh'},
        {id: 6, title: 'Can Tho'},
        {id: 7, title: 'Bac Lieu'},
        {id: 8, title: 'An Giang'},
        {id: 9, title: 'Bac Ning'},
        {id: 10, title: 'Ninh Thuan'},
        {id: 11, title: 'Thua Thien Hue'},
        {id: 12, title: 'Quang Tri'},
        {id: 13, title: 'Binh Thuan'},
        {id: 14, title: 'Lam Dong'},
        {id: 15, title: 'Daklak'}
      ],
      createOnBlur: true,
      create: true,
      render: {
        option: function (data, escape) {
          return '<div class="option">' +
            '<span class="title">' + escape(data.title) + '</span>' +
            '</div>';
        },
        item: function (data, escape) {
          return '<div class="item">' + escape(data.title) + '</div>';
        }
      },
      create: function (input) {
        return {
          id: 0,
          title: input
        };
      }
    });
  };
  return {init: init}
})();

var talentItemManager = (function () {
  var item = $('.talent-item'),
    action = $('.talent-action-block'),
    init = function () {
      mouseOverItem();
    },
    hItem = function () {
      var max = 0;
      item.each(function () {
        if (max < $(this).height()) {
          max = $(this).height();
        }
      });
      return max;
    },
    mouseOverItem = function () {
      item.mouseenter(function () {
        $(this).find('.talent-action-block').stop().animate({
          height: hItem() + 20,
          'min-height': '430px'
        });
      }).mouseleave(function () {
        $(this).find('.talent-action-block').stop().animate({
          height: 0,
          'min-height': 0
        });
      });
    };
  return {init: init}
})();