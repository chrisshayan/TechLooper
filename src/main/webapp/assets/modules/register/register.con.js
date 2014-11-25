angular.module('Register').controller('registerController', function (shortcutFactory) {
	utils.sendNotification(jsonValue.notifications.switchScope, $scope);
	$('input[type="checkbox"]').checkbox();

	$("#salary").slider({});
	
    $('.btn-close').click(function(){shortcutFactory.trigger('esc');});
    $('.btn-logo').click(function(){shortcutFactory.trigger('esc');});
});