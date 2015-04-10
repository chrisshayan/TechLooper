angular.module("Common").factory("utils", function (jsonValue, $location, $rootScope) {
  var techlooperObserver = $.microObserver.get("techlooper");

  var instance = {
    hasNonAsciiChar: function(str) {
      var chars = str.split("");
      var rs = false;
      $.each(chars, function(i, c) {
        rs = rs || (c < "0");
        rs = rs || (c > "9" && c < "A");
        rs = rs || (c > "Z" && c < "a");
        rs = rs || (c > "z");
        return !rs;
      });
      return rs;
    },

    toAscii: function (str) {//Công ty Cổ phần Licogi 16.6
      str = str.toLowerCase();
      str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
      str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
      str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
      str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
      str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
      str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
      str = str.replace(/đ/g, "d");
      str = str.replace(/!|@|\$|%|\^|\*|\(|\)|\+|\=|\<|\>|\?|\/|,|\.|\:|\'| |\"|\&|\#|\[|\]|~/g, "-");
      str = str.replace(/-+-/g, "-"); //change "--" to "-"
      str = str.replace(/^\-+|\-+$/g, "");//trim "-"
      return str;
    },

    isHome: function () {
      var home = "/";
      if (window.location.host.indexOf("hiring") >= 0) {
        home = "/home";
      }
      else {
        home = "/pie-chart";
      }
      return $location.path() === home;
    },

    apply: function () {
      if (!$rootScope.$$phase) {
        $rootScope.$apply();
      }
    },

    go2SkillAnalyticPage: function (scope, term) {
      var path = jsonValue.routerUris.analyticsSkill + '/' + term;
      $location.path(path);
      instance.apply();
    },

    getDatePeriods: function (number, period) {
      switch (period) {
        case "month":
          return (number).months();
        case "quarter":
          return (number * 3).months();
        case "sixMonths":
          return (number * 6).months();
        case "oneYear":
          return (number * 12).months();
      }
      return (number).weeks();
    },

    formatDateByPeriod: function (date, period) {
      //TODO : It should be able to autodectect period base on the value in json.val.js
      if (period === "oneYear") {
        return date.toString("MMM yyyy");
      }
      return date.toString("MMM dd");
    },

    getHistogramPeriod: function (histogram) {
      switch (histogram) {
        case jsonValue.histograms.sixBlocksOfFiveDays:
          return 5;
        case jsonValue.histograms.sixBlocksOfFifteenDays:
          return 15;
        case jsonValue.histograms.oneMonth:
          return 1;
        case jsonValue.histograms.eighteenBlocksOfFiveDays:
          return 5;
        case jsonValue.histograms.sixMonths:
          return 10;
        case jsonValue.histograms.oneYear:
          return 31;
      }
      return 1;
    },

    getNonZeroItems: function (items, props) {
      return $.grep(items.slice(0), function (item, i) {
        var zeroCounter = 0;
        $.each(props, function (j, prop) {
          item[prop] === 0 && (++zeroCounter);
        });
        return zeroCounter !== props.length;
      });
    },

    flatMap: function (items, ats, tos) {
      var clone = items.slice(0);
      $.each(clone, function (index, item) {
        $.each(ats, function (index, at) {
          item[tos[index]] = jsonPath.eval(item, at)[0];
        });
      });
      return clone;
    },

    getTopItems: function (array, props, n) {
      var clone = array.slice(0);
      clone.sort(function (x, y) {
        var dx = 0, dy = 0;
        $.each(props, function (i, p) {
          dx = Math.abs(dx - x[p]);
          dy = Math.abs(dy - y[p]);
        });
        return dy - dx;
      });
      return clone.slice(0, n || 1);
    },

    //TODO allow auto scan observable objects
    registerNotification: function (notify, handler, able) {
      return techlooperObserver.on(notify, handler, able);
    },

    sendNotification: function () {
      return techlooperObserver.send.apply(techlooperObserver, arguments);
    },

    getView: function ($path) {
      var path = ($path === undefined) ? $location.path() : $path;
      if (/\/jobs\/search\//i.test(path)) {
        return jsonValue.views.jobsSearchText;
      }
      else if (/\/jobs\/search/i.test(path)) {
        return jsonValue.views.jobsSearch;
      }
      else if (/\/bubble-chart/.test(path)) {
        return jsonValue.views.bubbleChart;
      }
      else if (/\/pie-chart/.test(path)) {
        return jsonValue.views.pieChart;
      }
      else if (/\/analytics\/skill/.test(path)) {
        return jsonValue.views.analyticsSkill;
      }
      else if (/\/signin/.test(path)) {
        return jsonValue.views.signIn;
      }
      else if (/\/register/.test(path)) {
        return jsonValue.views.register;
      }
      else if (/\/home/.test(path)) {
        return jsonValue.views.home;
      }
      else if (/\/talent-search-result/.test(path)) {
        return jsonValue.views.talentSearchResult;
      }
      else if (/\/talent-profile/.test(path)) {
        return jsonValue.views.talentProfile;
      }
      else if (/\/landing/.test(path)) {
        return jsonValue.views.landing;
      }
      else if (/\/companies\//i.test(path)) {
        return jsonValue.views.companyProfile;
      }
    },

    sum: function (array, prop) {
      var total = 0;
      $.each(array, function (index, value) {
        total += value[prop];
      });
      return total;
    },

    isMobile: function () {
      var isMobile = false;
      (function (a) {
        if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))) isMobile = true
      })(navigator.userAgent || navigator.vendor || window.opera);
      return isMobile;
    },

    setIds: function (array) {
      $.each(array, function (index, value) {
        value.id = index;
      })
      return array;
    },

    findBy: function (array, attr, value) {
      var val = undefined;
      $.each(array, function (index, item) {
        if (item[attr].toLowerCase() === value.toLowerCase()) {
          val = item;
          return false;
        }
      });
      return val;
    },

    //openOathDialog: function (auth, successUrl) {
    //  if (auth.isNotSupported) {return alert("Sign-in by " + auth.provider.toUpperCase() + " isn't supported");}
    //  instance.sendNotification(jsonValue.notifications.loading, $(".signin-page").height());
    //  $auth.authenticate(auth.provider)
    //    .then(function (resp) {//success
    //      delete $window.localStorage["satellizer_token"];
    //      localStorageService.set(jsonValue.storage.key, resp.data.key);
    //      $http.post("login", $.param({key: resp.data.key}), {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}});
    //      $location.path(successUrl);
    //    })
    //    .catch(function (resp) {
    //      instance.sendNotification(jsonValue.notifications.loaded);
    //    });
    //},
    //closeAlert: function(){
    //  $('.messager-block').find('.close').click(function(){
    //    $(this).parent().hide();
    //  });
    //},
    notify: function (msg, type) {
      var bootstrapClass = "alert-danger";
      switch (type) {
        case "error":
          bootstrapClass = "alert-danger";
          break;
        case "success":
          bootstrapClass = "alert-success";
          break;
      }

      $('.messager-block').addClass(bootstrapClass).find('p').text(msg);
      $('.messager-block').show();
      $('.messager-block').find('.close').click(function () {
        $(this).parent().hide();
      });
      setTimeout(function () {
        $(".messager-block").fadeOut(1000);
      }, 4000);
    },

    closeNotify: function () {
      $(".messager-block").fadeOut(1000);
    }
  }

  //instance.registerNotification(jsonValue.notifications.changeUrl, instance.closeNotify)

  return instance;
});