techlooper.controller('hiringController', function ($scope, $timeout) {
  $scope.checkingGoogle =  function(){
    ga("send", {
      hitType: "event",
      eventCategory: "onlinecontest",
      eventAction: "click",
      eventLabel: "postnowbtn"
    });
  }
  $timeout(function(){
    var tallest = 0;
    $('.hiring-main-feature-item').each(function () {
      var thisHeight = $(this).height();
      if (thisHeight > tallest)
        tallest = thisHeight;
    });
    $('.hiring-main-feature-item').height(tallest + $('.cta-button').height() + 10);
  }, 100);
});