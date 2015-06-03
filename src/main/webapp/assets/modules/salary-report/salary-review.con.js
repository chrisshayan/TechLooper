techlooper.controller("salaryReviewController", function ($scope, $rootScope, jsonValue, $http, utils, $translate,
                                                          $route, $location, connectionFactory, $timeout, validatorService) {
  var state = {
    init: true,

    default: {
      showJob: true,
      skillBoxConfig: {
        placeholder: "mostRelevantSkills.placeholder",
        items: []
      }
    },

    company: {
      showCompany: true
    },

    report: {
      showReport: true
    }
  }

  $scope.changeState = function (st) {
    st = st || state.default;
    var preferState = $.extend(true, {}, (typeof st === 'string') ? state[st] : st);
    if (state.init) {
      //TODO: check validation and return if any error
    }
    delete state.init;
    $scope.state = preferState;
  }

  $scope.$watch("state.skillBoxConfig.newTag", function () {

    if (!$scope.state.skillBoxConfig.newTag) {
      $scope.state.skillBoxConfig.items = [];
      return;
    }

    $.get("suggestion/skill/" + $scope.newTag)
      .success(function (data) {
        $scope.state.skillBoxConfig.items = data.items.map(function (item) {return item.name;});
        //$scope.$apply();
      });
  }, true);

  $scope.changeState(state.default);
});
