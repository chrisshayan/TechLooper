techlooper.factory("tsMainService", function (jsonValue, $http) {
  var locations, skills, titles, companies;

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
          $$.loadMap('21.017329', '105.848996', '125-127 Ba Trieu street, Nguyen Du Ward, Hai Ba Trung district Ha Noi');
        }
        else {
          $$.loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
        }
        $(this).parent().addClass('active');
      });
    }
  };

  var instance = {
    applySlider: function () {
      $('.kz-slider-run').kzSlider();
    },

    enableSelectOptions: function () {
      // skill
      skills = $('#input-skill').selectize({
        persist: false,
        createOnBlur: true,
        create: true
      })[0].selectize;

      // Job title
      titles = $('#input-job-title').selectize({
        persist: false,
        createOnBlur: true,
        create: true
      })[0].selectize;

      // company name
      companies = $('#input-company-name').selectize({
        persist: false,
        createOnBlur: true,
        create: true
      })[0].selectize;

      // location
      locations = $('#select-location').selectize({
        maxItems: null,
        valueField: 'id',
        searchField: 'title',
        options: jsonValue.cities,
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
      })[0].selectize;
    },

    location: function () {
      $$.loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
      $$.swapMap();
    },

    searchTalent: function() {
      var request = {
        skills: skills.getValue().split(","),
        locations: locations.getValue().split(","),
        titles: titles.getValue().split(","),
        companies: companies.getValue().split(",")
      }

      $http.post(jsonValue.httpUri.searchTalent, JSON.stringify(request))
        .success(function (data, status, headers, config) {
          console.log(data);
        })
        .error(function (data, status, headers, config) {
          console.log(data);
        });
    },
    
    validationFeedback: function() {
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
  };

  return instance;

});