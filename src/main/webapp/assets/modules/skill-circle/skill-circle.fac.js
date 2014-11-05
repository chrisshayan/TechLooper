angular.module('Skill').factory('skillCircleFactory', function() {
	return {
        drawCircle: function() {
            var colors = [
                    ['#D3B6C6', '#4B253A'],
                    ['#FCE6A4', '#EFB917'],
                    ['#BEE3F7', '#45AEEA'],
                    ['#F8F9B6', '#D2D558'],
                    ['#F4BCBF', '#D43A43']
                ];

            for (var i = 1; i <= 5; i++) {
                var child = document.getElementById('circles-' + i),
                    percentage = 31.42 + (i * 9.84);

                Circles.create({
                    id: child.id,
                    value: percentage,
                    radius: 60,
                    width: 10,
                    colors: colors[i - 1]
                })
            }
        }
    }
});