techlooper.controller("salaryReviewController", function ($scope, $rootScope, jsonValue, $http, utils, $translate,
                                                          $route, $location, connectionFactory, $timeout, validatorService) {
  var state = {
    init: true,

    default: {
      showJob: true,
      skillBoxConfig: {
        placeholder: "mostRelevantSkills.placeholder",
        items: [],
        required: true
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

  var jobTitleSuggestion = function (jobTitle) {
    if (!jobTitle) {return;}

    $.get("suggestion/jobTitle/" + jobTitle)
      .success(function (data) {
        $scope.state.jobTitles = data.items.map(function (item) {return item.name;});
        $scope.$apply();
      });
  }

  $scope.$watch("salaryReview.jobTitle", function (newVal) {jobTitleSuggestion(newVal);}, true);
  $scope.$watch("salaryReview.reportTo", function (newVal) {jobTitleSuggestion(newVal);}, true);

  $scope.$watch("state.skillBoxConfig.newTag", function () {
    var newTag = $scope.state.skillBoxConfig.newTag;
    if (!newTag) {return;}

    $.get("suggestion/skill/" + newTag)
      .success(function (data) {
        $scope.state.skillBoxConfig.items = data.items.map(function (item) {return item.name;});
        $scope.$apply();
      });
  }, true);

  $scope.changeState(state.default);
});
