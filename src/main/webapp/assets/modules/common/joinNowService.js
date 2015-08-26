techlooper.factory("joinNowService", function(apiService, localStorageService, $location) {

  var param = $location.search();
  if ($.isEmptyObject(param)) {
    return;
  }

  var from = localStorageService.get("joinForm");
  var id = localStorageService.get("id");
  switch (from) {
    case "webinar":
      //apiService.
      break;
  }

  localStorageService.remove("joinForm");
  localStorageService.remove("id");

  return {
    initialize: function() {}
  }
});