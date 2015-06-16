techlooper.directive('autoTagbox', function ($timeout) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/auto-tagbox.tem.html",
    scope: {
      tags: "=",
      config: "=",
      suggestionTags: "=",
      translateSuggestionTitle: "@"
    },
    link: function (scope, element, attr, ctrl) {
      //scope.suggestionTags = scope.suggestionTags || [];// [{title: "spring"}, {title: "angularjs"}];
      scope.errors = [];

      scope.removeTag = function (tag) {
        scope.tags.splice(scope.tags.indexOf(tag), 1);
        $.each(scope.suggestionTags, function(i, item) {
          if (item.title.toLowerCase() === tag) {
            delete item.added;
            return false;
          }
        });
        scope.errors.length = 0;
      }

      scope.tags = scope.tags || [];

      scope.addTag = function (tag) {
        var newTag = (tag && tag.title) || scope.config.newTag;
        if (!newTag || !newTag.length) {
          return;
        }
        scope.errors.length = 0;

        var lowerTag = newTag.toLowerCase();
        if (scope.tags.length >= 50) {
          return scope.errors.push("maximum50");
        }
        else if (scope.tags.indexOf(lowerTag) > -1) {
          return scope.errors.push("hasExist");
        }
        else if (newTag.length > 40) {
          return scope.errors.push("tooLong");
        }
        scope.tags.push(lowerTag);

        tag && (tag.added = true);
        tag || (scope.config.newTag = "");
      }

      scope.$on("state change success", function () {scope.errors.length = 0;});
    }
  }
});