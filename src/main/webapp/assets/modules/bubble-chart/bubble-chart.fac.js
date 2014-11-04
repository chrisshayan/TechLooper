angular.module('Bubble').factory('bubbleFactory', ["utils", "jsonValue", '$location', function(utils, jsonValue, $location) {
   var terms = [];
   var totalJobs = 0;
   var scope;

   var mT = "70px",
      mL = "20px",
      dT = "80px",
      dL = "50px",
      lPosition = "",
      tPosition = "",
      mSize = "220px",
      dSize = "320px",
      sActive = "";
   
   var termsMap = {};

   return {

      initializeAnimation: function ($scope) {
         scope = $scope;
         if (utils.isMobile()) {
            lPosition = mL;
            tPosition = mT;
            sActive = mSize;
         } else {
            lPosition = dL;
            tPosition = dT;
            sActive = dSize;
            
         }

         var bubblePosition = utils.isMobile() ?  jsonValue.mBubblePosition : jsonValue.dBubblePosition;
         var n = bubblePosition.length,
            nameTech = new Array(),
            java_locations = new Array(),
            dotnet_locations = new Array(),
            php_locations = new Array(),
            pm_locations = new Array(),
            qa_locations = new Array(),
            ruby_locations = new Array(),
            python_locations = new Array(),
            ba_locations = new Array(),
            dba_locations = new Array();

         for (var i = 0; i < n; i++) {
            nameTech[i] = bubblePosition[i].name;
            for (var j = 0; j < n; j++) {
               var t = bubblePosition[i].data[j].top,
                  l = bubblePosition[i].data[j].left;

               if (nameTech[i] == 'java') {
                  java_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == '.net') {
                  dotnet_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'php') {
                  php_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'ruby') {
                  ruby_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'python') {
                  python_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'pm') {
                  pm_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'qa') {
                  qa_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'ba') {
                  ba_locations[j] = new Array(t, l);
               }
               if (nameTech[i] == 'dba') {
                  dba_locations[j] = new Array(t, l);
               }
            }
         }

         $('.circle').click(function (e) {
            e.preventDefault();

            if($(this).hasClass('active')){
               var termName =  $(this).find('.termcount').text().replace(/[0-9]/g, '');
               var path = jsonValue.routerUris.analyticsTechnical + '/'+ termName;
               $location.path(path);
               scope.$apply();
            }   

            var sDotnet = $('.DOTNETTech').height(),

               sJava = $('.JAVATech').height(),

               sPhp = $('.PHPTech').height(),

               sRuby = $('.RUBYTech').height(),

               sPython = $('.PYTHONTech').height(),

               sQc = $('.QATech').height(),

               sPm = $('.PROJECT_MANAGERTech').height(),

               sDba = $('.DBATech').height(),

               sBa = $('.BATech').height();

            var circle = $(this);
            var circle_id = $(this).attr('data-techTerm');

            if (!circle.hasClass('active')) {
               $(this).children(':not(".circle-content")').hide();

               $('.inactive span, .inactive .intro').hide(); // hides the intro on the main circle before it is animated away

               // move the new circle and increase size to center
               $('.' + circle_id).addClass('active').removeClass('small').animate({
                  'top': tPosition,
                  'left': lPosition,
                  'z-index': 0
               }).children('.circle-content').animate({
                  'width': sActive,
                  'height': sActive
               }, {
                  duration: '4000',
                  easing: 'easeOutQuad'
               }).find('.ball-highlight').css({
                  'width': sActive,
                  'height': sActive
               });

               // add the title and content once the circle has resized itself
               setTimeout((function () {
                  $('.' + circle_id).children(':not(".circle-content")').slideDown('fast');
               }), 500);

               /*
                * 1 Java is active
                **/
               if (circle_id == 'JAVATech') {

                  $('.PROJECT_MANAGERTech').animate({
                     'left': pm_locations[0][1],
                     'top': pm_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });

                  $('.PHPTech').animate({
                     'left': php_locations[0][1],
                     'top': php_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp,
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });

                  $('.DOTNETTech').animate({
                     'left': dotnet_locations[0][1],
                     'top': dotnet_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.RUBYTech').animate({
                     'left': ruby_locations[0][1],
                     'top': ruby_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });


                  $('.PYTHONTech').animate({
                     'left': python_locations[0][1],
                     'top': python_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.DBATech').animate({
                     'left': dba_locations[0][1],
                     'top': dba_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'left': ba_locations[0][1],
                     'top': ba_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.QATech').animate({
                     'left': qa_locations[0][1],
                     'top': qa_locations[0][0]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });
               } // end if circle id = .Net
               /*
                * 2 .Net is active
                **/
               if (circle_id == 'DOTNETTech') {

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[1][0],
                     'left': pm_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[1][0],
                     'left': php_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[1][0],
                     'left': java_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.RUBYTech').animate({
                     'top': ruby_locations[1][0],
                     'left': ruby_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });


                  $('.PYTHONTech').animate({
                     'top': python_locations[1][0],
                     'left': python_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[1][0],
                     'left': dba_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'top': ba_locations[1][0],
                     'left': ba_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.QATech').animate({
                     'top': qa_locations[1][0],
                     'left': qa_locations[1][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });
               } // end if circle id = .Net

               /*
                * 3 PHP is active
                **/
               if (circle_id == 'PHPTech') {

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[2][0],
                     'left': pm_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[2][0],
                     'left': java_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[2][0],
                     'left': dotnet_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.RUBYTech').animate({
                     'top': ruby_locations[2][0],
                     'left': ruby_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });


                  $('.PYTHONTech').animate({
                     'top': python_locations[2][0],
                     'left': python_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[2][0],
                     'left': dba_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'top': ba_locations[2][0],
                     'left': ba_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.QATech').animate({
                     'top': qa_locations[2][0],
                     'left': qa_locations[2][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });
               } // end if circle id = .Net

               /*
                * 4 Ruby is active
                **/
               if (circle_id == 'RUBYTech') {

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[3][0],
                     'left': pm_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[3][0],
                     'left': java_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[3][0],
                     'left': dotnet_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[3][0],
                     'left': php_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });


                  $('.PYTHONTech').animate({
                     'top': python_locations[3][0],
                     'left': python_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[3][0],
                     'left': dba_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'top': ba_locations[3][0],
                     'left': ba_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.QATech').animate({
                     'top': qa_locations[3][0],
                     'left': qa_locations[3][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });
               } // end if circle id = .Net

               /*
                * 5 Python is active
                **/
               if (circle_id == 'PYTHONTech') {

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[4][0],
                     'left': pm_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[4][0],
                     'left': java_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[4][0],
                     'left': dotnet_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[4][0],
                     'left': php_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });


                  $('.RUBYTech').animate({
                     'top': ruby_locations[4][0],
                     'left': ruby_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[4][0],
                     'left': dba_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'top': ba_locations[4][0],
                     'left': ba_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.QATech').animate({
                     'top': qa_locations[4][0],
                     'left': qa_locations[4][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });
               } // end if circle id = Python

               /*
                * 6 Project Manager is active
                **/
               if (circle_id == 'PROJECT_MANAGERTech') {

                  $('.PYTHONTech').animate({
                     'top': python_locations[5][0],
                     'left': python_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[5][0],
                     'left': java_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[5][0],
                     'left': dotnet_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[5][0],
                     'left': php_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });


                  $('.RUBYTech').animate({
                     'top': ruby_locations[5][0],
                     'left': ruby_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[5][0],
                     'left': dba_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'top': ba_locations[5][0],
                     'left': ba_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.QATech').animate({
                     'top': qa_locations[5][0],
                     'left': qa_locations[5][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });
               } // end if circle id = Project Manager

               /*
                * 7 Quanity Control is active
                **/
               if (circle_id == 'QATech') {

                  $('.PYTHONTech').animate({
                     'top': python_locations[6][0],
                     'left': python_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[6][0],
                     'left': java_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[6][0],
                     'left': dotnet_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[6][0],
                     'left': php_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });


                  $('.RUBYTech').animate({
                     'top': ruby_locations[6][0],
                     'left': ruby_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[6][0],
                     'left': dba_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.BATech').animate({
                     'top': ba_locations[6][0],
                     'left': ba_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[6][0],
                     'left': pm_locations[6][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });
               } // end if circle id = .Net

               /*
                * 8 DBA is active
                **/
               if (circle_id == 'DBATech') {

                  $('.PYTHONTech').animate({
                     'top': python_locations[7][0],
                     'left': python_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[7][0],
                     'left': java_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[7][0],
                     'left': dotnet_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[7][0],
                     'left': php_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });


                  $('.RUBYTech').animate({
                     'top': ruby_locations[7][0],
                     'left': ruby_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });

                  $('.QATech').animate({
                     'top': qa_locations[7][0],
                     'left': qa_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });

                  $('.BATech').animate({
                     'top': ba_locations[7][0],
                     'left': ba_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sBa,
                     'height': sBa
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sBa,
                     'height': sBa
                  });

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[7][0],
                     'left': pm_locations[7][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });
               } // end if circle id = DBA

               /*
                * 9 Business Analytics is active
                **/
               if (circle_id == 'BATech') {

                  $('.PYTHONTech').animate({
                     'top': python_locations[8][0],
                     'left': python_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPython,
                     'height': sPython
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPython,
                     'height': sPython
                  });

                  $('.JAVATech').animate({
                     'top': java_locations[8][0],
                     'left': java_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sJava,
                     'height': sJava
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sJava,
                     'height': sJava
                  });

                  $('.DOTNETTech').animate({
                     'top': dotnet_locations[8][0],
                     'left': dotnet_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDotnet,
                     'height': sDotnet
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDotnet,
                     'height': sDotnet
                  });

                  $('.PHPTech').animate({
                     'top': php_locations[8][0],
                     'left': php_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPhp,
                     'height': sPhp
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPhp,
                     'height': sPhp
                  });


                  $('.RUBYTech').animate({
                     'top': ruby_locations[8][0],
                     'left': ruby_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sRuby,
                     'height': sRuby
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sRuby,
                     'height': sRuby
                  });

                  $('.QATech').animate({
                     'top': qa_locations[8][0],
                     'left': qa_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sQc,
                     'height': sQc
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sQc,
                     'height': sQc
                  });

                  $('.DBATech').animate({
                     'top': dba_locations[8][0],
                     'left': dba_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sDba,
                     'height': sDba
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sDba,
                     'height': sDba
                  });

                  $('.PROJECT_MANAGERTech').animate({
                     'top': pm_locations[8][0],
                     'left': pm_locations[8][1]
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).addClass('small').removeClass('active').children('.circle-content').animate({
                     'width': sPm,
                     'height': sPm
                  }, {
                     duration: '4000',
                     easing: 'easeOutQuad'
                  }).find('.ball-highlight').css({
                     'width': sPm,
                     'height': sPm
                  });
               } // end if circle id = Business Analytics

               $('.small .intro').hide();


               //remove the inactive class and slide the title back in
               setTimeout((function () {
                  $('#box div.inactive').removeClass('inactive').children('span').fadeIn('fast');
               }), 300);

            } // end if($(this).hasClass('active'))
         });


         // First position
         var biggestDiameter = 0,
            biggestName = "",
            count = 0,
            locations = new Array(),
            sBubble = 0,
            resize = 0;

         if (utils.isMobile()) {
            sActive = mSize;
            lPosition = mL;
            tPosition = mT;
            resize = -10;
         } else {
            sActive = dSize;
            lPosition = dL;
            tPosition = dT;
            resize = 25;
         }

         bubblePosition = utils.isMobile() ? jsonValue.mPositionDefault : jsonValue.dPositionDefault;
         for (var i = 0; i < 9; i++) {
            var t = bubblePosition[0].data[i].top,
               l = bubblePosition[0].data[i].left;
            locations[i] = new Array(t, l);
         }
         for (var i = 0; i < terms.length; i++) {
            if (terms[i].count > biggestDiameter) {
               biggestDiameter = terms[i].count;
               biggestName = terms[i].term + 'Tech';
            }
         }

         for (var i = 0; i < terms.length; i++) {
            if (terms[i].count < biggestDiameter) {
               var n = Math.round(parseInt(terms[i].count) * 100 / totalJobs),
                  fSize = '';

               if (n < 11) {
                  fSize = 'textSize1';
                  sBubble = 55 + resize;
               } else if (n > 10 && n < 21) {
                  fSize = 'textSize2';
                  sBubble = 75 + resize;
               } else if (n > 20 && n < 31) {
                  fSize = 'textSize3';
                  sBubble = 100 + resize;
               } else if (n > 30 && n < 41) {
                  fSize = 'textSize4';
                  sBubble = 125 + resize;
               } else if (n > 40 && n < 51) {
                  fSize = 'textSize5';
                  sBubble = 150 + resize;
               } else if (n > 50 && n < 61) {
                  fSize = 'textSize6';
                  sBubble = 175 + resize;
               } else if (n > 60 && n < 71) {
                  fSize = 'textSize7';
                  sBubble = 200 + resize;
               } else if (n > 70 && n < 81) {
                  fSize = 'textSize8';
                  sBubble = 225 + resize;
               } else if (n > 80 && n < 91) {
                  fSize = 'textSize9';
                  sBubble = 250 + resize;
               } else {
                  fSize = 'bigText';
                  sBubble = 275 - resize;
               }
               $('.' + terms[i].term + 'Tech').css({
                  'top': locations[i][0],
                  'left': locations[i][1],
                  'opacity': 0.8,
                  'z-index': i + 1
               }).addClass('small').removeClass('active').children('.circle-content').css({
                  'width': sBubble,
                  'height': sBubble
               });
            }
         }
         $('.' + biggestName).css({
            'top': tPosition,
            'left': lPosition,
            'z-index': 1
         });
         $('.' + biggestName).addClass('active').children('.circle-content').css({
            'width': sActive,
            'height': sActive
         }).find('.ball-highlight').css({
            'width': sActive,
            'height': sActive
         });
      },

      setTerms: function ($terms) {
         $("#box").empty();
         terms = $terms;
         totalJobs = utils.sum(terms, "count");
      },

      getTotalJobs: function () {
         return totalJobs;
      },

      draw: function (bubbleItem, force) {
         if (force !== true && termsMap[bubbleItem.termID] !== undefined) {
            var currentTerm = termsMap[bubbleItem.termID];
            if (currentTerm.count === bubbleItem.count) {
               return false;
            }
            currentTerm.count = bubbleItem.count;
            var bItem =  $("div[data-techTerm='" + bubbleItem.termID +"Tech']");
            bItem.find('.ball-highlight').css('display','block');
            bItem.find("span.termcount strong").text(bubbleItem.count);

            setTimeout((function () {
               bItem.find('.ball-highlight').css('display','none');
            }),3000);
            
            return;
         }
         termsMap[bubbleItem.termID] = bubbleItem;

         var html = '',
            clColor = '',
            fSize = '',
            diameterPlus = 0;

         if (utils.isMobile()) {
            diameterPlus = -10;
         } else {
            diameterPlus = 25;
            
         }
         var n = Math.round(parseInt(bubbleItem.count) * 100 / totalJobs),
            // These numbers are in pixel
            diameter = 0;

         if (n < 11) {
            fSize = 'textSize1';
            diameter = 55 + diameterPlus;
         } else if (n > 10 && n < 21) {
            fSize = 'textSize2';
            diameter = 75 + diameterPlus;
         } else if (n > 20 && n < 31) {
            fSize = 'textSize3';
            diameter = 100 + diameterPlus;
         } else if (n > 30 && n < 41) {
            fSize = 'textSize4';
            diameter = 125 + diameterPlus;
         } else if (n > 40 && n < 51) {
            fSize = 'textSize5';
            diameter = 150 + diameterPlus;
         } else if (n > 50 && n < 61) {
            fSize = 'textSize6';
            diameter = 175 + diameterPlus;
         } else if (n > 60 && n < 71) {
            fSize = 'textSize7';
            diameter = 200 + diameterPlus;
         } else if (n > 70 && n < 81) {
            fSize = 'textSize8';
            diameter = 225 + diameterPlus;
         } else if (n > 80 && n < 91) {
            fSize = 'textSize9';
            diameter = 250 + diameterPlus;
         } else {
            fSize = 'bigText';
            diameter = 275 + diameterPlus;
         }

         switch (bubbleItem.colorID) {
         case 0:
            clColor = "redColor";
            break;
         case 1:
            clColor = "blueColor";
            break;
         case 2:
            clColor = "yellowColor";
            break;
         case 3:
            clColor = "pinkColor";
            break;
         case 4:
            clColor = "greenColor";
            break;
         case 5:
            clColor = "orangeColor";
            break;
         case 6:
            clColor = "lightSalmonColor";
            break;
         case 7:
            clColor = "indigoColor";
            break;
         case 8:
            clColor = "oliveColor";
            break;
         };
         var rota = diameter - 17;
         html = '<div data-techTerm="' + bubbleItem.termID + 'Tech" class="circle ' + bubbleItem.termID + 'Tech ' + fSize + '" style="width:' + diameter + 'px; height:' + diameter + 'px">';
         html = html + '<div class="circle-content ' + clColor + '" style="width:' + diameter + 'px; height:' + diameter + 'px">';
         html = html + '<span class="termcount"><strong>' + bubbleItem.count + '</strong>' + bubbleItem.termName + '</span>';
         html = html + '<div class="ball-highlight" style="width:' + diameter + 'px; height:' + diameter + 'px"></div></div></div>';
         $('.bubble-chart-container').append(html);
      }
   }
}]);