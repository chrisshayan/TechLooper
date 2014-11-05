angular.module('Skill').factory('skillCircleFactory', function() {
    return {
        drawCircle: function(data) {
            var colors = [
                    ['#ccc', '#4B253A'],
                    ['#ccc', '#EFB917'],
                    ['#ccc', '#45AEEA'],
                    ['#ccc', '#D2D558'],
                    ['#ccc', '#D43A43']
                ];
            $.each(data, function(index, value){
                var child = document.getElementById(value.skill);
                percentage = 31.42 + (index * 9.84),  //% number
                circle = Circles.create({
                    id: child.id,
                    value: percentage,
                    radius: 30,
                    width: 5,
                    colors: colors[index - 1]
                });
            });
        }

    }
});