techlooper.controller("technicalDetailController", function (utils, connectionFactory, $routeParams, $rootScope,
                                                             technicalDetailService, $scope, $timeout, jsonValue, termService) {

  $scope.error = {};
  $scope.jobLevels = jsonValue.jobLevels;
  $scope.termRequest = {term: $routeParams.term};

  $scope.createTermRequestShadow = function () {
    $scope.termRequestShadow = $.extend(true, {}, $scope.termRequest);
  }

  $scope.deleteAllSkills = function () {
    $scope.termRequestShadow.skills.length = 0
  }

  $scope.selectJobLevel = function (jobLevel) {
    var translate = $rootScope.translate;
    $('span.lavelName').text(translate[jobLevel.translate]);
    $scope.termRequest.jobLevelIds = jobLevel.ids;
    if (jobLevel.id === -1) {
      delete $scope.termRequest.jobLevelIds;
    }
    $scope.loadData();
  }

  $scope.removeSkill = function (skill) {
    $scope.termRequestShadow.skills.splice($scope.termRequestShadow.skills.indexOf(skill), 1);
    delete $scope.error.newSkillName;
  }

  $scope.applySkills = function () {
    if ($scope.termRequestShadow.skills.length === 0) {
      return;
    }
    $scope.termRequest = $.extend(true, {}, $scope.termRequestShadow);
    $scope.loadData();
  }

  $scope.addSkill = function () {
    var newSkillName = $scope.newSkillName.trim();
    $scope.newSkillName = newSkillName;
    if (newSkillName.length == 0) {
      $scope.error = {};
      return;
    }

    if ($scope.termRequestShadow.skills.length === 5) {
      var translate = $rootScope.translate;
      $scope.error.newSkillName = translate.maximum5;
      return;
    }
    delete $scope.error.newSkillName;

    var skillLowerCases = $scope.termRequestShadow.skills.map(function (skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) >= 0) {
      var translate = $rootScope.translate;
      $scope.error.existSkillName = translate.hasExist;
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.termRequestShadow.skills.push(newSkillName);
      $scope.newSkillName = "";
    }
  }

  var termView = termService.toViewTerm($scope.termRequest);

  $scope.showCircle = function (skill) {
    var shownMe = $("#circles-" + skill.id + " > .circles-wrp").length > 0;
    if (shownMe) return true;
    technicalDetailService.showSkillsList(skill, $scope.termStatistic.totalJob);
    return true;
  }

  $scope.companyUrl = function (company) {//job-search/employer/{CompanyName}-{EmployerId1}_{EmployerId2}_{EmployerIdN}
      return sprintf("http://vietnamworks.com/job-search/employer/%s-%s", company.name, company.employerIds.join("_"));
  }

  $scope.loadData = function () {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    connectionFactory.termStatisticInOneYear($scope.termRequest)
      .success(function (data, status, headers) {
        $scope.termRequest.skills = data.skills.map(function (skill) {return skill.skillName;});
        $.each(data.skills, function (i, skill) {skill.id = i;});
        $scope.termStatistic = termService.toViewTerm(data);
        if (!technicalDetailService.trendSkills($scope.termStatistic)) {
          $('.no-data-chart').addClass('active');
        }
        else {
          $('.no-data-chart').removeClass('active');
        }
        utils.sendNotification(jsonValue.notifications.loaded);
      })
      .error(function (data, status, headers, config) {
      });
  }

  //technicalDetailService.skillManager();

  $scope.loadData();
});
