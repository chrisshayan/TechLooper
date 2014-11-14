angular.module("Skill").factory("skillCircleFactory", function (jsonValue, utils) {
  var circles = [];

  var $$ = {
    clear: function() {
      circles.length = 0;
    },

    draw: function (term, skills, indexFrom) {
      $.each(skills, function (index, skill) {
        var circle = Circles.create({
          id: "circle-" + (index + indexFrom),
          radius: 30,
          value: skill.currentCount,
          maxValue: term.count,
          width: 10,
          text: function (value) {
            return value;
          },
          colors: ["#343233", skill.color],
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

    renderTermBox: function (term) {
      var per = Math.round((term.count * 260) / term.totalTechnicalJobs);
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

      // TODO: find other way to map term's labels
      var rename = utils.mappingData(term.jobTerm);
      if (rename.length > 5) {
        $('.term-infor-chart .number').addClass('small');
      }
      $('.term-infor-chart .number').append(rename);
    }
  }

  utils.registerNotification(jsonValue.notifications.switchScope, $$.clear);

  return {
    renderView: function (term, skills) {
      $$.renderCircles(term, skills);
      $$.renderTermBox(term);

      $('.skill-circle-item').on('click mouseover', function(){
        utils.sendNotification(jsonValue.notifications.mouseHover, $(this).find('.skill-name').text());
      });
    }
  }
});