techlooper.controller("eventDetailsController", function ($scope, apiService, $routeParams, localStorageService,
                                                          vnwConfigService, utils, jsonValue) {
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
    }).finally(function () {
      utils.sendNotification(jsonValue.notifications.loaded);
    });

  $scope.joinNow = function() {
    localStorageService.set("joinAnything", "webinar");
    localStorageService.set("id", webinarId);
    apiService.joinNowByFB();
  }

  $scope.$on("joinAnything", function(fromScope, webinar) {
    $scope.webinar = webinar;
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

  //$scope.$on("joinAnythingWithoutEmail", function() {
  //  console.log(567);
  //  console.log(arguments);
  //});
});

