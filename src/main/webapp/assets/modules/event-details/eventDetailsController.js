techlooper.controller("eventDetailsController", function ($scope, apiService, $routeParams, localStorageService) {


  var parts = $routeParams.id.split("-");
  parts.pop();
  var webinarId = parts.pop();

  apiService.findWebinarById(webinarId)
    .success(function(webinar) {
      $scope.webinar = webinar;
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

