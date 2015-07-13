techlooper.controller('freelancerPostProjectController', function ($scope, jsonValue) {
  $('.field-content').find('[data-toggle="tooltip"]').tooltip({
    html: true,
    placement: 'right',
    'trigger': "focus",
    animation: true,
    html: true
  });

  //$scope.abc = function() {
  //  console.log($scope.postProjectForm);
  //}case "ex-start-date":
  //return moment().add(7, 'day').format('DD/MM/YYYY');

  $scope.status = function(type) {
    switch (type) {
      case "ex-today":
        return moment().add(4, 'weeks').format(jsonValue.dateFormat);

      //case "estimated-end-date-in-4w":
      //  if (!$scope.postProject.estimatedEndDate) return true;
      //  var lastDate = moment().add(4, 'weeks');
      //  var startDate = moment($scope.postProject.estimatedEndDate, jsonValue.dateFormat);
      //  return startDate.isBetween(moment(), lastDate, 'day') || startDate.isSame(moment(), "day");
    }
  }

});
