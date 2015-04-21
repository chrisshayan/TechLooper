techlooper.factory("companyProfileService", function () {
  var instance = {
    followManager: function(){
      $('.follow-button').find('button').prop('disabled', true).text('Following');
      ga('send', 'event', 'FollowCompany', 'clicked');
    }
  };
  return instance;

});