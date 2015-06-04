techlooper
  .directive("srNavigation", function () {
    return {
      restrict: "A",
      replace: true,
      templateUrl: "modules/salary-report/sr-navigation.tem.html"
    }
  })
  .directive("srAboutYourReport", function ($http, $location, utils, jsonValue) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-report.tem.html",
      link: function (scope, element, attr, ctrl) {
        var campaign = $location.search();

        var beforeSendSalaryReport = function (salaryReview) {
          var jobLevelIds = salaryReview.jobLevelIds;
          salaryReview.jobLevelIds = jsonValue.jobLevelsMap['' + jobLevelIds].ids;
          return salaryReview;
        }

        var afterSendSalaryReport = function (salaryReview) {
          salaryReview.campaign = !$.isEmptyObject(campaign);
          return salaryReview;
        }

        scope.$watch("state", function (newVal) {
          if (!scope.state.ableCreateNewReport) {
            return;
          }

          var salaryReview = beforeSendSalaryReport($.extend(true, {}, scope.salaryReview));
          utils.sendNotification(jsonValue.notifications.switchScope);

          $http.post(jsonValue.httpUri.salaryReview, salaryReview)
            .success(function (data, status, headers, config) {
              scope.salaryReview = afterSendSalaryReport(data);
              scope.salaryReport = scope.salaryReview.salaryReport;

              var hasCity = jsonValue.companyPromotion.AcceptedCity.indexOf(scope.salaryReview.locationId) >= 0;
              var enoughMoney = (scope.salaryReview.usdToVndRate * scope.salaryReview.netSalary) >= jsonValue.companyPromotion.minSalary;
              var hasDone = localStorage.getItem('PROMOTION-KEY') === 'yes';
              scope.state.showPromotion = hasCity && enoughMoney && !hasDone;
              scope.state.showAskPromotion = scope.state.showPromotion;

              utils.sendNotification(jsonValue.notifications.loaded);
            });
        });
      }
    }
  })
  .directive("srAboutYourCompany", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-company.tem.html"
    }
  })
  .directive("srAboutYourJob", function ($http) {
    return {
      restrict: "E",
      replace: true,
      templateUrl: "modules/salary-report/sr-about-your-job.tem.html",
      link: function (scope, element, attr, ctrl) {
        var jobTitleSuggestion = function (jobTitle) {
          if (!jobTitle) {return;}

          $http.get("suggestion/jobTitle/" + jobTitle)
            .success(function (data) {
              scope.state.jobTitles = data.items.map(function (item) {return item.name;});
            });
        }

        scope.$watch("salaryReview.jobTitle", function (newVal) {jobTitleSuggestion(newVal);}, true);
        scope.$watch("salaryReview.reportTo", function (newVal) {jobTitleSuggestion(newVal);}, true);

        scope.$watch("state.skillBoxConfig.newTag", function (newVal) {
          if (!newVal) {return;}

          $http.get("suggestion/skill/" + newVal)
            .success(function (data) {
              scope.state.skillBoxConfig.items = data.items.map(function (item) {return item.name;});
            });
        }, true);
      }
    }
  });
