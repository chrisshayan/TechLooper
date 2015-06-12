techlooper.controller('getPromotedController', function ($scope, validatorService, vnwConfigService) {
  $scope.getPromoted = {};
  $scope.selectize = vnwConfigService;
  $scope.validationGetPromoted = function(st){
    var elems = st.find("[validate]");
    $scope.error = validatorService.validate(elems);
  };
  $scope.getPromoted = function(){
    var jobTitle = $('.gp-form-field');
    if(!$scope.validationGetPromoted(jobTitle)){
      return false;
    }
  };
});