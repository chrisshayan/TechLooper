techlooper.directive("technicalDetail", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/technical-detail/technical-detail.tem.html"
  }
}).directive("technicalInfo", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/technical-detail/technical-info.tem.html"
  }
}).directive("technicalSkills", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/technical-detail/technical-skills.tem.html",
    link: function(){
      for(var i = 0; i< 5 ; i++){
        var myCircle = Circles.create({
          id:           'circles-'+i,
          radius:       60,
          value:        43,
          maxValue:     100,
          width:        10,
          text:         function(value){return value;},
          colors:       ['#D3B6C6', '#4B253A'],
          duration:       400,
          wrpClass:     'circles-wrp',
          textClass:      'circles-text',
          styleWrapper: true,
          styleText:    true
        });
      }
    }
  }
}).directive("trendSkills", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/technical-detail/trend-skills.tem.html"
  }
}).directive("companiesList", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/technical-detail/companies-list.tem.html"
  }
}).directive("careerAlert", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/technical-detail/career-alert.tem.html"
  }
});