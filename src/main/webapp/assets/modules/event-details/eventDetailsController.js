techlooper.controller("eventDetailsController", function ($scope, apiService, $routeParams, localStorageService,
                                                          vnwConfigService, utils, jsonValue, $translate) {
  var parts = $routeParams.id.split("-");
  parts.pop();
  var webinarId = parts.pop();

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  apiService.findWebinarById(webinarId)
    .success(function(webinar) {
      $scope.webinar = webinar;
        if($scope.webinar.company){
          $scope.webinar.company.companySize = vnwConfigService.getCompanySizeText($scope.webinar.company.companySizeId);
        }
        //var today = new Date();
        $scope.webinar.expired = moment($scope.webinar.startDate).isBefore(moment());
    }).finally(function () {
      utils.sendNotification(jsonValue.notifications.loaded);
    });

  $scope.joinNow = function() {
    localStorageService.set("joinAnything", "webinar");
    localStorageService.set("id", webinarId);
    apiService.joinNowByFB();
  }

  $scope.$on("joinAnythingSuccess", function(fromScope, webinar) {
    $scope.webinar = webinar;

    //join success
  });

  $scope.status = function(type) {
    switch (type) {
      case "not-joined":
        if (!$scope.webinar) return true;
        var email = localStorageService.get("email");
        var attendee = utils.findBy($scope.webinar.attendees, "email", email);
        return attendee;
    }
    return false;
  }

  $scope.showPostSuccessfulMessage = localStorageService.get("webinarCreated");

  localStorageService.remove("webinarCreated");

  $scope.fbShare = function () {
    ga("send", {
      hitType: "event",
      eventCategory: "facebookshare",
      eventAction: "click",
      eventLabel: "webinarDetails"
    });
    utils.openFBShare("/shareWebinar/" + $translate.use() + "/" + webinarId);
  }

});

