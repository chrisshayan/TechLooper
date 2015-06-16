techlooper.directive("srAboutYourJob", function ($http, vnwConfigService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-about-your-job.tem.html",
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

      var demandSkillSuggestion = function () {
        //delete scope.state.demandSkills;

        var request = {};
        scope.salaryReview.jobTitle && (request.jobTitle = scope.salaryReview.jobTitle);
        scope.salaryReview.jobLevelIds && (request.jobLevelIds = vnwConfigService.getJobLevelIds(scope.salaryReview.jobLevelIds));

        if ($.isEmptyObject(request) || !scope.salaryReview.jobTitle) {
          return false;
        }

        $http
          .post("getPromoted", request)
          .success(function (userPromotionInfo) {
            scope.state.demandSkills = userPromotionInfo.topDemandedSkills.map(function (skill) {
              return {title: skill.skillName};
            });
          });
      }

      scope.$watch("salaryReview.jobTitle", function (newVal) {
        jobTitleSuggestion(newVal);
        demandSkillSuggestion();
      });

      scope.$watch("salaryReview.jobLevelIds", function (newVal) {
        demandSkillSuggestion();
      });

      scope.$watch("salaryReview.skills", function (newVal) {
        delete scope.skillBoxConfig.items;
      }, true);

      scope.$watch("salaryReview.reportTo", function (newVal) {jobTitleSuggestion(newVal);}, true);

      scope.$watch("skillBoxConfig.newTag", function (newVal) {
        delete scope.skillBoxConfig.items;

        if (!newVal) {
          return;
        }

        $http.get("suggestion/skill/" + newVal)
          .success(function (data) {
            scope.skillBoxConfig.items = data.items.map(function (item) {return item.name;});
          });
      });
    }
  }
});