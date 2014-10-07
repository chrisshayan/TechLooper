angular.module("Header").factory("headerService",['$location','$rootScope', function($location, $rootScope) {
	var hash;
    return {
        changeView: function(event) {
            var item = $(event.target).parent().parent().find('i');
            item.removeClass('active');
        	if($(event.target).hasClass('fa-pie-chart')){
        		hash = 'pieChart';
                $(event.target).addClass('active');
        	}else{
        		hash = 'bubbleChart';
                $(event.target).addClass('active');
        	}
        	$location.path(hash);
            $rootScope.$apply();
        },
        showSetting: function(){
            var set = $(".setting-content");
            if(set.hasClass('hideContent')){
                set.removeClass('hideContent').addClass('showContent');
                set.find("ul.setting-items").css("display", "block");
                set.stop().animate({
                    width : "125" // 125
                });
                
            }else{
                set.removeClass('showContent').addClass('hideContent');
                set.find("ul.setting-items").css("display", "none");
                set.stop().animate({
                    width : "28" // 125
                });               
            }           
        }
    }
}]);