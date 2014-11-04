angular.module('Skill').factory('skillFactory', function(){

	start();
    function start() {
       skillChart($('.skill-item-block'))
        .label("RADIAL 1")
        .diameter(150)
        .value(78)
        .render();
    }

});