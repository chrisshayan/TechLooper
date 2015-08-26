techlooper.controller("eventDetailsController", function ($scope, apiService, $routeParams) {


  var parts = $routeParams.id.split("-");
  parts.pop();
  var webinarId = parts.pop();

  apiService.findWebinarById(webinarId)
    .success(function(webinar) {
      console.log(webinar);
      $scope.webinar = webinar;

    });
});

