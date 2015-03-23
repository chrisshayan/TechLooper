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
          total: $scope.totalSkills[skillName]
        });
      }

      $scope.totalSkills = data.skillMap;
    });

  $scope.$watch("contentLoaded", function() {
    if ($scope.contentLoaded === true) {
      talentProfileService.showRating(parseFloat($scope.userProfile.rate));
    }
  });
});
