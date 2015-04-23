techlooper.controller("technicalDetailController", function (utils, connectionFactory, $routeParams, $rootScope,
                                                             technicalDetailService, $scope, $timeout, jsonValue, termService) {

  $scope.error= {};
  $scope.jobLevels = jsonValue.jobLevels;
  $scope.termRequest = {term: $routeParams.term};
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
    $scope.termRequest.skills.splice($scope.termRequest.skills.indexOf(skill), 1);
    delete $scope.error.newSkillName;
  }

  $scope.applySkills = function () {
    $scope.loadData();
  }

  $scope.addSkill = function() {
    if ($scope.termRequest.skills.length === 5) {
      $scope.error.newSkillName = "Maximum 5 skills";
      return;
    }
    delete $scope.error.newSkillName;

    var newSkillName = $scope.newSkillName;
    $scope.newSkillName = newSkillName.trim();

    var skillLowerCases = $scope.termRequest.skills.map(function(skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) > 0) {
      $scope.error.existSkillName = "This skill has already";
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.termRequest.skills.push(newSkillName);
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

  $scope.companyUrl = function (company) {//java-fpt+at-it-software-i35-en
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
          console.log("Empty Chart");
        }
        utils.sendNotification(jsonValue.notifications.loaded);
      })
      .error(function (data, status, headers, config) {
      });
  }

  //technicalDetailService.skillManager();

  $scope.loadData();
});
