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
    $scope.termRequest.jobLevelId = jobLevel.id;
    if ($scope.termRequest.jobLevelId === -1) {
      delete $scope.termRequest.jobLevelId;
    }
    $scope.loadData();
  }

  $scope.removeSkill = function (skill) {
    $scope.termRequestShadow.skills.splice($scope.termRequestShadow.skills.indexOf(skill), 1);
    delete $scope.error.newSkillName;
  }

  $scope.applySkills = function () {
    $scope.termRequest = $.extend(true, {}, $scope.termRequestShadow);
    $scope.loadData();
  }

  $scope.addSkill = function () {
    if ($scope.termRequestShadow.skills.length === 5) {
      $scope.error.newSkillName = "Maximum 5 skills";
      return;
    }
    delete $scope.error.newSkillName;

    var newSkillName = $scope.newSkillName;
    $scope.newSkillName = newSkillName.trim();

    var skillLowerCases = $scope.termRequestShadow.skills.map(function (skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) > 0) {
      $scope.error.existSkillName = "This skill has already";
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

  $scope.companyUrl = function (company) {//java-fpt+at-it-software-i35l5-en
    if ($scope.termRequest.jobLevelId > 0) {
      return sprintf("http://vietnamworks.com/%s-%s+at-it-software-i35l%d-en", termView.label, company.name, $scope.termRequest.jobLevelId);
    }
    return sprintf("http://vietnamworks.com/%s-%s+at-it-software-i35-en", termView.label, company.name);
  }

  $scope.loadData = function () {
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    connectionFactory.termStatisticInOneYear($scope.termRequest)
      .success(function (data, status, headers, config) {
        $scope.termRequest.skills = data.skills.map(function (skill) {return skill.skillName;});
        $.each(data.skills, function (i, skill) {skill.id = i;});
        $scope.termStatistic = termService.toViewTerm(data);
        if (!technicalDetailService.trendSkills($scope.termStatistic)) {
          $('.no-data-chart').addClass('active');
        }else{
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
