angular.module('Register').controller('registerController', function (shortcutFactory) {

	$('input[type="checkbox"]').checkbox();

	$("#salary").slider({});
	
    $('.btn-close').click(function(){shortcutFactory.trigger('esc');});
    $('.btn-logo').click(function(){shortcutFactory.trigger('esc');});
});