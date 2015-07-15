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
        scope.tagForm.$submitted = true;

        var tag = tag || scope.tag || scope.autoTag || "";
        if (tag.length == 0) return false;

        //var limitation = scope.tags.length >= scope.listMaxLength;
        //scope.tagForm.tag.$setValidity("listMaxLength", !limitation);
        //if (limitation) {
        //  return false;
        //}

        if (!scope.tagForm.$valid) {
          return false;
        }

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

      var uniqueValidator = function (modelValue, viewValue) {
        if (!modelValue) return true;
        if (modelValue.length == 0) return true;
        if (scope.tags.length == scope.listMaxLength) return true;
        return scope.tags.indexOf(modelValue) < 0;
      }

      var limitValidator = function (modelValue, viewValue) {
        if (!modelValue) return true;
        if (modelValue.length == 0) return true;
        var invalid = scope.tags.length < scope.listMaxLength;
        return invalid;
      }

      scope.tagForm.tag.$validators.unique = uniqueValidator;
      scope.tagForm.autoTag.$validators.unique = uniqueValidator;

      scope.tagForm.tag.$validators.listMaxLength = limitValidator;
      scope.tagForm.autoTag.$validators.listMaxLength = limitValidator;

      if (scope.getTags) {
        element.find("input").keypress(function(event) {
          scope.submitTag(event);
        });
      }

      scope.autoTagType = function(tag) {
        if (tag.length == 0) scope.tagList.length = 0;
        scope.tagForm.$setPristine();
      }
    }
  }
});