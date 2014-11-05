angular.module("Skill").factory("skillCircleFactory", function (jsonValue) {
  var circles = [];
  var $$ = {
    initialize: function (term, skills) {
      circles.length = 0;
      var colorIndex = -1;
      $.each(skills, function (index, skill) {
        colorIndex = (index >= jsonValue.skillColors.length) ? 0 : colorIndex + 1;
        var circle = Circles.create({
          id: "circle-" + skill.skill,
          radius: 30,
          value: skill.currentCount,
          maxValue: term.count,
          width: 5,
          text: function (value) {
            return value;
          },
          colors: ["#D3B6C6", jsonValue.skillColors[colorIndex]],
          duration: 400
        });
        circles.push(circle);
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
        return $$.initialize(term, skills);
      }

      //update
    }

  }
});