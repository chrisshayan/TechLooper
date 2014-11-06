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
          radius: 40,
          value: skill.currentCount,
          maxValue: term.count,
          width: 13,
          text: function (value) {
            return value;
          },
          colors: ["#343233", jsonValue.skillColors[colorIndex]],
          duration: 400
        });
        circles.push(circle);
      });
    },

    update: function(term, skills) {
      $.each(circles, function(index, circle) {
        // TODO update skill text & value
        circle.update(skills[index].currentCount);
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
      $$.update(term, skills);
    }

  }
});