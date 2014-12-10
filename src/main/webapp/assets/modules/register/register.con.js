angular.module('Register').controller('registerController', function ($scope, shortcutFactory, jsonValue) {
	$('input[type="checkbox"]').checkbox();
	$("#salary").slider({});
	$scope.accounts = jsonValue.accountSignin;
  $('.btn-close').click(function(){shortcutFactory.trigger('esc');});
  $('.btn-logo').click(function(){shortcutFactory.trigger('esc');});

  $('.register-successful').click(function(){shortcutFactory.trigger('esc');});
});