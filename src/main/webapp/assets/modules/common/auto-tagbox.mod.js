techlooper.directive('autoTagbox', function ($timeout) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/auto-tagbox.tem.html",
    scope: {
      tags: "=",
      config: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.errors = [];
      scope.removeTag = function(tag) {
        scope.tags.splice(scope.tags.indexOf(tag), 1);
        scope.errors.length = 0;
      }

      scope.addTag = function(tag) {
        if (!scope.config.newTag || !scope.config.newTag.length) {
          return;
        }

        scope.tags = scope.tags || [];
        scope.errors.length = 0;

        if (scope.tags.length >= 50) {
          return scope.errors.push("maximum50");
        }
        else if (scope.tags.indexOf(scope.config.newTag) > -1) {
          return scope.errors.push("hasExist");
        }

        scope.tags.push(scope.config.newTag);
        scope.config.newTag = "";
      }
    }
  }
});