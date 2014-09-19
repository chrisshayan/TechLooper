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
    $http.get('data/techlist.json').
    success(function(data, status, headers, config) {
        $scope.techlist = data;
    }).
    error(function(data, status, headers, config) {
        // log error
    });
});

app.controller("setPercent", function($scope, $http) {});

app.controller('bubble-ctrl', ['$scope',
    function($scope) {

        var dotnet_locations = new Array();
        //top, left
        dotnet_locations[0] = new Array('370px', '110px'); // java
        dotnet_locations[1] = new Array('100px', '250px'); // .net
        dotnet_locations[2] = new Array('-30px', '-65px'); // php
        dotnet_locations[3] = new Array('330px', '200px'); // ruby
        dotnet_locations[4] = new Array('-10px', '-80px');  // python
        dotnet_locations[5] = new Array('10px', '230px');  // pm
        dotnet_locations[6] = new Array('-30px', '200px');  //qc
        dotnet_locations[7] = new Array('200px', '100px');
        dotnet_locations[8] = new Array('150px', '100px');


        var php_locations = new Array();
        //top, left
        php_locations[0] = new Array('-10px', '-220px');// java
        php_locations[1] = new Array('-30px', '250px'); // .net
        php_locations[2] = new Array('-290px', '10px'); 
        php_locations[3] = new Array('180px', '280px'); // ruby
        php_locations[4] = new Array('280px', '-190px');  // python
        php_locations[5] = new Array('65px', '-260px');  //pm
        php_locations[6] = new Array('180px', '280px'); //Qc
        php_locations[7] = new Array('220px', '-220px'); //DBA
        php_locations[8] = new Array('175px', '-240px');  //BA


        var java_locations = new Array();
        //top, left
        java_locations[0] = new Array('25px', '50px');
        java_locations[1] = new Array('290px', '-100px');// .net
        java_locations[2] = new Array('25px', '330px');   // php
        java_locations[3] = new Array('290px', '-100px'); // ruby
        java_locations[4] = new Array('180px', '-150px');  // python
        java_locations[5] = new Array('250px', '-140px'); // pm
        java_locations[6] = new Array('100px', '-150px');  // QC
        java_locations[7] = new Array('305px', '250px'); //DBA
        java_locations[8] = new Array('340px', '190px'); // BA

        var pm_locations = new Array();
        //top, left
        pm_locations[0] = new Array('10px', '160px'); // java
        pm_locations[1] = new Array('330px', '250px'); // .net
        pm_locations[2] = new Array('300px', '-90px'); // php
        pm_locations[3] = new Array('150px', '290px'); // ruby
        pm_locations[4] = new Array('100px', '-100px');  // python
        pm_locations[5] = new Array('-100px', '210px');  
        pm_locations[6] = new Array('380px', '60px');  //Qc
        pm_locations[7] = new Array('360px', '180px'); //DBA
        pm_locations[8] = new Array('350px', '-10px');  //BA

        var python_locations = new Array();
        //top, left
        python_locations[0] = new Array('50px', '290px'); // java
        python_locations[1] = new Array('185px', '280px'); // .net
        python_locations[2] = new Array('0', '-230px');    // php
        python_locations[3] = new Array('-60px', '-200px'); // ruby
        python_locations[4] = new Array('100px', '200px');  // python
        python_locations[5] = new Array('100px', '295px'); // pm
        python_locations[6] = new Array('240px', '-240px'); // QC
        python_locations[7] = new Array('10px', '260px'); // DBA
        python_locations[8] = new Array('130px', '290px');  //BA

        var ruby_locations = new Array();
        //top, left
        ruby_locations[0] = new Array('290px', '-150px');// java
        ruby_locations[1] = new Array('100px', '-200px'); // .net
        ruby_locations[2] = new Array('310px', '200px'); // php
        ruby_locations[3] = new Array('-50px', '50px'); // ruby
        ruby_locations[4] = new Array('-50px', '190px');  // python
        ruby_locations[5] = new Array('360px', '-90px'); //pm
        ruby_locations[6] = new Array('-45px', '-130px');  // QC
        ruby_locations[7] = new Array('85px', '-200px'); //DBA
        ruby_locations[8] = new Array('-30px', '250px');  //BA

        var ba_locations = new Array();
        //top, left
        ba_locations[0] = new Array('180px', '-160px'); // java
        ba_locations[1] = new Array('-10px', '-110px'); // .net
        ba_locations[2] = new Array('200px', '-160px'); // php
        ba_locations[3] = new Array('200px', '-150px'); // ruby
        ba_locations[4] = new Array('20px', '-200px');  // python
        ba_locations[5] = new Array('10px', '-100px');  // Pm
        ba_locations[6] = new Array('50px', '280px');  //qc
        ba_locations[7] = new Array('10px', '-100px');  //DBA
        ba_locations[8] = new Array('-100px', '150px');


        var qc_locations = new Array();
        //top, left
        qc_locations[0] = new Array('10px', '245px'); // java
        qc_locations[1] = new Array('370px', '110px'); // .net
        qc_locations[2] = new Array('-35px', '210px'); // php
        qc_locations[3] = new Array('-62px', '10px'); // ruby
        qc_locations[4] = new Array('100px', '290px');  // python
        qc_locations[5] = new Array('-60px', '-30px');  //pm
        qc_locations[6] = new Array('200px', '130px');  
        qc_locations[7] = new Array('-35px', '190px'); //DBA
        qc_locations[8] = new Array('-30px', '-150px'); //BA


        var dba_locations = new Array();
        //top, left
        dba_locations[0] = new Array('245px', '270px'); // java
        dba_locations[1] = new Array('-40px', '210px'); // .net
        dba_locations[2] = new Array('190px', '290px'); // php
        dba_locations[3] = new Array('0px', '240px'); // ruby
        dba_locations[4] = new Array('270px', '260px');  // python
        dba_locations[5] = new Array('280px', '250px');  // pm
        dba_locations[6] = new Array('330px', '210px');  //Qc
        dba_locations[7] = new Array('80px', '190px');
        dba_locations[8] = new Array('70px', '-140px'); //BA

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
                    'top': '30px',
                    'left': '-50px'
                }).children('.circle-content').animate({
                    'width': 340,
                    'height': 340
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
                } // end if circle id = .Net

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
                } // end if circle id = .Net

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
                } // end if circle id = .Net

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
                } // end if circle id = .Net

                $('.small .intro').hide();


                //remove the inactive class and slide the title back in
                setTimeout((function() {
                    $('#box div.inactive').removeClass('inactive').children('span').fadeIn('fast');
                }), 300);


            } // end if($(this).hasClass('active'))

        });
    }
])