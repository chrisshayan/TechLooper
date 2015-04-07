techlooper.factory("companyProfileService", function () {
  var instance = {
    followManager: function(){
      $('.follow-button').find('button').prop('disabled', true).text('Following');
    }
  };
  return instance;

});