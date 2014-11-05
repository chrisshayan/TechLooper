angular.module('Skill').controller('skillCircleController', 
	function ($scope, skillCircleFactory) {
    var data = {
	    "jobTerm": "java",
	    "count": "200",
	    "period": "week",
	    "jobSkills": [
		    {
		      "skill": "Spring",
		      "currentCount": "60",
		      "previousCount": "120"
		    },
		    {
		      "skill": "Maven",
		      "currentCount": "60",
		      "previousCount": "10"
		    },
		    {
		      "skill": "Sprint",
		      "currentCount": "180",
		      "previousCount": "120"
		    },
		    {
		      "skill": "ABC",
		      "currentCount": "110",
		      "previousCount": "160"
		    },
		    {
		      "skill": "ddd",
		      "currentCount": "110",
		      "previousCount": "160"
		    }
	    ]
	};
	$scope.items = data.jobSkills;

	$scope.$on('loadCircleChart', function(ngRepeatFinishedEvent) {
        skillCircleFactory.drawCircle($scope.items);
    });
});