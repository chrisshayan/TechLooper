techlooper.factory("tsMainService", function () {
  var $$ = {
    loadMap: function (lp, rp, title) {
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
    },
    swapMap: function () {
      var lmap = $('.address-info').find('.head-name');

      //var lpHN = '21.017329',
      //    rpHN = '105.848996',
      //    titleHN = '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi';
      //
      //var lpHCM = '10.770850',
      //    rpHCM = '106.6880500',
      //    titleHCM = 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City';

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
  };
  var instance = {
    applySlider: function () {
      $('.kz-slider-run').kzSlider();
    },

    enableSelectOptions: function() {
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
          option: function(data, escape) {
            return '<div class="option">' +
              '<span class="title">' + escape(data.title) + '</span>' +
              '</div>';
          },
          item: function(data, escape) {
            return '<div class="item">'+ escape(data.title)  + '</div>';
          }
        },
        create: function(input) {
          return {
            id: 0,
            title: input
          };
        }
      });
    },

    location: function () {
      $$.loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
      $$.swapMap();
    }
  };

  return instance;

});