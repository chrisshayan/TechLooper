angular.module("Footer").controller("footerController", ["$scope", "jsonFactory", function($scope, jsonFactory) {
   $scope.companies = jsonFactory.companies;

   var cp = $('.companies-bar'), list = $('.companies-list');
   cp.click(function() {
      var ic = $(this).find('i');
      if (ic.hasClass('fa-arrow-up')) {
         ic.removeClass('fa-arrow-up').addClass('fa-arrow-down');
         list.animate({
            height : '120px',
            padding : '10px'
         });
      }
      else {
         ic.removeClass('fa-arrow-down').addClass('fa-arrow-up');
         list.animate({
            height : '0',
            padding : '5px 10px'
         });
      }
   });
}]);