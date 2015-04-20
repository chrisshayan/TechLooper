techlooper.factory("technicalDetailService", function () {
  var instance = {
    showSkillsList: function(){
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
  },
    trendSkills: function(){

    }
  };
  return instance;

});