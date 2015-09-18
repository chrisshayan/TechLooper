techlooper.controller("eventsController", function ($scope, apiService, utils, jsonValue, $timeout) {

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  var today = moment().format('YYYY MM DD');
  apiService.findAvailableWebinars()
    .success(function (webinars) {//group by startDate
      var group = [];
      var lastVisitWebinars = [];
      for (var i = 0; i < webinars.length; i++) {
        if ($.inArray(webinars[i], lastVisitWebinars) >= 0) continue;
        var startDate = moment(webinars[i].startDate, jsonValue.dateTimeFormat);
        if (!startDate.isValid()) continue;
        var web = {startDate: startDate.format(jsonValue.dateFormat)};
        web.expired = moment(web.startDate, jsonValue.dateTimeFormat).isBefore(today, "day");
        if(web.expired == true){
          group.now = true;
        }
        group.push(web);
        lastVisitWebinars = [];
        for (var j = i; j < webinars.length; j++) {
          if ($.inArray(webinars[j], lastVisitWebinars) >= 0) continue;
          if (startDate.isSame(moment(webinars[j].startDate, jsonValue.dateTimeFormat), "day")) {
            lastVisitWebinars.push(webinars[j]);
          }
        }
        web.webinars = lastVisitWebinars;
      }
      $scope.webinarsGroup = group;
    })
    .finally(function () {
      utils.sendNotification(jsonValue.notifications.loaded);
    });
  $timeout(function () {
    $('html,body').animate({
      scrollTop: $(".event-timeline").offset().top - 200
    }, 1500);
  }, 2500);
});