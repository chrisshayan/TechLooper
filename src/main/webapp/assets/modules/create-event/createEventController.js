techlooper.controller("createEventController", function ($scope, $translate, jsonValue) {

  $('.selectionTime').datetimepicker();

  var placeholder = $translate.instant('whoJoinAndWhyEx');
  $('#txtWhyEvent').val(placeholder);
  $('#txtWhyEvent').focus(function(){
    if($(this).val() === placeholder){
      $(this).val('');
      $(this).removeClass('change-color');
    }
  });

  $('#txtWhyEvent').blur(function(){
    if($(this).val() ===''){
      $(this).val(placeholder);
      $(this).addClass('change-color');
    }
  });

  $scope.errors = [];

  $scope.removeTag = function (tag) {
    $scope.emails.splice($scope.emails.indexOf(tag), 1);
    $scope.errors.length = 0;
  }

  $scope.emails = $scope.emails || [];

  $scope.addTag = function () {
    var newTag = $scope.attendants;
    if (!newTag || !newTag.length) {
      return;
    }
    if($('#txtAttendants').hasClass("ng-invalid-email")){
      return $scope.errors.push("emailInvalid");
    }
    else if ($scope.emails.length >= 100) {
      return $scope.errors.push("maximum100");
    }
    else if (newTag.length > 40) {
      return $scope.errors.push("tooLong");
    }
    else if ($scope.emails.indexOf(newTag) > -1) {
      return $scope.errors.push("hasEmailExist");
    }
    $scope.emails.push(newTag);
    $scope.errors.length = 0;
    $scope.attendants = "";
  }
});