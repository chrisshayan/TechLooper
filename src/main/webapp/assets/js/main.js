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
])

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
])

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
])

app.controller("loadCompanies", function($scope, $http) {
    $http.get('data/companies.json').
    success(function(data, status, headers, config) {
        $scope.companies = data;
    }).
    error(function(data, status, headers, config) {
        // log error
    });
});

app.controller("loadTech", function($scope, $http) {
    var socket = new SockJS('ws');
        stompClient = Stomp.over(socket),
        totalJobs = "",
        jan=0, 
        jaPercent=0,

        don=0,
        doPercent=0,  

        phn=0, 
        phPercent =0, 

        pmn =0, 
        pmPercent=0, 

        pyn=0, 
        pyPercent=0,

        rbn=0, 
        rbPercent=0, 

        qan=0,
        qaPercent=0, 

        dbn=0,
        dbPercent=0, 

        qan=0,
        qaPercent=0, 

        ban=0,
        baPercent=0;

        stompClient.debug = function(){};

    $http.get('data/techlist.json').
    success(function(data, status, headers, config) {
        stompClient.connect({}, function(frame) {
           
            stompClient.subscribe('/topic/technical-job/total', function(techName){
                totalJobs =JSON.parse(techName.body).count;
            });
            stompClient.subscribe('/topic/technical-job/java', function(techName){
                jan = JSON.parse(techName.body).count;
                jaPercent = parseInt(jan)*100/parseInt(totalJobs);

                $('#javaTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/dotnet', function(techName){
                don = JSON.parse(techName.body).count;
                doPercent = parseInt(don)*100/parseInt(totalJobs);

                $('#dotnetTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/php', function(techName){
                phn = JSON.parse(techName.body).count;
                phPercent = parseInt(phn)*100/parseInt(totalJobs);

                $('#phpTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/python', function(techName){
                pyn = JSON.parse(techName.body).count;
                pyPercent = parseInt(pyn)*100/parseInt(totalJobs);

                $('#pythonTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/ruby', function(techName){
                rbn = JSON.parse(techName.body).count;
                rbPercent = parseInt(rbn)*100/parseInt(totalJobs);

                $('#rubyTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/project_manager', function(techName){
                pmn = JSON.parse(techName.body).count;
                pmPercent = parseInt(pmn)*100/parseInt(totalJobs);

                $('#pmTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/qa', function(techName){
                qan = JSON.parse(techName.body).count;
                qaPercent = parseInt(qan)*100/parseInt(totalJobs);

                $('#qcTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/dba', function(techName){
                dbn = JSON.parse(techName.body).count;
                dbPercent = parseInt(dbn)*100/parseInt(totalJobs);

                $('#dbaTech').find('strong').text(JSON.parse(techName.body).count);
            });
            stompClient.subscribe('/topic/technical-job/ba', function(techName){
                ban = JSON.parse(techName.body).count;
                baPercent = parseInt(ban)*100/parseInt(totalJobs);

                $('#baTech').find('strong').text(JSON.parse(techName.body).count);
            });
        });
        $scope.techlist = data;
        // console.log(jaPercent)
        // if(jaPercent > 0 && jaPercent < 11){
        //     console.log(10);
        // }
        // else if(jaPercent > 10 && jaPercent <21){
        //     console.log(20);
        // }
        // else if(jaPercent > 20 && jaPercent <31){
        //     console.log(30);
        // }
        // else if(jaPercent > 30 && jaPercent <41){
        //     console.log(40);
        // }
        // else if(jaPercent > 40 && jaPercent <51){
        //     console.log(50);
        // }
        // else if(jaPercent > 50 && jaPercent <61){
        //     console.log(60);
        // }
        // else if(jaPercent > 60 && jaPercent <71){
        //     console.log(70);
        // }
        // else if(jaPercent > 70 && jaPercent <81){
        //     console.log(80);
        // }
        // else if(jaPercent > 80 && jaPercent <91){
        //     console.log(90);
        // }
        // else{
        //     console.log(100);
        // }

    }).
    error(function(data, status, headers, config) {
        console.logError("Error in Loading techlist.json", status);
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

            var hDotnet = $('#dotnetTech').height(),
                wDotnet = $('#dotnetTech').width(),

                hJava   = $('#javaTech').height(),
                wJava   = $('#javaTech').width(),

                hPhp = $('#phpTech').height(),
                wPhp = $('#phpTech').width(),

                hRuby   = $('#rubyTech').height(),
                wRuby   = $('#rubyTech').width(),

                hPython = $('#pythonTech').height(),
                wPython = $('#pythonTech').width(),

                hQc   = $('#qcTech').height(),
                wQc   = $('#qcTech').width(),

                hPm = $('#pmTech').height(),
                wPm = $('#pmTech').width(),

                hDba   = $('#dbaTech').height(),
                wDba   = $('#dbaTech').width(),

                hBa = $('#baTech').height(),
                wBa = $('#baTech').width();

            var circle = $(this);
            var circle_id = this.id;

            if (!circle.hasClass('active')) {
                $(this).children(':not(".circle-content")').hide();

                $('.inactive span, .inactive .intro').hide(); // hides the intro on the main circle before it is animated away

                // move the new circle and increase size to center
                $('#' + circle_id).addClass('active').removeClass('small').animate({
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
                    $('#' + circle_id).children(':not(".circle-content")').slideDown('fast');
                }), 500);

                /*
                 * 1 Java is active
                 **/
                if (circle_id == 'javaTech') {

                    $('#pmTech').animate({
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

                    $('#phpTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#rubyTech').animate({
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


                    $('#pythonTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#qcTech').animate({
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
                if (circle_id == 'dotnetTech') {

                    $('#pmTech').animate({
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

                    $('#phpTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#rubyTech').animate({
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


                    $('#pythonTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#qcTech').animate({
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
                if (circle_id == 'phpTech') {

                    $('#pmTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#rubyTech').animate({
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


                    $('#pythonTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#qcTech').animate({
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
                if (circle_id == 'rubyTech') {

                    $('#pmTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#phpTech').animate({
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


                    $('#pythonTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#qcTech').animate({
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
                if (circle_id == 'pythonTech') {

                    $('#pmTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#phpTech').animate({
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


                    $('#rubyTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#qcTech').animate({
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
                if (circle_id == 'pmTech') {

                    $('#pythonTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#phpTech').animate({
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


                    $('#rubyTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#qcTech').animate({
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
                if (circle_id == 'qcTech') {

                    $('#pythonTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#phpTech').animate({
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


                    $('#rubyTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#baTech').animate({
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

                    $('#pmTech').animate({
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
                if (circle_id == 'dbaTech') {

                    $('#pythonTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#phpTech').animate({
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


                    $('#rubyTech').animate({
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

                    $('#qcTech').animate({
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

                    $('#baTech').animate({
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

                    $('#pmTech').animate({
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
                if (circle_id == 'baTech') {

                    $('#pythonTech').animate({
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

                    $('#javaTech').animate({
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

                    $('#dotnetTech').animate({
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

                    $('#phpTech').animate({
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


                    $('#rubyTech').animate({
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

                    $('#qcTech').animate({
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

                    $('#dbaTech').animate({
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

                    $('#pmTech').animate({
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