angular.module('Skill').controller('skillAnalyticsController',
    function($scope, jsonValue, connectionFactory, $routeParams, animationFactory, utils, skillTableFactory,
        skillCircleFactory, skillChartFactory, shortcutFactory) {
        $('.loading-data').show();
        utils.sendNotification(jsonValue.notifications.switchScope, $scope);

        $scope.$on(jsonValue.events.analyticsSkill, function(event, data) {
            var path = "$.histograms[?(@.name=='$PERIOD')].values[$INDEX]";
            var atCurrentCount = path.replace('$PERIOD', jsonValue.histograms.twoWeeks).replace('$INDEX', '1');

            var top10 = utils.getTopItemsAt(data.skills, [atCurrentCount], 10);
            $scope.term = data;
            $scope.top10 = utils.flatMap(top10, [atCurrentCount], ["currentCount"]);

            var atPreviousCount = path.replace('$PERIOD', jsonValue.histograms.twoWeeks).replace('$INDEX', '0');
            var nonZeroSkills = utils.zeroDataFilter(data.skills, [atPreviousCount, atCurrentCount], 3);
            var top3 = utils.getTopItemsAt(nonZeroSkills, [atPreviousCount, atCurrentCount], 3);

            var atHistogramData = "$.histograms[?(@.name=='$PERIOD')].values".replace('$PERIOD', jsonValue.histograms.thirtyDays);
            top3 = utils.flatMap(top3, [atPreviousCount, atCurrentCount, atHistogramData], ["previousCount", "currentCount", "histogramData"]);
            $scope.top3 = skillTableFactory.reformatData(top3);
            $scope.$apply();

            // render left circle chart
            skillCircleFactory.renderTermChart(data.totalTechnicalJobs, data.count, data.jobTerm);
            skillCircleFactory.draw(data, $scope.top10);
            skillCircleFactory.highLightSkill();
            // render bottom-right table & top-right line-chart
            skillTableFactory.formatDate();
            skillChartFactory.draw(top3);
            $('.loading-data').hide();
        });

        connectionFactory.analyticsSkill($routeParams.term);
        animationFactory.animatePage(); //TODO

        $('.btn-close').click(function() {
            shortcutFactory.trigger('esc');
        });
        $('.btn-logo').click(function() {
            shortcutFactory.trigger('esc');
        });
    });
