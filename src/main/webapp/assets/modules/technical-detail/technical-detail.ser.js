techlooper.factory("technicalDetailService", function (utils, $translate, jsonValue, $rootScope) {
  var trendSkillChart = {};
  var fnColor = d3.scale.category10();

  var instance = {

    /**
     * @param {object} skill - Skill object
     * @see skill-level-analytics.json
     */
    showSkillsList: function (skill, maxValue) {
      skill.colors = [];
      skill.colors.unshift(fnColor(skill.id));
      skill.colors.unshift("#e9e8e7");

      Circles.create({
        id: 'circles-' + skill.id,
        radius: 60,
        value: skill.totalJob,
        maxValue: maxValue,
        width: 5,
        text: function (value) {return value;},
        colors: skill.colors,
        duration: 400,
        wrpClass: 'circles-wrp',
        textClass: 'circles-text',
        styleWrapper: true,
        styleText: true
      });
    },

    /**
     * Return Trend skill Line chart configuration
     *
     * @param {object} termStatistic - Term Statistic object
     * @see skill-level-analytics.json
     *
     * @return {object} chartConfig - Trend skill chart configuration
     */
    prepareTrendSkills: function (termStatistic) {
      var chartConfig = {};

      var series = [];
      var yMax = 0;
      var yMin = termStatistic.skills[0].histograms[0].values[0];
      var colors = [];
      var fnColor = d3.scale.category10();
      $.each(termStatistic.skills, function (i, skill) {
        series.push({name: skill.skillName, data: skill.histograms[0].values});
        yMax = Math.max(yMax, skill.histograms[0].values.max());
        yMin = Math.min(yMin, skill.histograms[0].values.min());
        colors.push(fnColor(skill.skillName));
      });

      var labels = [];
      var period = utils.getHistogramPeriod(termStatistic.skills[0].histograms[0].name);
      $.each(termStatistic.skills[0].histograms[0].values, function (i, item) {
        labels.unshift(utils.formatDateByPeriod((i * period).days().ago(), "oneYear"));
      });

      chartConfig = {
        series: series,
        colors: colors,
        yAxis: {min: yMin, max: yMax},
        xAxis: {labels: labels}
      }
      return chartConfig;
    },

    generateTrendSkillsChartOptions: function () {
      var translate = $rootScope.translate;
      return {
        chart: {
          renderTo: 'trendSkills',
          type: 'spline'
        },
        colors: trendSkillChart.config.colors,
        title: {
          text: ''
        },
        subtitle: {
          text: ''
        },
        xAxis: {
          categories: trendSkillChart.config.xAxis.labels,
          gridLineColor: '#353233',
          labels: {
            style: {
              color: '#8a8a8a'
            }
          },
          title: {
            text: translate.timeline
          },
          tickInterval: 1,
          tickmarkPlacement: 'on',
          gridLineWidth: 1
        },
        yAxis: {
          labels: {
            formatter: function () {
              return this.value;
            }
          },
          title: {
            text: translate.numberOfJobs
          },
          min: trendSkillChart.config.yAxis.min,
          max: trendSkillChart.config.yAxis.max,
          tickInterval: 5
        },
        tooltip: {
          valueSuffix: ' ' + translate.jobs
        },
        legend: {
          layout: 'horizontal',
          align: 'center',
          verticalAlign: 'top',
          borderWidth: 0,
          itemStyle: {
            color: '#636363'
          },
          itemHoverStyle: {
            color: '#E0E0E3'
          },
          itemHiddenStyle: {
            color: '#606063'
          }
        },
        plotOptions: {
          series: {
            marker: {
              enabled: false
            },
            lineWidth: 1,
            states: {
              hover: {
                lineWidth: 3
              }
            }
          }
        },
        series: trendSkillChart.config.series,
        credits: {//disable Highchart.com text
          enabled: false
        }
      }
    },

    createTrendSkillsChart: function () {
      return new Highcharts.Chart(instance.generateTrendSkillsChartOptions());
    },

    /**
     * @param {object} termStatistic - Term Statistic object @see skill-level-analytics.json
     */
    trendSkills: function (termStatistic) {
      trendSkillChart.config = instance.prepareTrendSkills(termStatistic);
      trendSkillChart.instance && trendSkillChart.instance.destroy();
      return instance.hasSkillValues(termStatistic) && (trendSkillChart.instance = instance.createTrendSkillsChart());
    },

    hasSkillValues: function (termStatistic) {
      var values = "";
      $.each(termStatistic.skills, function (i, skill) {values += skill.histograms[0].values.join("");});
      return values.replace(/0/g, "").length > 0;
    },

    enableNotifications: function () {
      return utils.getView() === jsonValue.views.analyticsSkill;
    },

    skillManager: function(){
      instance.removeSkill();
      instance.removeAllSKills();
      instance.addSkillButton();
      instance.addSkillEnter();
    },

    removeSkill: function(){
      var listSkill = $('.added-list-skills').find('.close');
      listSkill.on('click', function(){
        $(this).parent().parent().parent().remove();
        var flg = instance.updateNumberSkill();
        if( flg == 4){
          $('.max-skill-alert').html('');
        }
      });
    },
    removeAllSKills: function(){
      $('p.removeAll').click(function(){
        $('.added-list-skills').find('ul').html('');
      });
    },
    updateNumberSkill: function(){
      var numberSkill = $('.added-list-skills').find('li').length;
      return numberSkill;
    },
    addSkills: function(){
      var flgNumber = instance.updateNumberSkill(),
          skillName = $('.add-skill-input').find('input').val();
      var exist = instance.checkExistSkill(skillName);
      if($.trim(skillName) !== ''){
        if(flgNumber < 5){
            if(exist == 1){
              $('.max-skill-alert').html('This skill has already');
            }else{
              $('.added-list-skills').find('ul').append('<li><span class="left"><span class="right"><i>'+ skillName +'</i><span class="close" title="Remove">x</span></span></span></li>');
              $('.add-skill-input').find('input').val('');
              $('.max-skill-alert').html('');
              instance.removeSkill();
            }
          }else{
          $('.max-skill-alert').html('Maximum 5 skills');
        }
      }
    },
    checkExistSkill: function(skillName){
      var listSkill = $('.added-list-skills').find('i'),
          exist = 0;
      listSkill.each(function(){
        if($(this).text().toUpperCase() == skillName.toUpperCase()){
          exist = 1;
        }
      });
      return exist;
    },
    addSkillButton: function(){
      $('.add-skill-input').find('button').click(function(){
        instance.addSkills();
      });
    },
    addSkillEnter: function(){
      $('.add-skill-input').find('input').keypress(function(event){
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
          instance.addSkills();
        }
      });
    }
  };

  return instance;
});