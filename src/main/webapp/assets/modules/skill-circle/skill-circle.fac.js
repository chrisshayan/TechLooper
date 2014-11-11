angular.module("Skill").factory("skillCircleFactory", function (jsonValue, utils) {
  var circles = [];
  var $$ = {
    clear: function() {
      circles.length = 0;
    },

    draw: function (term, skills, indexFrom) {
      var colorIndex = -1;
      $.each(skills, function (index, skill) {
        colorIndex = (index + indexFrom >= jsonValue.skillColors.length) ? 0 : colorIndex + 1;
        var circle = Circles.create({
          id: "circle-" + (index + indexFrom),
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

    renderCircles: function (term, skills) {
      $.each(circles, function (index, circle) {
        circle.update(skills[index].currentCount);
      });
      var newSkills = skills.slice(circles.length);
      $$.draw(term, newSkills, circles.length);
    },

    percentTerm: function (total, number) {
      var per = Math.round((number * 260) / total);
      $('.term-infor-chart .percent').animate({
        'height': per
      }, {
        duration: '19000',
        easing: 'easeOutQuad'
      });
      $('.term-infor-chart .number').animate({
        'bottom': per + 15 + 'px'
      }, {
        duration: '19000',
        easing: 'easeOutQuad'
      });
      $('.term-infor-chart .arrow-up').animate({
        'bottom': (per - 8) + 'px'
      }, {
        duration: '19000',
        easing: 'easeOutQuad'
      });
      $('i.fa-caret-up').show();
    },

    renameTerm: function (name) {
      var newName = '';
      var rename = name.split("_");
      if (name.length > 5) {
        for (var i = 0; i < rename.length; i++) {
          newName = newName + ' ' + rename[i];
        }
        $('.term-infor-chart .number').addClass('small');
      }
      else {
        newName = name;
      }
      $('.term-infor-chart .number').append(newName);
    }
  }

  utils.registerNotification(jsonValue.notifications.changeView, $$.clear);

  return {

    /*
     @param term
     @see src/test/resources/expect/vnw-jobs-count-skill.json
     */
    draw: function (term, skills) {
      $$.renderCircles(term, skills);
    },

    renderTermChart: function (total, number, name) {
      $$.percentTerm(total, number);
      $$.renameTerm(name);
    }
  }
});