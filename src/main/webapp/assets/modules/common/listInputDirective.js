techlooper.directive("listInput", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/listInput.html",
    scope: {
      ngModel: "=",
      config: "="
    },

    link: function (scope, element, attr, ctrl) {
      scope.ngModel = scope.ngModel || [];


      scope.addItem = function() {
        scope.listForm.$setSubmitted();
        if (scope.listForm.$invalid) {
          return;
        }

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
        return scope.ngModel.indexOf(modelValue) < 0;
      };

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