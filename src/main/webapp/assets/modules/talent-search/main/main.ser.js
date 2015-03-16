techlooper.factory("tsMainService", function (jsonValue, $http, $location) {
  //var locations, skills, titles, companies;
  var searchRequest = {};

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
      $.each([
        {key: "skills", selector: "#input-skill"},
        {key: "titles", selector: "#input-job-title"},
        {key: "companies", selector: "#input-company-name"},
        {key: "locations", selector: "#select-location", options: jsonValue.cities},
      ], function (i, item) {
        searchRequest[item.key] = $(item.selector).selectize({
          plugins: {
            "remove_button": {},
            "restore_on_backspace": {}
          },
          sortField: "text",
          mode: "multi",
          persist: false,
          createOnBlur: false,
          create: true,
          valueField: "text",
          searchField: ['text'],
          labelField: "text",
          createFilter: function (input) {
            var ok = true;
            $.each(this.options, function (index, value) {
              if (value.text.toLowerCase() === input.toLowerCase()) {
                ok = false;
                return false;
              }
            });
            return ok;
          },
          render: {
            item: function (item, escape) {
              return "<div>" + item.text + " </div>";
            },
            option: function (item, escape) {
              return "<div>" + item.text + " </div>";
            }
          }
        })[0].selectize;

        if (item.options !== undefined) {
          searchRequest[item.key].addOption(item.options);
        }
      });
    },

    location: function () {
      $$.loadMap('10.770850', '106.6880500', 'Navigos Group Vietnam : 130 Suong Nguyet Anh Street, Ben Thanh Ward, District 1, Ho Chi Minh City');
      $$.swapMap();
    },

    searchTalent: function () {
      var request = {
        skills: searchRequest.skills.getValue().split(","),
        locations: searchRequest.locations.getValue().split(","),
        titles: searchRequest.titles.getValue().split(","),
        companies: searchRequest.companies.getValue().split(",")
      }
      var hasSearchField = false;
      for (var prop in request) {
        if (request[prop][0] !== "") {
          hasSearchField = true;
          break;
        }
      }
      if (!hasSearchField) {
        return;
      }

      var queryArray = CryptoJS.enc.Utf8.parse(JSON.stringify(request));
      $location.path(jsonValue.routerUris.talentSearchResult + "/" + CryptoJS.enc.Base64.stringify(queryArray));
    },

    validationFeedback: function () {
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
    },

    countdown: function () {
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
    },

    getSearchRequest: function () {
      return searchRequest;
    }
  };

  return instance;

});