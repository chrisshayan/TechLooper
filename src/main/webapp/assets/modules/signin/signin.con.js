angular.module('SignIn').controller('signInController', function ($location, jsonValue, utils, $scope, shortcutFactory) {
	
	$scope.accounts = jsonValue.accountSignin;
	
	$('.signin-accounts').parallax();
	
  $('.btn-close').click(function(){shortcutFactory.trigger('esc');});
  $('.btn-logo').click(function(){shortcutFactory.trigger('esc');});

  $(".signin-popup-close").on('click', function() {
    $('#signin-form').modal('hide');
  });

  $('.sign-successful').on('click', function(){
  	$location.path('/register');
  	$scope.$apply();
  });
});