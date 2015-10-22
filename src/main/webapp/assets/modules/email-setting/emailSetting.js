techlooper.controller("emailSetting", function ($scope, utils, jsonValue, apiService, validatorService) {
  utils.sendNotification(jsonValue.notifications.switchScope);
  $scope.status = function (type) {
    switch (type) {
      case "already-save":
        return $scope.saved;
      case "already-fail":
        return $scope.fail;
    }
  };
  $('.email-setting-form').find('[data-toggle="tooltip"]').tooltip({
    html: true,
    placement: 'right',
    'trigger': "focus",
    animation: true
  });
  apiService.loadEmailSetting()
    .success(function (data) {
      $scope.replyEmail = data.replyEmail;
      $scope.emailSignature = data.emailSignature;
    }).finally(function(){
      utils.sendNotification(jsonValue.notifications.loaded);
    });

  $scope.saveEmailSetting = function () {
    $scope.saved = false;
    $scope.fail = false;
    $scope.emailSetting.$setSubmitted();
    if($scope.emailSetting.$invalid) {
      return;
    }
    utils.sendNotification(jsonValue.notifications.switchScope);
    var emailSetting = {
      replyEmail: $scope.replyEmail,
      emailSignature: $scope.emailSignature
    };
    apiService.saveEmailSetting(emailSetting)
      .success(function () {
          $scope.saved = true;
      })
      .error(function () {
          $scope.fail = true;
      }).finally(function(){
          utils.sendNotification(jsonValue.notifications.loaded);
      });
  };

  $scope.cancleEmailSetting = function () {
    apiService.loadEmailSetting()
      .success(function (data) {
        $scope.replyEmail = data.replyEmail;
        $scope.emailSignature = data.emailSignature;
      })
  };

});