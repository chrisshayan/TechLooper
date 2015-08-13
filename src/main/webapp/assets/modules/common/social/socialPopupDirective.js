techlooper.directive("socialPopup", function ($http) {
    return {
      restrict: "E",
      replace: true,
      scope: {},
      templateUrl: "modules/common/social/socialPopup.html",
      controller: "socialPopupController"
    }
  });

