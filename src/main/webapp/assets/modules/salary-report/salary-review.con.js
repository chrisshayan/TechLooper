techlooper.controller("salaryReviewController", function ($scope, vnwConfigService) {
  var state = {
    init: true,

    default: {
      showJob: true,
      skillBoxConfig: {
        placeholder: "mostRelevantSkills.placeholder",
        items: [],
        required: true
      },

      tabs: [
        {title: "aboutYourJob", class: "active"},
        {title: "aboutYourCompany"},
        {title: "yourSalaryReport"}
      ]
    },

    company: {
      showCompany: true,

      tabs: [
        {title: "aboutYourJob", class: "active"},
        {title: "aboutYourCompany", class: "active"},
        {title: "yourSalaryReport"}
      ]
    },

    report: {
      showReport: true,
      tabs: [
        {title: "aboutYourJob", class: "active"},
        {title: "aboutYourCompany", class: "active"},
        {title: "yourSalaryReport", class: "active"}
      ]
    }
  }

  $scope.selectize = vnwConfigService;

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

  $scope.$watch("state.skillBoxConfig.newTag", function (newVal) {
    if (!newVal) {return;}

    $.get("suggestion/skill/" + newVal)
      .success(function (data) {
        $scope.state.skillBoxConfig.items = data.items.map(function (item) {return item.name;});
        $scope.$apply();
      });
  }, true);

  $scope.changeState(state.default);
});
