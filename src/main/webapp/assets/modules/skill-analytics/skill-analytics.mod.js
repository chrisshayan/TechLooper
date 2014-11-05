angular.module("Skill").directive("skillcircle", function (skillCircleFactory) {
  return {
    restrict: "A",
    replace: false,
    templateUrl: "modules/skill-circle/skill-circle.tem.html"
  }
}).directive("skillchart", function () {
  return {
    restrict: "A",
    replace: false,
    templateUrl: "modules/skill-chart/skill-chart.tem.html"
  }
}).directive("skilltable", function () {
  return {
    restrict: "A",
    replace: false,
    templateUrl: "modules/skill-table/skill-table.tem.html"
  }
});