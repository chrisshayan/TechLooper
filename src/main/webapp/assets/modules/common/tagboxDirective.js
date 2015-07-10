techlooper.directive('tagbox', function ($http) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/tagbox.html",
    scope: {
      tags: "=",
      type: "@",
      placeholder: "@",
      listMaxLength: "@",
      maxTagLength: "@",
      getTags: "="
    },

    /**
     * @event "addedTagSuccessful"
     * */
    link: function (scope, element, attr, ctrl) {

      var resetForm = function () {
        scope.tagForm.$setPristine();
        scope.tagForm.tag.$edited = false;
      }

      scope.tags = scope.tags || [];
      scope.tagList = scope.tagList || [];
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

        console.log(scope.tagForm);
        if (!scope.tagForm.$valid) {
          return false;
        }

        scope.tags.push(tag);
        scope.tag = "";
        resetForm();
      }

      var getTags = function () {
        if (!scope.getTags) {
          return [];
        }

        scope.getTags()
          .success(function (data) {
            console.log(data);
            scope.tagList = data;
          })
          .error(function () {scope.tagList.length = 0;});
      }

      scope.status = function (type) {
        var args = [].slice.call(arguments).slice(1);
        switch (type) {
          case "show-auto-complete-input":
            return scope.getTags;

          case "show-text-input":
            return !scope.getTags;

          case "view-style":
            return scope.getTags ? "auto-complete" : "normal";

        }

        return false;
      }

      scope.submitTag = function (event) {
        console.log(scope.tagForm);
        if (event.which === 13) {
          event.preventDefault();
          scope.addTag(scope.tag);
          return false;
        }
        getTags();
      }

      scope.tagForm.tag.$validators.unique = function (modelValue, viewValue) {
        return scope.tags.indexOf(modelValue) < 0;
      }
    }
  }
});