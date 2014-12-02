angular.module("Common").factory("bootstrapTourFactory", function () {

    return {
        makeTourGuide: function () {
            var tour = new Tour({
                steps: [
                    {
                        element: "#setting-label",
                        placement: "right",
                        title: "#1",
                        content: "You can change chart style (Bubble/Pie) or language here."
                    },
                    {
                        element: "#box",
                        placement: "right",
                        title: "#2",
                        content: "This is the bubble chart which shows the number of jobs for each technology."
                    }
                ]
            });
            tour.init();
            tour.start();
        }
    };
});