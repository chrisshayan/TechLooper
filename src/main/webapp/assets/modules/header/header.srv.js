angular.module("Header").factory("headerService",['jsonValue', '$rootScope', function(jsonValue, $rootScope) {
	var chartStyle;
    return {
        changeChart: function(event) {
            var item = $(event.target).parent().parent().find('i');
            item.removeClass('active');
        	if($(event.target).hasClass('fa-pie-chart')){
        		chartStyle = jsonValue.charts.pie;
                $(event.target).addClass('active');
        	}else{
        		chartStyle = jsonValue.charts.bubble;
                $(event.target).addClass('active');
        	}

            $rootScope.$emit(jsonValue.events.changeChart, chartStyle);
        },
        showSetting: function(){
            var set = $(".setting-content");
            if(set.hasClass('hideContent')){
                set.removeClass('hideContent').addClass('showContent');
                set.find("ul.setting-items").css("display", "block");
                set.stop().animate({
                    width : "125"
                });
                
            }else{
                set.removeClass('showContent').addClass('hideContent');
                set.find("ul.setting-items").css("display", "none");
                set.stop().animate({
                    width : "28"
                });               
            }           
        },
        getChart: function(){
            return chartStyle;
        },
        setChart: function($chart) {
            chartStyle = $chart;
        }
    }
}]);