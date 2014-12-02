var tour = new Tour({
    steps: [
        {
            element: "#logo",
            placement: "bottom",
            title: "Welcome to Bootstrap Tour!",
            content: "Introduce new users to your product by walking them through it step by step."
        }, {
            element: "#box",
            placement: "right",
            title: "A super simple setup",
            content: "Easy is better, right? The tour is up and running with just a few options and steps."
        }, {
            element: "#setting-label",
            placement: "top",
            title: "Best of all, it's free!",
            content: "Yeah! Free as in beer... or speech. Use and abuse, but don't forget to contribute!"
        }
    ]
}).init();

tour.start();