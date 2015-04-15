angular.module("Skill").directive("skillcircle", function () {
  return {
    restrict: "E",
    templateUrl: "modules/skill-circle/skill-circle.tem.html",
    scope: {
      viewJson: "="
    },
    link: function(){
      $('.js-footer').addClass('technical-detail');
    }
  }
}).directive("skillchart", function () {
  return {
    restrict: "A",
    replace: false,
    templateUrl: "modules/skill-chart/skill-chart.tem.html"
  }
}).directive("skilltable", function () {
  return {
    restrict: "E",
    replace: false,
    templateUrl: "modules/skill-table/skill-table.tem.html",
    scope: {
      viewJson: "="
    }
  }
});