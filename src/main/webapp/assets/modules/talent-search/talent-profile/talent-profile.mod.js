techlooper.directive("general-info", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/general-info.tem.html",
    controller: "talentProfileController"
  }
}).directive("evaluation", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/evaluation.tem.html",
    controller: "talentProfileController"
  }
}).directive("resume", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/talent-profile/resume.tem.html",
    controller: "talentProfileController"
  }
});