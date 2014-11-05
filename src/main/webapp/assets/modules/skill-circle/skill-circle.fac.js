angular.module('Skill').factory('skillCircleFactory', function() {
    return {
        drawCircle: function(data) {
            var colors = [
                    ['#D3B6C6', '#4B253A'],
                    ['#FCE6A4', '#EFB917'],
                    ['#BEE3F7', '#45AEEA'],
                    ['#F8F9B6', '#D2D558'],
                    ['#F4BCBF', '#D43A43']
                ];

            // for (var i = 1; i <= data.length; i++) {
            //     var child = document.getElementById(data[i].skill),
            //         percentage = 31.42 + (i * 9.84),  //% number
            //         circle = Circles.create({
            //             id: child.id,
            //             value: percentage,
            //             radius: 30,
            //             width: 5,
            //             colors: colors[i - 1]
            //         });
            // }
            $.each(data, function(index, value){
                var child = document.getElementById(value);
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