techlooper.controller("talentProfileController", function ($timeout, jsonValue, talentProfileService, $scope, $routeParams, $http) {
  //$timeout(function(){
  //  //talentProfileService.init();
  //  talentProfileService.showRating(parseFloat($scope.userProfile.rate));
  //}, 500);

  var hashEmail = $routeParams.text;//$.base64.decode($routeParams.text);
  $http.get(jsonValue.httpUri.talentProfile + "/" + hashEmail)
    .success(function (data, status, headers, config) {
      $scope.userProfile = data.userImportEntity;
      $scope.userProfile.itemSkills = [];
      for (var skillName in $scope.userProfile.ranks) {
          $scope.userProfile.itemSkills.push({
          name: skillName,
          rank: $scope.userProfile.ranks[skillName],
          logo: talentProfileService.getLogo(skillName),
          total: data.skillMap[skillName]
        });
      }

      //$scope.totalSkills = data.skillMap;
      if ($scope.userProfile.profiles.GITHUB.imageUrl === undefined) {
        $scope.userProfile.profiles.GITHUB.imageUrl = $scope.userProfile.profiles.GITHUB.imageurl;
      }

      if ($scope.userProfile.ranks.Overall === undefined) {
        $scope.userProfile.ranks.Overall = 50;
      }
      else {
        $scope.userProfile.ranks.Overall = Math.min(userProfile.ranks.Overall / 188501 * 100, 50);
      }

      $timeout(talentProfileService.showRating(parseFloat($scope.userProfile.rate)), 100);

      //console.log($scope.userProfile);
      $(window).scrollTop(0);
      $('[data-toggle="tooltip"]').tooltip({html:true});
    });

  $scope.githubLink = function(userProfile) {
    if (userProfile === undefined) {
      return "#";
    }
    return "https://github.com/" + userProfile.profiles.GITHUB.username;
  }

  //$scope.$watch("contentLoaded", function() {
  //  if ($scope.contentLoaded === true) {
  //    talentProfileService.showRating(parseFloat($scope.userProfile.rate));
  //  }
  //});
});
