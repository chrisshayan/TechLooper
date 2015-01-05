angular.module("Common").factory("tourService", function (jsonValue, utils) {
  var tour = {};

  var options = {template: jsonValue.introTour.template}
  //tour[jsonValue.views.jobsSearch] = new Tour($.extend(options, {steps: jsonValue.introTour.searchForm}));
  //tour[jsonValue.views.analyticsSkill] = new Tour($.extend(options, {steps: jsonValue.introTour.technicalDetail}));
  tour[jsonValue.views.bubbleChart] = new Tour($.extend(options, {steps: jsonValue.introTour.bubbleHomePage}));
  tour[jsonValue.views.pieChart] = new Tour($.extend(options, {steps: jsonValue.introTour.pieHomePage}));
  //tour[jsonValue.views.signIn] = new Tour($.extend(options, {steps: jsonValue.introTour.signIn}));

  var currentTour;
  var instance = {
    initialize: function () {},

    endTourGuide: function () {
      if (currentTour !== undefined && !currentTour.ended()) {
        currentTour.end();
      }
    },

    makeTourGuide: function (restart) {
      currentTour = tour[utils.getView()];
      if (currentTour !== undefined) {
        currentTour.init();
        if (restart) {return currentTour.restart();}
        else {currentTour.start();}
      }
    },

    restart: function () {
      instance.makeTourGuide(true);
    }
  }

  //utils.registerNotification(jsonValue.notifications.changeUrl, function (currentPath, nextPath) {
  //  //console.log(123);
  //  instance.endTourGuide();
  //});

  utils.registerNotification(jsonValue.notifications.gotData, function () {
    instance.makeTourGuide();
  });

  //$('body').on('click', function (e) {
  //  var container = $(".tour");
  //  if (container.is(":visible") && !container.is(e.target) && container.has(e.target).length === 0) {
  //    instance.endTourGuide();
  //  }
  //});

  return instance;
});