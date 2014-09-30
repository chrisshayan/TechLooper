angular.module('Common').controller('translationController', function($scope, $translate) {
   $(".langKey").on('click', function() {
   	    var lang = $translate.use();
   		$translate.use(lang == "vi" ? "en-US":"vi");
   });
});