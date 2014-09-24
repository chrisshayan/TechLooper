var app = angular
    .module('app', [
        'ngRoute'
    ])

app.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'partials/home.html',
                controller: 'PageCtrl'
            })
    }
]);

app.controller("loadKeyboard", function($scope, $http) {
    $http.get('data/keyboardshortcuts.json').
    success(function(data, status, headers, config) {
        $scope.keyList = data;
    }).
    error(function(data, status, headers, config) {
        // log error
    });
});

app.controller('PageCtrl', ['$scope',
    function($scope) {
        $scope.tagline = 'Career Analytics. Open Source. Awesome!';
    }
]);



app.controller('openKeyboardShortcuts', ['$scope',
    function($scope) {
        var key = $('.keyboard');
        key.on('click', function() {
            if ($(".keyboard-shortcuts-items:first").is(":hidden")) {
                $(".keyboard-shortcuts-items").slideDown("slow");
            } else {
                $(".keyboard-shortcuts-items").hide();
            }
        });
    }
]);

app.controller('settingStyle', ['$scope',
    function($scope) {
        var set = $('.setting-content');
        set.on('click', function() {
            set.find('ul.setting-items').css("display", "block");
            set.stop().animate({
                width: '125'
            });
        }).mouseleave(function() {
            set.find('ul.setting-items').css("display", "none");
            set.stop().animate({
                width: '28px'
            });
            $(".keyboard-shortcuts-items").hide();
        });
    }
]);
app.controller('openCompaniesBar', ['$scope',
    function($scope) {
        var cp = $('.companies-bar'),
            list = $('.companies-list');
        cp.click(function() {
            var ic = $(this).find('i');
            if (ic.hasClass('fa-arrow-up')) {
                ic.removeClass('fa-arrow-up').addClass('fa-arrow-down');
                list.animate({
                    height: '120px',
                    padding: '10px'
                });
            } else {
                ic.removeClass('fa-arrow-down').addClass('fa-arrow-up');
                list.animate({
                    height: '0',
                    padding: '5px 10px'
                });
            }
        });
    }
]);
app.controller("loadCompanies", function($scope, $http) {
    $http.get('data/companies.json').
    success(function(data, status, headers, config) {
        $scope.companies = data;
    }).
    error(function(data, status, headers, config) {
        console.logError("Error in Loading companies.json", status);
    });
});

app.controller("loadTech", function($scope) {
    var socket = new SockJS('ws');
        stompClient = Stomp.over(socket),
        currentTerms = new Array,
        totalTerms = 0,
        totalJobs = 0,
        rColor = 0;

    stompClient.debug = function(){};
    $scope.$on('terms-coming', function(event, terms) {
       $.each(terms, function(index, value) {
          $scope.$emit('term', value);
        });
    });
    $scope.$on('term', function(event, data) {
       stompClient.subscribe('/topic/technical-job/' + data.term, function(response) {
            rColor = rColor + 1;
            if(rColor == 9){
                rColor = 0;
            }
            $.each(currentTerms, function(index, value){
                if(data.term == value.term && data.count != value.count){
                    $scope.$emit('draw-bubble', {'colorID': rColor, 'count' : data.count, 'termName': data.name, 'termID': data.term});
                }
            });
       });
       stompClient.subscribe('/topic/technical-job/total', function(response) {
            $scope.$emit('get-total-jobs', JSON.parse(response.body).count);
        });
    });
    $scope.$on('get-total-jobs', function(event, jobs) {
       totalJobs = jobs;
    });

    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/technical-job/terms', function(response) {
            currentTerms = JSON.parse(response.body);
            $scope.termList = JSON.parse(response.body);
            $.each($scope.termList, function(index, value) {
                totalJobs += value.count;
            });
            $.each($scope.termList, function(index, value) {
                rColor = rColor + 1;
                if(rColor == 9){
                    rColor = 0;
                }
                $scope.$emit('draw-bubble', {'colorID': rColor, 'count' : value.count, 'termName': value.name, 'termID':value.term });
            });
            $scope.$emit('terms-coming', $scope.termList);
        });
        stompClient.send("/app/technical-job/terms");
    });

    $scope.$on('draw-bubble', function(event, bubbleItem){
        var html ='',
            clColor = '',
            fSize = '';

        var n = Math.round(parseInt(bubbleItem.count)*100/parseInt(totalJobs)),
            diameter = 0;
        if( n < 11){
            fSize = 'textSize1';
            diameter = 55;
        }else if(n > 10 && n < 21){
            fSize = 'textSize2';
            diameter = 75;
        }else if(n > 20 && n < 31){
            fSize = 'textSize3';
            diameter = 100;
        }else if(n > 30 && n < 41){
            fSize = 'textSize4';
            diameter = 125;
        }else if(n > 40 && n < 51){
            fSize = 'textSize5';
            diameter = 150;
        }else if(n > 50 && n < 61){
            fSize = 'textSize6';
            diameter = 175;
        }else if(n > 60 && n < 71){
            fSize = 'textSize7';
            diameter = 200;
        }else if(n > 70 && n < 81){
            fSize = 'textSize8';
            diameter = 225;
        }else if(n > 80 && n < 91){
            fSize = 'textSize9';
            diameter = 250;
        }else{
            fSize = 'bigText';
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

        html ='<div data-techTerm="'+ bubbleItem.termID +'Tech" class="circle '+ bubbleItem.termID +'Tech '+ fSize +' style="width:' + diameter + 'px; height:' + diameter + 'px">';
        html = html + '<div class="circle-content '+ clColor +'" style="width:' + diameter + 'px; height:' + diameter + 'px">';
        html = html + '<span><strong>'+ bubbleItem.count +'</strong>'+ bubbleItem.termName +'</span></div></div>';
        $('.bubble-chart-container').append(html);
    });
});

