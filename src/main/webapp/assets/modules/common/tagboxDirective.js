techlooper.directive('tagbox', function ($rootScope) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/tagbox.html",
    scope: {
      tags: "=",
      type: "@"
    },

    /**
     * @event "addedTagSuccessful"
     * */
    link: function (scope, element, attr, ctrl) {

      var resetForm = function () {
        scope.tagForm.$setPristine();
        //scope.tagForm.$setUntouched();
        //scope.tagForm.tag.$touched = false;
      }

      scope.tags = scope.tags || [];
      scope.tag = "";

      scope.removeTag = function (tag) {
        scope.tags.splice(scope.tags.indexOf(tag), 1);
        resetForm();
      }

      scope.addTag = function (tag) {
        scope.tagForm.$submitted = true;
        if (!scope.tagForm.$valid) {
          return false;
        }

        //scope.tagForm.tag.$setValidity("required", scope.tag.length > 0);
        //if (scope.tag.length == 0) {
        //  return;
        //}

        scope.tags.push(tag);
        scope.tag = "";
        resetForm();
      }

      scope.submitTag = function (event, tag) {
        if (event.which === 13) {
          event.preventDefault();
          scope.addTag(scope.tag);
          return false;
        }
      }

      scope.tagForm.tag.$validators.unique = function (modelValue, viewValue) {
        return scope.tags.indexOf(modelValue) < 0;
      }

      //$rootScope.$on("formSubmitted", function () {
      //  console.log(scope.tagForm);
      //  scope.tagForm.$setValidity("required", scope.tags.length == 0);
      //});

      //scope.tagForm.tags.$validators.arrayRequired = function (modelValue, viewValue) {
      //  console.log(scope.tags);
      //  return scope.tags.length > 0;
      //}

      //scope.$watch("tags", function(tags, oldVal) {
      //  if (!tags && !oldVal) return;
      //
      //  console.log(scope.tagForm.tags);
      //}, true);
    }
  }
});