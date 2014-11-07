angular.module("Skill").factory("skillCircleFactory", function (jsonValue) {
  var circles = [];
  var $$ = {
    initialize: function (term, skills) {
      circles.length = 0;
      var colorIndex = -1;
      $.each(skills, function (index, skill) {
        colorIndex = (index >= jsonValue.skillColors.length) ? 0 : colorIndex + 1;
        var circle = Circles.create({
          id: "circle-" + index,
          radius: 30,
          value: skill.currentCount,
          maxValue: term.count,
          width: 10,
          text: function (value) {
            return value;
          },
          colors: ["#343233", jsonValue.skillColors[colorIndex]],
          duration: 1300
        });
        circles.push(circle);
      });
    },

    update: function(term, skills) {
      $.each(circles, function(index, circle) {
        // TODO update skill text & value
        circle.update(skills[index].currentCount);
      });
    },
    percentTerm: function(total, number){
      var per = Math.round((number*260)/total);
      $('.term-infor-chart .percent').animate({
        'height': per
      }, {
        duration: '19000',
        easing: 'easeOutQuad'
      });
      $('.term-infor-chart .number').animate({
        'bottom': per + 15 +'px'
      }, {
        duration: '19000',
        easing: 'easeOutQuad'
      });
      $('.term-infor-chart .arrow-up').animate({
        'bottom': (per - 8) +'px'
      }, {
        duration: '19000',
        easing: 'easeOutQuad'
      });
      $('i.fa-caret-up').show();  
    },
    renameTerm: function(name){
      var newName ='';
      var rename = name.split(" ");
      if(name.length > 6){
        for(var i = 0; i < rename.length; i++){
          newName =  newName + rename[i].charAt(0);
        }
      }else{
        newName = name;
      }
      $('.term-infor-chart .number').append(newName);
    },
    handlingLongSKillName: function(){
      $(".skill-name").dotdotdot({
        height: 23
      });
    }
  }

  return {
    /*
     @param term
     @see src/test/resources/expect/vnw-jobs-count-skill.json
     */
    draw: function (term, skills) {
      if (circles.length === 0) {
        $$.handlingLongSKillName();
        return $$.initialize(term, skills);
      }
      $$.update(term, skills);
    },
    renderTermChart: function(total, number, name){
      $$.percentTerm(total, number);
      $$.renameTerm(name);
    }
  }
});