app.controller("bubble-ctrl", function($scope, $http) {
    var jsonPath ="",
        mT = "70px",
        mL = "-85px",
        dT = "80px",
        dL = "-50px",
        lPosition = "",
        tPosition = "",
        mSize = "220px",
        dSize = "340px",
        wActive = "",
        hActive = "";

    var _isNotMobile = (function() {
        var check = false;
        (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4)))check = true})(navigator.userAgent||navigator.vendor||window.opera);
        return !check;
    })();
    if(_isNotMobile){
        jsonPath = "data/dBubble-position.json";
        lPosition = dL;
        tPosition = dT;
        wActive = dSize;
        hActive = dSize;
    }else{
        jsonPath = "data/mBubble-position.json";
        lPosition = mL;
        tPosition = mT;
        wActive = mSize;
        hActive = mSize;
    }
    $http.get(jsonPath).
    success(function(data, status, headers, config) {
        $scope.bubblePosition = data;

        var n = $scope.bubblePosition.length,
            nameTech = new Array(),
            java_locations = new Array(),
            dotnet_locations = new Array(),
            php_locations = new Array(),
            pm_locations = new Array(),
            qc_locations = new Array(),
            ruby_locations = new Array(),
            python_locations = new Array(),
            ba_locations = new Array(),
            dba_locations = new Array();

        for (var i = 0; i < n; i++){
            nameTech[i] = $scope.bubblePosition[i].name;

            for( var j =0; j < n; j++){
                var t = $scope.bubblePosition[i].data[j].top,
                    l = $scope.bubblePosition[i].data[j].left;
                
                if (nameTech[i] == 'java_locations'){
                    java_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'dotnet_locations'){
                    dotnet_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'php_locations'){
                    php_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'ruby_locations'){
                    ruby_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'python_locations'){
                    python_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'pm_locations'){
                    pm_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'qc_locations'){
                    qc_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'ba_locations'){
                    ba_locations[j] = new Array(t, l);
                }
                if (nameTech[i] == 'dba_locations'){
                    dba_locations[j] = new Array(t, l);
                }
            }
        }

        $('.circle').click(function(e) {
            e.preventDefault();

            var hDotnet = $('.DOTNETTech').height(),
                wDotnet = $('.DOTNETTech').width(),

                hJava   = $('.JAVATech').height(),
                wJava   = $('.JAVATech').width(),

                hPhp = $('.PHPTech').height(),
                wPhp = $('.PHPTech').width(),

                hRuby   = $('.RUBYTech').height(),
                wRuby   = $('.RUBYTech').width(),

                hPython = $('.PYTHONTech').height(),
                wPython = $('.PYTHONTech').width(),

                hQc   = $('.QATech').height(),
                wQc   = $('.QATech').width(),

                hPm = $('.PROJECT_MANAGERTech').height(),
                wPm = $('.PROJECT_MANAGERTech').width(),

                hDba   = $('.DBATech').height(),
                wDba   = $('.DBATech').width(),

                hBa = $('.BATech').height(),
                wBa = $('.BATech').width();

            var circle = $(this);
            var circle_id = $(this).attr('data-techTerm');

            if (!circle.hasClass('active')) {
                $(this).children(':not(".circle-content")').hide();

                $('.inactive span, .inactive .intro').hide(); // hides the intro on the main circle before it is animated away

                // move the new circle and increase size to center
                $('.' + circle_id).addClass('active').removeClass('small').animate({
                    'top': tPosition,
                    'left': lPosition
                }).children('.circle-content').animate({
                    'width': wActive,
                    'height': hActive
                }, {
                    duration: '4000',
                    easing: 'easeOutQuad'
                });

                // add the title and content once the circle has resized itself
                setTimeout((function() {
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
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'left': php_locations[0][1],
                        'top': php_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'left': dotnet_locations[0][1],
                        'top': dotnet_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.RUBYTech').animate({
                        'left': ruby_locations[0][1],
                        'top': ruby_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.PYTHONTech').animate({
                        'left': python_locations[0][1],
                        'top': python_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'left': dba_locations[0][1],
                        'top': dba_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'left': ba_locations[0][1],
                        'top': ba_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'left': qc_locations[0][1],
                        'top': qc_locations[0][0]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height':hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[1][0],
                        'left': php_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[1][0],
                        'left': java_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.RUBYTech').animate({
                        'top': ruby_locations[1][0],  
                        'left': ruby_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.PYTHONTech').animate({
                        'top': python_locations[1][0],
                        'left': python_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[1][0],
                        'left': dba_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[1][0],
                        'left': ba_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[1][0],
                        'left': qc_locations[1][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height':hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[2][0],
                        'left': java_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[2][0],
                        'left': dotnet_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.RUBYTech').animate({
                        'top': ruby_locations[2][0],
                        'left': ruby_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.PYTHONTech').animate({
                        'top': python_locations[2][0],
                        'left': python_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[2][0],
                        'left': dba_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[2][0],
                        'left': ba_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[2][0],
                        'left': qc_locations[2][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height':hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[3][0],
                        'left': java_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[3][0],
                        'left': dotnet_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[3][0],
                        'left': php_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.PYTHONTech').animate({
                        'top': python_locations[3][0],
                        'left': python_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[3][0],
                        'left': dba_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[3][0],
                        'left': ba_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[3][0],
                        'left': qc_locations[3][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height':hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[4][0],
                        'left': java_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[4][0],
                        'left': dotnet_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[4][0],
                        'left': php_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.RUBYTech').animate({
                        'top': ruby_locations[4][0],
                        'left': ruby_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[4][0],
                        'left': dba_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[4][0],
                        'left': ba_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[4][0],
                        'left': qc_locations[4][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height':hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[5][0],
                        'left': java_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[5][0],
                        'left': dotnet_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[5][0],
                        'left': php_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                       'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.RUBYTech').animate({
                        'top': ruby_locations[5][0],
                        'left': ruby_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[5][0],
                        'left': dba_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[5][0],
                        'left': ba_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[5][0],
                        'left': qc_locations[5][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height':hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[6][0],
                        'left': java_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[6][0],
                        'left': dotnet_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[6][0],
                        'left': php_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.RUBYTech').animate({
                        'top': ruby_locations[6][0],
                        'left': ruby_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[6][0],
                        'left': dba_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[6][0],
                        'left': ba_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PROJECT_MANAGERTech').animate({
                        'top': pm_locations[6][0],
                        'left': pm_locations[6][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[7][0],
                        'left': java_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[7][0],
                        'left': dotnet_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[7][0],
                        'left': php_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.RUBYTech').animate({
                        'top': ruby_locations[7][0],
                        'left': ruby_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[7][0],
                        'left': qc_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height': hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.BATech').animate({
                        'top': ba_locations[7][0],
                        'left': ba_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wBa,
                        'height': hBa
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PROJECT_MANAGERTech').animate({
                        'top': pm_locations[7][0],
                        'left': pm_locations[7][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
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
                        'width': wPython,
                        'height': hPython
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.JAVATech').animate({
                        'top': java_locations[8][0],
                        'left': java_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wJava,
                        'height': hJava
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DOTNETTech').animate({
                        'top': dotnet_locations[8][0],
                        'left': dotnet_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDotnet,
                        'height': hDotnet
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PHPTech').animate({
                        'top': php_locations[8][0],
                        'left': php_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPhp,
                        'height': hPhp
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });


                    $('.RUBYTech').animate({
                        'top': ruby_locations[8][0],
                        'left': ruby_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wRuby,
                        'height': hRuby
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.QATech').animate({
                        'top': qc_locations[8][0],
                        'left': qc_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wQc,
                        'height': hQc
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.DBATech').animate({
                        'top': dba_locations[8][0],
                        'left': dba_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wDba,
                        'height': hDba
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });

                    $('.PROJECT_MANAGERTech').animate({
                        'top': pm_locations[8][0],
                        'left': pm_locations[8][1]
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    }).addClass('small').removeClass('active').children('.circle-content').animate({
                        'width': wPm,
                        'height': hPm
                    }, {
                        duration: '4000',
                        easing: 'easeOutQuad'
                    });
                } // end if circle id = Business Analytics

                $('.small .intro').hide();


                //remove the inactive class and slide the title back in
                setTimeout((function() {
                    $('#box div.inactive').removeClass('inactive').children('span').fadeIn('fast');
                }), 300);

            } // end if($(this).hasClass('active'))

        });
    }).
    error(function(data, status, headers, config) {
        // log error
    });
});