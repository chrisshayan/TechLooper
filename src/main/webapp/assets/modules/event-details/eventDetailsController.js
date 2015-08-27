techlooper.controller("eventDetailsController", function ($scope, apiService, $routeParams, jsonValue, localStorageService, vnwConfigService, utils) {
  var parts = $routeParams.id.split("-");
  parts.pop();
  var webinarId = parts.pop();

  utils.sendNotification(jsonValue.notifications.loading, $(window).height());
  apiService.findWebinarById(webinarId)
    .success(function(webinar) {
      $scope.webinar = webinar;
        if($scope.webinar.company = !undefined){
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

  //$scope.$on("joinAnythingWithoutEmail", function() {
  //  console.log(567);
  //  console.log(arguments);
  //});
});

