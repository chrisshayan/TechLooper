techlooper.factory("technicalDetailService", function () {
  var instance = {
    showSkills: function(){
      var myCircle = Circles.create({
        id:           'circles-1',
        radius:       60,
        value:        43,
        maxValue:     100,
        width:        10,
        text:         function(value){return value + '%';},
        colors:       ['#D3B6C6', '#4B253A'],
        duration:       400,
        wrpClass:     'circles-wrp',
        textClass:      'circles-text',
        styleWrapper: true,
        styleText:    true
      });
    }
  };
  return instance;

});