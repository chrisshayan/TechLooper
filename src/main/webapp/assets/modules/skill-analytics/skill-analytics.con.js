angular.module('Skill').controller('skillAnalyticsController',
    function($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory, skillCircleFactory, skillChartFactory, shortcutFactory) {
        connectionFactory.initialize($scope);
        $scope.$on(jsonValue.events.analyticsSkill, function(event, data) {
            var top10 = utils.getTopItems(data.jobSkills, ["currentCount"], 10);
            $scope.term = data;
            $scope.top10 = top10;

            var top3 = utils.getTopItems(data.jobSkills, ["currentCount", "previousCount"], 3);
            $scope.top3 = skillTableFactory.reformatData(top3);

            $scope.$apply();

            // render left circle chart
            skillCircleFactory.renderTermChart(data.totalTechnicalJobs, data.count, data.jobTerm);
            skillCircleFactory.draw(data, top10);

            // render bottom-right table & top-right line-chart
            skillTableFactory.formatDate();
            skillChartFactory.draw(top3);
        });

        connectionFactory.analyticsSkill($routeParams.term);
        //animationFactory.animatePage();  TODO
        
        $('.btn-close').click(function(){shortcutFactory.trigger('esc');});

    });