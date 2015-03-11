techlooper.directive("talentSearch", function ($compile, $location, utils, jsonValue) {

  return {
    restrict: "A",
    templateUrl: function(elem, attrs) {
      switch (utils.getView()) {
        case jsonValue.views.home:
          return "modules/talent-search/main/main.tem.html";
      }
      return '';
    }
  };
}).directive("tsHeader", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/header/header.tem.html",
    controller: "tsHeaderController"
  }
}).directive("tsFooter", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/footer/footer.tem.html",
    controller: "tsFooterController"
  }
});