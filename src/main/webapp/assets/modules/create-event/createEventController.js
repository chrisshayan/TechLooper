techlooper.controller("createEventController", function ($scope, $translate, jsonValue, apiService) {

  $scope.createWebinar = function () {
    $scope.webinarForm.$setSubmitted();
    if ($scope.webinarForm.$invalid) {
      return;
    }

    apiService.createWebinar($scope.webinar);

    $scope.webinarForm.$setPristine();
  }

  $scope.uiConfig = {
    attendantsConfig: {
      type: "email",
      placeholder: "attendantsEx"
    },

    fromNowDatetimeConfig: {
      step:10,
      minDate: new Date()
    }
  }

  $scope.state = function(type) {
    switch (type) {
      case "error-event-date":
        return $scope.webinarForm.$submitted || $scope.webinarForm.startDate.$dirty || $scope.webinarForm.endDate.$dirty;

      case "error-required-event-date":
        if ($scope.webinarForm.startDate.$error.required) {
          return true;
        }
        if (!$scope.webinarForm.endDate.$dirty) return false;
        if ($scope.webinarForm.endDate.$error.required) {
          return true;
        }
        break;
    }

    return false;
  }

});