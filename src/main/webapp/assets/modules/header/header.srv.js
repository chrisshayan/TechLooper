angular.module("Header").factory("headerService",['$location', function($location) {
	var hash;
    return {
        changeView: function(event) {
        	if($(event.target).hasClass('fa-pie-chart')){
        		hash = 'pieChart';
        	}else{
        		hash = 'bubbleChart';
        	}
        	$location.path(hash);
        }
    }
}]);