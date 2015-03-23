techlooper.controller("talentProfileController", function ($timeout, jsonValue, talentProfileService, $scope, $routeParams, $http) {
  //$timeout(function(){
  //  //talentProfileService.init();
  //  talentProfileService.showRating(parseFloat($scope.userProfile.rate));
  //}, 500);

  var email = $.base64.decode($routeParams.text);
  $http.get(jsonValue.httpUri + "/" + email)
    .success(function (data, status, headers, config) {
      $scope.talentProfile = data;
    });

  $scope.$watch("contentLoaded", function() {
    if ($scope.contentLoaded === true) {
      talentProfileService.showRating(parseFloat($scope.userProfile.rate));
    }
  });

  //$scope.userProfile = {
  //  "email": "abc@missing.com",
  //  "fullName": "Peter Parker",
  //  "rate": "3",
  //  "ranks": {
  //    "java": 400,
  //    "c#": 239,
  //    "AngularJS": 1024
  //  },
  //  "score": 1600,
  //  "profiles": {
  //    "GITHUB": {
  //      "email": "skynet.hd.91@gmail.com",
  //      "originalEmail": null,
  //      "fullName": "Xon Xoi",
  //      "crawlerSource": "GITHUB",
  //      "username": "xonxoi",
  //      "imageUrl": "https://avatars3.githubusercontent.com/u/10355175?v=3&s=460",
  //      "location": "Hanoi, Vietnam",
  //      "dateJoin": "2014-12-31T04:34:29Z",
  //      "company": "Niteco",
  //      "website": null,
  //      "description": "xonxoi has 5 repositories written in Java, CSS, and JavaScript. Follow their code on GitHub.",
  //      "followers": "13",
  //      "following": "40",
  //      "organizations": null,
  //      "popularRepositories": [
  //        "/xonxoi/interportlet-communication-jms",
  //        "/xonxoi/portlet-chart",
  //        "/xonxoi/portlet-config-mode",
  //        "/xonxoi/portlet-spring",
  //        "/xonxoi/upload-images-portlet"
  //      ],
  //      "contributedRepositories": null,
  //      "contributedLongestStreakTotal": "2",
  //      "lastContributedDateTime": "2015-02-06T08:00:00Z",
  //      "contributeNumberLastYear": "26",
  //      "contributeNumberLastYearPeriod": "Mar 18, 2014 – Mar 18, 2015",
  //      "contributeLongestStreakPeriod": "January 13 – January 14",
  //      "contributeCurrentStreakTotal": "0",
  //      "skills": [
  //        "Java",
  //        "CSS",
  //        "JavaScript"
  //      ],
  //      "numberOfRepositories": 5
  //    }
  //  }
  //};

  $scope.userProfile.itemSkills = [];
  for (var skillName in $scope.userProfile.ranks) {
    $scope.userProfile.itemSkills.push({
      name: skillName,
      rank: $scope.userProfile.ranks[skillName],
      logo: talentProfileService.getLogo(skillName)
    });
  }

});
