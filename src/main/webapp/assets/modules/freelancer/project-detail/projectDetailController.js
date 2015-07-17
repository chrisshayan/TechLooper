techlooper.controller('freelancerProjectDetailController', function ($scope, utils, $location, $routeParams, apiService, $filter) {
  var parts = $routeParams.id.split("-");
  var lastPart = parts.pop();
  if (parts.length < 2 || (lastPart !== "id")) {
    return $location.path("/");
  }

  var projectId = parts.pop();
  var title = parts.join("");
  if (utils.hasNonAsciiChar(title)) {
    title = utils.toAscii(title);
    return $location.url(sprintf("/freelancer/project-detail/%s-%s-id", title, projectId));
  }

  apiService.getProject(projectId).success(function (data) {
    $scope.project = data;
    //$filter("progress")($scope.contestDetail, "challenge");
  });

});

