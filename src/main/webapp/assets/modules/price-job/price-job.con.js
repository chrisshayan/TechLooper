techlooper.controller("priceJobController", function ($scope, jsonValue) {
  $scope.step = "step1";
  $scope.validate = function(){
    return true;
  }
  $scope.nextStep = function (step, priorStep) {
    if ((($scope.step === priorStep || step === "step3") && !$scope.validate()) || $scope.step === "step3") {
      return;
    }
    var swstep = step || $scope.step;
    $scope.step = swstep;

    switch (swstep) {
      case "step3":

        break;
    }
  }
});