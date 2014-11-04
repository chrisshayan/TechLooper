angular.module('Skill').factory('skillFactory', function(){
	return {
		drawCircle: function($scope) {
        skillChart($('.skill-item-container'))
        .label("Java")
        .diameter(150)
        .value(78)
        .render();
	    }
	}
});