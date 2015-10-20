techlooper.controller("emailSetting", function ($scope, apiService) {

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

});