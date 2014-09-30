angular.module('Common').controller('translationController', function($scope, $translate) {
   $scope.setLang = function(langKey) {
      $translate.use(langKey);
   };
});