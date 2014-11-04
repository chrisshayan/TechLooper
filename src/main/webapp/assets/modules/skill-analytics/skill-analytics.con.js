angular.module("Skill").controller("skillAnalyticsController", function ($scope, jsonValue, connectionFactory) {
  connectionFactory.initialize($scope);



  connectionFactory.analyticsByTerm();
});
