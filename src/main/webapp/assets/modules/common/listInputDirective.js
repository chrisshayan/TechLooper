techlooper.directive("listInput", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/listInput.html",
    scope: {
      ngModel: "=",
      config: "=",
      organisers: "="
    },

    link: function (scope, element, attr, ctrl) {
      scope.ngModel = scope.ngModel || [];
      //var oriItems = angular.copy(scope.ngModel);


      scope.addItem = function() {
        scope.listForm.$setSubmitted();
        if (scope.listForm.$invalid) return;

        if (!scope.item || scope.item.length == 0) return;

        scope.ngModel.push(scope.item);

        scope.item = "";
        scope.listForm.$setPristine();
      }

      scope.removeItem = function(index) {
        scope.ngModel.splice(index, 1);
      }

      scope.listForm.inputItem.$validators.unique = function (modelValue, viewValue) {
        if (!modelValue) return true;
        if (modelValue.length == 0) return true;
        return scope.ngModel.indexOf(modelValue) < 0 && scope.organisers.indexOf(modelValue) < 0;
      };

      scope.status = function(type) {
        switch (type) {
          case "organiser":
            var item = arguments[1];
            return $.inArray(item, oriItems) > -1;

          case "attendee":
            var item = arguments[1];
            return !scope.status("organiser", item)
        }

        return false;
      }

      //scope.$on("bodyClicked", function(targetScope, e) {
      //  console.log(2);
      //  if ($(e.target).hasClass("add-item")) {
      //    console.log(123);
      //    return;
      //  }
      //  console.log(3);
      //  scope.item = "";
      //  scope.listForm.$setPristine();
      //});
    }
  }
});