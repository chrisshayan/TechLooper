angular.module('SignIn').controller('signInController', function (jsonValue, utils, $scope, shortcutFactory) {
	
	$scope.accounts = jsonValue.accountSignin;
	utils.sendNotification(jsonValue.notifications.switchScope, $scope);
	$('.signin-accounts').parallax();
	
    $('.btn-close').click(function(){shortcutFactory.trigger('esc');});
    $('.btn-logo').click(function(){shortcutFactory.trigger('esc');});


});