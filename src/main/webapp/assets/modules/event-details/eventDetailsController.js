techlooper.controller("eventDetailsController", function ($scope, apiService, $routeParams, localStorageService) {


  var parts = $routeParams.id.split("-");
  parts.pop();
  var webinarId = parts.pop();

  apiService.findWebinarById(webinarId)
    .success(function(webinar) {
      $scope.webinar = webinar;
    });

  $scope.joinNow = function() {
    localStorageService.set("joinForm", "webinar");
    localStorageService.set("id", webinarId);
    apiService.joinNowByFB();
  }
});

