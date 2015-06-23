techlooper.directive("srJobInformation", function ($http, validatorService, $translate, vnwConfigService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-job-information.tem.html",
    link: function (scope, $timeout) {

      //scope.salaryReview = $.extend(true, {}, scope.salaryReview);

      scope.$watch("salaryReview", function () {
        if (scope.state.editableSalaryReview) {
          scope.sr = $.extend(true, {}, scope.salaryReview);
        }
      });

      scope.showUpdateInfo = function () {
        delete scope.state.editableSalaryReview;

        scope.state.validateAllState = true;
        delete scope.state.jobTitles;

        scope.cloneSalaryReview = $.extend(true, {}, scope.salaryReview);
        //scope.sr = $.extend(true, {}, scope.salaryReview);

        $('.update-job-information').removeClass('only-read');
        $('.ic-update-info').addClass('clicked');
      };

      $('.overlap-form').mouseenter(function () {
        $('.ic-update-info').show();
      }).mouseleave(function () {
        $('.ic-update-info').hide();
      });

      scope.updateSalaryReport = function () {
        scope.error = $.extend(true, {},validatorService.validate($(".user-personal-info").find("[validate]")));
        scope.sr.skills = scope.sr.skills || [];
        if (scope.sr.skills.length === 0) {
          $translate("requiredThisField").then(function(trans) {scope.error.skills = trans;});
          scope.error.skills = true;
        }

        if (!$.isEmptyObject(scope.error)) {
          return true;
        }

        scope.salaryReview = $.extend(true, {}, scope.sr);
        scope.changeState("report", true);
        delete scope.$parent.email;
        $('input[type=email]').val('');
        ga('send', {
          'hitType': 'event',
          'eventCategory': 'editsalaryreport',
          'eventAction': 'click',
          'eventLabel': 'savebtn'
        });
      }

      scope.cancelUpdateSalaryReport = function () {
        scope.state.editableSalaryReview = true;
        //scope.cloneSalaryReview && (scope.salaryReview = $.extend(true, {}, scope.cloneSalaryReview));

        delete scope.state.validateAllState;

        scope.sr = $.extend(true, {}, scope.salaryReview);
        delete scope.cloneSalaryReview;
        delete scope.error;
        delete scope.state.jobTitles;

        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      }

      var demandSkillSuggestion = function () {
        delete scope.state.demandSkills;
        if (!scope.sr) return;

        var request = {};
        scope.sr.jobTitle && (request.jobTitle = scope.sr.jobTitle);
        scope.sr.jobLevelIds && (request.jobLevelIds = vnwConfigService.getJobLevelIds(scope.sr.jobLevelIds));

        if ($.isEmptyObject(request) || !scope.sr.jobTitle) {
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

      var jobTitleSuggestion = function (jobTitle) {
        if (!jobTitle || scope.state.editableSalaryReview) {return;}

        $http.get("suggestion/jobTitle/" + jobTitle)
          .success(function (data) {
            scope.state.jobTitles = data.items.map(function (item) {return item.name;});
          });
      }

      delete scope.state.jobTitles;

      scope.$watch("sr.jobTitle", function (newVal) {
        jobTitleSuggestion(newVal);
        demandSkillSuggestion();
      });
      scope.$watch("sr.jobLevelIds", function (newVal) {
        jobTitleSuggestion(newVal);
        demandSkillSuggestion();
      });
      scope.$watch("sr.reportTo", function (newVal) {jobTitleSuggestion(newVal);});

      scope.$watch("sr.skills", function (newVal) {
        delete scope.skillBoxConfig.items;
      }, true);

      scope.$on("state change success", function () {
        $('.update-job-information').addClass('only-read');
        $('.ic-update-info').removeClass('clicked');
      });
    }
  }
});