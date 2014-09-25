app.controller('openCompaniesBar', ['$scope',
    function($scope) {
        var cp = $('.companies-bar'),
            list = $('.companies-list');
        cp.click(function() {
            var ic = $(this).find('i');
            if (ic.hasClass('fa-arrow-up')) {
                ic.removeClass('fa-arrow-up').addClass('fa-arrow-down');
                list.animate({
                    height: '120px',
                    padding: '10px'
                });
            } else {
                ic.removeClass('fa-arrow-down').addClass('fa-arrow-up');
                list.animate({
                    height: '0',
                    padding: '5px 10px'
                });
            }
        });
    }
]);
app.controller("loadCompanies", function($scope, $http) {
    $http.get('data/companies.json').
    success(function(data, status, headers, config) {
        $scope.companies = data;
    }).
    error(function(data, status, headers, config) {
        console.logError("Error in Loading companies.json", status);
    });
});