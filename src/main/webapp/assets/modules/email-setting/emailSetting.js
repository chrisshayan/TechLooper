techlooper.controller("emailSetting", function ($scope, apiService) {

  apiService.loadEmailSetting()
    .success(function (data) {
      $scope.replyEmail = data.replyEmail;
      $scope.emailSignature = data.emailSignature;
    })

  $scope.saveEmailSetting = function () {
    var emailSetting = {
      replyEmail: $scope.replyEmail,
      emailSignature: $scope.emailSignature
    };
    apiService.saveEmailSetting(emailSetting)
      .success(function () {
        console.log("Success :");
      })
      .error(function () {
        console.log("Fail :");
      })
  };

  $scope.cancleEmailSetting = function () {
    apiService.loadEmailSetting()
      .success(function (data) {
        $scope.replyEmail = data.replyEmail;
        $scope.emailSignature = data.emailSignature;
      })
  };

});