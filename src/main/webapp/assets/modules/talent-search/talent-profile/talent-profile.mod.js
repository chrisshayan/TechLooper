techlooper.directive("generalInfo", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/general-info.tem.html",
  }
}).directive("evaluation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/evaluation.tem.html",
  }
}).directive("resume", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/resume.tem.html",
  }
});