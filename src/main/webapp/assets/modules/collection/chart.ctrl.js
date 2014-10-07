angular.module('Chart').controller('chartController', ["$scope", "jsonValue", function ($scope, jsonValue) {
    var socket = new SockJS('ws');
    stompClient = Stomp.over(socket),
    currentTerms = new Array,
    totalTerms = 0,
    totalJobs = 0,
    rColor = 0,
    mT = "70px",
    mL = "-85px",
    dT = "80px",
    dL = "-50px",
    lPosition = "",
    tPosition = "",
    mSize = "220px",
    dSize = "340px",
    sActive = "";

    var _isNotMobile = (function () {
        var check = false;
        (function (a) {
            if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) check = true
        })(navigator.userAgent || navigator.vendor || window.opera);
        return !check;
    })();
    stompClient.debug = function () {};
    $scope.$on('terms-coming', function (event, terms) {
        $.each(terms, function (index, value) {
            $scope.$emit('term', value);
        });
    });
    $scope.$on('term', function (event, data) {
        stompClient.subscribe('/topic/technical-job/' + data.term, function (response) {
            rColor = rColor + 1;
            if (rColor == 9) {
                rColor = 0;
            }
            $.each(currentTerms, function (index, value) {
                if (data.term == value.term && data.count != value.count) {
                    $('.' + value.term).remove();
                    $scope.$emit('draw-bubble', {
                        'colorID': rColor,
                        'count': data.count,
                        'termName': data.name,
                        'termID': data.term
                    });
                }
            });
        });
    });

    stompClient.connect({}, function (frame) {
        var abc = stompClient.subscribe('/topic/technical-job/terms', function (response) {
            currentTerms = JSON.parse(response.body);
            $scope.termList = JSON.parse(response.body);
            $.each($scope.termList, function (index, value) {
                totalJobs += value.count;
            });
            $.each($scope.termList, function (index, value) {
                rColor = rColor + 1;
                if (rColor == 9) {
                    rColor = 0;
                }
                $scope.$emit('draw-bubble', {
                    'colorID': rColor,
                    'count': value.count,
                    'termName': value.name,
                    'termID': value.term
                });
            });
            $scope.$emit('bubble-ctrl');
            $scope.$emit('first-position', $scope.termList);
            $scope.$emit('terms-coming', $scope.termList);
            abc.unsubscribe();
        });
        stompClient.send("/app/technical-job/terms");
    });

    $scope.$on('draw-bubble', function (event, bubbleItem) {
        var html = '',
            clColor = '',
            fSize = '',
            diameterPlus = 0;

        if (_isNotMobile) {
            diameterPlus = 25;
        } else {
            diameterPlus = -10;
        }
        var n = Math.round(parseInt(bubbleItem.count) * 100 / parseInt(totalJobs)),
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

        html = '<div data-techTerm="' + bubbleItem.termID + 'Tech" class="circle ' + bubbleItem.termID + 'Tech ' + fSize + '" style="width:' + diameter + 'px; height:' + diameter + 'px">';
        html = html + '<div class="circle-content ' + clColor + '" style="width:' + diameter + 'px; height:' + diameter + 'px">';
        html = html + '<span><strong>' + bubbleItem.count + '</strong>' + bubbleItem.termName + '</span></div></div>';
        $('.bubble-chart-container').append(html);
    });

    $scope.$on('bubble-ctrl', function (event, data) {
        var jsonPath = "";

        if (_isNotMobile) {
            jsonPath = "data/dBubble-position.json";
            lPosition = dL;
            tPosition = dT;
            sActive = dSize;
        } else {
            jsonPath = "data/mBubble-position.json";
            lPosition = mL;
            tPosition = mT;
            sActive = mSize;
        }


        $scope.bubblePosition = _isNotMobile ? jsonValue.dBubblePosition : jsonValue.mBubblePosition;
        var n = $scope.bubblePosition.length,
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
            nameTech[i] = $scope.bubblePosition[i].name;
            for (var j = 0; j < n; j++) {
                var t = $scope.bubblePosition[i].data[j].top,
                    l = $scope.bubblePosition[i].data[j].left;

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
                    });
                } // end if circle id = Business Analytics

                $('.small .intro').hide();


                //remove the inactive class and slide the title back in
                setTimeout((function () {
                    $('#box div.inactive').removeClass('inactive').children('span').fadeIn('fast');
                }), 300);

            } // end if($(this).hasClass('active'))

        });

    });

    $scope.$on('first-position', function (event, bubbleItem) {
        var biggestDiameter = 0,
            biggestName = "",
            count = 0,
            jsonPath = "",
            locations = new Array(),
            sBubble = 0,
            resize = 0;

        if (_isNotMobile) {
            jsonPath = "data/dposition-default.json";
            sActive = dSize;
            lPosition = dL;
            tPosition = dT;
            resize = 25;
        } else {
            jsonPath = "data/mposition-default.json";
            sActive = mSize;
            lPosition = mL;
            tPosition = mT;
            resize = -10;
        }

        $scope.bubblePosition = _isNotMobile ? jsonValue.dPositionDefault : jsonValue.mPositionDefault
        for (var i = 0; i < 9; i++) {
            var t = $scope.bubblePosition[0].data[i].top,
                l = $scope.bubblePosition[0].data[i].left;
            locations[i] = new Array(t, l);
        }
        for (var i = 0; i < bubbleItem.length; i++) {
            if (bubbleItem[i].count > biggestDiameter) {
                biggestDiameter = bubbleItem[i].count;
                biggestName = bubbleItem[i].term + 'Tech';
            }
        }

        for (var i = 0; i < bubbleItem.length; i++) {
            if (bubbleItem[i].count < biggestDiameter) {
                var n = Math.round(parseInt(bubbleItem[i].count) * 100 / parseInt(totalJobs)),
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
                $('.' + bubbleItem[i].term + 'Tech').css({
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
        });
    });

}]);