angular.module('Skill').controller('skillAnalyticsController',
  function($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
    skillCircleFactory, skillChartFactory, shortcutFactory, skillAnalyticsService) {
    $('.loading-data').show();
    utils.sendNotification(jsonValue.notifications.switchScope, $scope);
    $scope.$on(jsonValue.events.analyticsSkill, function (event, data) {
      var viewJson = skillAnalyticsService.extractViewJson(data);
      $scope.viewJson = viewJson;
      skillTableFactory.calculatePercentage(viewJson);
      $scope.$apply();

      skillCircleFactory.renderView(viewJson);
      skillTableFactory.renderView(viewJson);
      skillChartFactory.renderView(viewJson);

        $('.loading-data').hide();
        skillAnalyticsService.registerEvents();
        chartManagement();
    });

    connectionFactory.analyticsSkill($routeParams.term);
    animationFactory.animatePage(); //TODO

    $('.btn-close').click(function() {
        shortcutFactory.trigger('esc');
    });
    $('.btn-logo').click(function() {
        shortcutFactory.trigger('esc');
    });
    function chartManagement(){
      setActiveChartType();
    }
    function setActiveChartType(){
      var type = $('.chart-management ul').find('li');
      type.on('click', function(){
        type.removeClass('active').find('i').removeClass('fa-dot-circle-o');
        if(!$(this).hasClass('active')){
          $(this).addClass('active').find('i').addClass('fa-dot-circle-o');
        }
      });
    }
});
