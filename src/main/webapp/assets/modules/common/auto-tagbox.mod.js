techlooper.directive('autoTagbox', function ($timeout) {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/common/tag-box.tem.html",
    scope: {
      tags: "=",
      config: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.removeTag = function(tag) {
        scope.tags.splice(scope.tags.indexOf(tag), 1);
        scope.error = [];
      }

      scope.addTag = function(tag) {
        console.log(tag);
        console.log(scope.config.newTag);
      }


    }
  }
});