techlooper.filter("jobseekerDashboard", function ($filter, $translate) {
  return function (input, type) {
    if (!input || input.$isRich) return input;

    var dashboard = input;

    _.each(dashboard.challenges, function(challenge) {
      $filter("jobseekerDashboardChallenge")(challenge);
    });

    dashboard.$browserLang = $translate.use();
    dashboard.$isRich = true;
    return dashboard;
  }
});