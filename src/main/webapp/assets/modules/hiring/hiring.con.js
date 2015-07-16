techlooper.controller('hiringController', function ($scope) {
  $scope.checkingGoogle =  function(){
    ga("send", {
      hitType: "event",
      eventCategory: "onlinecontest",
      eventAction: "click",
      eventLabel: "postnowbtn"
    });
  }
});