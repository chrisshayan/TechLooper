var app = angular
    .module('app', [
        'ngRoute'
    ])

app.config(['$routeProvider',
    function($routeProvider) {
        $routeProvider
            .when('/', {
                templateUrl: 'partials/home.html',
                controller: 'PageCtrl'
            })
    }
]);
app.controller('PageCtrl', ['$scope',
    function($scope) {
        $scope.tagline = 'Career Analytics. Open Source. Awesome!';
    }
]);