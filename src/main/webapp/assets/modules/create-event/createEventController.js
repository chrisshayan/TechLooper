techlooper.controller("createEventController", function ($scope, $translate, jsonValue, apiService, $rootScope) {

  var placeholder = $translate.instant('whoJoinAndWhyEx');
  $('#txtWhyEvent').val(placeholder);
  $('#txtWhyEvent').focus(function () {
    if ($(this).val() === placeholder) {
      $(this).val('');
      $(this).removeClass('change-color');
    }
  });

  $('#txtWhyEvent').blur(function () {
    if ($(this).val() === '') {
      $(this).val(placeholder);
      $(this).addClass('change-color');
    }
  });

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
        break;
    }

    return false;
  }


});