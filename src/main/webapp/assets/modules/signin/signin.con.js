angular.module('SignIn').controller('signInController', function (jsonValue, utils, $scope, shortcutFactory) {
	
	$scope.accounts = jsonValue.accountSignin;

	$('.signin-accounts').parallax();


	// $scope.animation = function (event) {
	// 	$(event.currentTarget).addClass('puffOut');
	// 	// var wait = window.setTimeout( function(){
	// 	// 	$(event.currentTarget).find('img').removeClass('puffOut')
	// 	// }, 1500 );
	// }
    $('.btn-close').click(function(){shortcutFactory.trigger('esc');});
    $('.btn-logo').click(function(){shortcutFactory.trigger('esc');});


});