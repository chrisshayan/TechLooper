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
        scope.tagForm.autoTag.$edited = false;
        scope.tag = "";
        scope.autoTag = "";
        scope.tagList.length = 0;
      }

      scope.tags = scope.tags || [];
      scope.tagList = scope.tagList || [];

      scope.removeTag = function (tag) {
        scope.tags.splice(scope.tags.indexOf(tag), 1);
        resetForm();
      }

      scope.addTag = function (tag) {
        console.log(123);
        scope.tagForm.$submitted = true;

        var limitation = scope.tags.length >= scope.listMaxLength;
        scope.tagForm.tag.$setValidity("listMaxLength", !limitation);
        if (limitation) {
          return false;
        }

        if (!scope.tagForm.$valid) {
          return false;
        }

        var tag = tag || scope.tag || scope.autoTag || "";
        if (tag.length == 0) return false;
        if ($.inArray(tag, scope.tags) >= 0) {
          return false;
        }

        scope.tags.push(tag);
        resetForm();
        scope.$apply();
      }

      var getTags = function () {
        if (!scope.getTags) {
          return [];
        }

        if (!scope.tagForm.$valid) {
          return false;
        }

        scope.tagList.length = 0;
        scope.getTags(scope.autoTag)
          .success(function (data) {
            scope.tagList = data;
          })
          .error(function () {scope.tagList.length = 0;});
      }

      scope.status = function (type) {
        switch (type) {
          case "show-auto-complete-input":
            return scope.getTags;

          case "show-text-input":
            return !scope.getTags;

          case "show-error":
            var errorType = arguments[1];
            return scope.tagForm.tag.$error[errorType] || scope.tagForm.autoTag.$error[errorType];

          case "show-errors":
            return scope.tagForm.$submitted || scope.tagForm.tag.$edited || scope.tagForm.autoTag.$edited;
        }

        return false;
      }

      scope.submitTag = function (event) {
        if (event.which === 13) {
          event.preventDefault();
          scope.addTag();
          return false;
        }
        getTags();
      }

      scope.tagForm.tag.$validators.unique = function (modelValue, viewValue) {
        return scope.tags.indexOf(modelValue) < 0;
      }

      scope.tagForm.autoTag.$validators.unique = function (modelValue, viewValue) {
        return scope.tags.indexOf(modelValue) < 0;
      }

      if (scope.getTags) {
        element.find("input").keypress(function(event) {
          scope.submitTag(event);
        });
      }
    }
  }
});