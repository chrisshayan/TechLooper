angular.module("Skill").factory("skillCircleFactory", function (jsonValue, utils) {
  var circles = [];

  var $$ = {
    clear: function() {
      circles.length = 0;
    },

    draw: function (viewJson, newSkills, indexFrom) {
      $.each(newSkills, function (index, skill) {
        var circle = Circles.create({
          id: "circle-" + (index + indexFrom),
          radius: 30,
          value: skill.currentCount,
          maxValue: viewJson.count,
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

    renderCircles: function (viewJson) {
      $.each(circles, function (index, circle) {
        circle.update(viewJson.circles[index].currentCount);
      });
      var newSkills = viewJson.circles.slice(circles.length);
      $$.draw(viewJson, newSkills, circles.length);
    },

    renderTermBox: function (viewJson) {
      var per = Math.round((viewJson.count * 260) / viewJson.totalTechnicalJobs);
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
      var rename = utils.mappingData(viewJson.jobTerm);
      if (rename.length > 5) {
        $('.term-infor-chart .number').addClass('small');
      }
      $('.term-infor-chart .number').append(rename);
    }
  }

  utils.registerNotification(jsonValue.notifications.switchScope, $$.clear);

  return {
    renderView: function (viewJson) {
      $$.renderCircles(viewJson);
      $$.renderTermBox(viewJson);

      $('.skill-circle-item').on('click mouseover', function(){
        utils.sendNotification(jsonValue.notifications.mouseHover, $(this).find('.skill-name').text());
      });
    }
  }
});