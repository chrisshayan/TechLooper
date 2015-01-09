angular.module("Skill").factory("skillCircleFactory", function (jsonValue, utils) {
  var circles = [];

  var $$ = {
    clear: function () {
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

      if ($('span.termLabel').width() > $('span.termLabel').parent().width()) {
        $('.term-infor-chart .number').addClass('small');
      }
    },

    hoverTermUseful: function () {
      $('.term-infor').on('click mouseover', function () {
        $(this).find('.v-mobile').show();
      }).mouseleave(function () {
        $(this).find('.v-mobile').hide();
      });
    }
  }

  utils.registerNotification(jsonValue.notifications.switchScope, $$.clear);

  return {
    renderView: function (viewJson) {
      $$.renderCircles(viewJson);
      $$.renderTermBox(viewJson);
      $('.skill-circle-item').on('click mouseover', function () {
        utils.sendNotification(jsonValue.notifications.mouseHover, $(this).find('.skill-name').text());
        $(this).find('.skill-useful-links').show();
      }).mouseleave(function () {
        $(this).find('.skill-useful-links').hide();
      });
      if ($(window).width() < 768) {
        $('.term-infor .term-useful-links').addClass('v-mobile');
      }
      $(window).resize(function () {
        var w = $(window).width();
        if (w > 767) {
          $('.term-infor .term-useful-links').removeClass('v-mobile');
        }
        else {
          $('.term-infor .term-useful-links').addClass('v-mobile');
        }
      });

      $$.hoverTermUseful();
    }
  }
});