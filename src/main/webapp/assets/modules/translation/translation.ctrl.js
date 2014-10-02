angular.module('Common').controller('translationController', function($scope, $translate) {
   $scope.setLang =  function() {
   	    var lang = $translate.use();
   		$translate.use(lang == "vi" ? "en-US":"vi");
   };
});