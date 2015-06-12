techlooper.directive("getPromotedForm", function ($http, $location, utils, jsonValue, vnwConfigService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/get-promoted/gp-form.tem.html",
    link: function (scope, element, attr, ngModel) {
      var jobTitleSuggestion = function (jobTitle) {
        if (!jobTitle) {
          return;
        }

        $http.get("suggestion/jobTitle/" + jobTitle)
          .success(function (data) {
            scope.state.jobTitles = data.items.map(function (item) {return item.name;});
          });
      }
      scope.$watch("getPromoted.jobTitle", function (newVal) {
        jobTitleSuggestion(newVal);
      });
    }
  }
});