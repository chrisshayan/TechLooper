techlooper.controller("createEventController", function ($scope, $translate, jsonValue, apiService, $rootScope) {

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
      placeholder: "attendantsEx",
      showCurrentUserEmail: true
    },

    fromNowDatetimeConfig: {
      step: 10,
      minDate: new Date()
    }
  }

  //$scope.webinar = {
  //  attendees: [$rootScope.userInfo.email]
  //}

  $scope.state = function (type) {
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
        return false;

      case "error-event-start-date":
        if (!$scope.webinar) return false;
        if ($scope.webinar.startDate && $scope.webinar.endDate) {
          var startDate = moment($scope.webinar.startDate, jsonValue.dateTimeFormat);
          var endDate = moment($scope.webinar.endDate, jsonValue.dateTimeFormat);
          var before = endDate.isBefore(startDate);
          $scope.webinarForm.startDate.$setValidity("range", !before);
          $scope.webinarForm.endDate.$setValidity("range", !before);
          return before;
        }
        $scope.webinarForm.startDate.$setValidity("range", true);
        $scope.webinarForm.endDate.$setValidity("range", true);
        return false;
    }

    return false;
  }

});