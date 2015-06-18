techlooper.directive('autoSuggestion', function ($http) {
  return {
    restrict: "A",
    scope: {
      text: "=",
      items: "=",
      inputModel: "=",
      getUrl: "@"
    },
    link: function (scope, element, attr, ctrl) {
      scope.$watch("inputModel", function (newVal, oldVal) {
        //console.log(scope.inputModel);
      });

      scope.$watch("text", function (newVal, oldVal) {
        if (!scope.inputModel.$touched) {
          return;
        }
        delete scope.items;

        $http.get(scope.getUrl + scope.text)
          .success(function (data) {
            scope.items = data.items.map(function (item) {return item.name;});
          });
      });
    }
  }
});