angular.module("Header",["Common"]).factory("headerService",['jsonValue', function($sonValue) {
	var chart;
    return {
        changeChart: function(event) {
            var item = $(event.target).parent().parent().find('i');
            item.removeClass('active');
        	if($(event.target).hasClass('fa-pie-chart')){
        		chart = jsonValue.pie;
                $(event.target).addClass('active');
        	}else{
        		chart = jsonValue.bubble;
                $(event.target).addClass('active');
        	}
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
            return chart;
        },
        setChart: function($chart) {
            chart = $chart;
        }
    }
}]);