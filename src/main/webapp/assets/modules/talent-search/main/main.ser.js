techlooper.factory("tsMainService", function () {
  var $$ ={
    loadMap: function(lp, rp, title){
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
    swapMap: function() {
    var lmap = $('.address-info').find('.head-name');

    //var lpHN = '21.017329',
    //    rpHN = '105.848996',
    //    titleHN = '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi';
    //
    //var lpHCM = '10.770850',
    //    rpHCM = '106.6880500',
    //    titleHCM = 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City';

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
  };
  var instance = {
      applySlider: function(){
        $('.kz-slider-run').kzSlider();
      },
      location: function(){
        $$.loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
        $$.swapMap();
      }
  };

  return instance;

});