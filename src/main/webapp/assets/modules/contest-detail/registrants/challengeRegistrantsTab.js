techlooper.directive('funnelManagement', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants/funnelManagement.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
  .directive('registrationList', function () {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/contest-detail/registrants/registrationList.html",
      link: function (scope, element, attr, ctrl) {
      }
    };
  })
.directive('qualifyAllToNextPhase', function (ngProgressFactory, $timeout, apiService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contest-detail/registrants/qualifyAllToNextPhase.html",
    link: function (scope, element, attr, ctrl) {
      scope.hideQualifyAllForm = function(){
        scope.progressbar = ngProgressFactory.createInstance();
        $timeout(scope.progressbar.complete(), 3000);
        delete scope.showQualifyAllRegistrantsForm;
      };
      scope.qualifyToAllRegistrant = function(challenge){
        apiService.save(challenge.challengeId)
            .success(function (data) {});
      }
    }
  };
});