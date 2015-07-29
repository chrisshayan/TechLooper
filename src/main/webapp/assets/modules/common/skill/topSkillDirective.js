techlooper.directive("topSkill", function (technicalDetailService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/skill/topSkill.html",
    scope: {
      skills: "=",
      totalJob: "="
    },

    link: function (scope, element, attr, ctrl) {
      //$.each(scope.skills, function (i, skill) {skill.id = i;});

      scope.showCircle = function (skill, index) {
        skill.id = index;
        var shownMe = $("#circles-" + skill.id + " > .circles-wrp").length > 0;
        if (shownMe == true) return true;
        technicalDetailService.showSkillsList(skill, scope.totalJob);
        return true;
      }
    }
  }
});