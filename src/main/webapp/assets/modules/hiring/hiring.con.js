techlooper.controller('hiringController', function ($scope, $timeout) {
  $scope.checkingGoogle =  function(){
    ga("send", {
      hitType: "event",
      eventCategory: "onlinecontest",
      eventAction: "click",
      eventLabel: "postnowbtn"
    });
  };
  if(localStorage.NG_TRANSLATE_LANG_KEY == 'vi'){
    $('.hiring-how-does-work-content').find('.hiring').addClass('vi');
  }else{
    $('.hiring-how-does-work-content').find('.hiring').removeClass('vi');
  };
  $scope.gotoOtherPage  = function(eventCategory, eventLabel, url){
    ga("send", {
      hitType: "event",
      eventCategory: eventCategory,
      eventAction: "click",
      eventLabel: eventLabel
    });
    window.location.href = url;
  };
});