techlooper.factory("joinNowService", function(apiService, localStorageService) {

  var from = localStorageService.get("joinForm");
  var id = localStorageService.get("id");
  switch (from) {
    case "webinar":

      break;
  }

  localStorageService.remove("joinForm");
  localStorageService.remove("id");

  return {
    initialize: function() {}
  }
});