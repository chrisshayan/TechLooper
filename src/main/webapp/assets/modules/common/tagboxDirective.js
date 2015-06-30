techlooper.directive('tagbox', function ($rootScope) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/tagbox.html",
    scope: {
      tags: "=",
      type: "@",
      placeholder: "@",
      listMaxLength: "@",
      maxTagLength: "@"
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

        var limitation = scope.tags.length >= scope.listMaxLength;
        scope.tagForm.tag.$setValidity("listMaxLength", !limitation);
        if (limitation) {
          return false;
        }

        if (!scope.tagForm.$valid) {
          return false;
        }

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
    }
  }
});