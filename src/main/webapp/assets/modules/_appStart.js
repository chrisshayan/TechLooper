techlooper.run(function (shortcutFactory, connectionFactory, loadingBoxFactory, cleanupFactory,
                         signInService, historyFactory, userService, routerService, $location,
                         utils, $rootScope, $translate, jsonValue, localStorageService, securityService,
                         apiService, resourcesService, seoService) {
  shortcutFactory.initialize();
  connectionFactory.initialize();
  loadingBoxFactory.initialize();
  cleanupFactory.initialize();
  historyFactory.initialize();
  routerService.initialize();
  userService.initialize();
  securityService.initialize();
  seoService.initialize();

  $rootScope.apiService = apiService;
  $rootScope.resourcesService = resourcesService;

  var doTranslate = function () {
    $translate(["newGradLevel", "experienced", "manager", "timeline", "numberOfJobs", "jobs", "isRequired", "exItSoftware", "ex149",
      "salaryRangeJob", "jobNumber", "salaryRangeInJob", "jobNumberLabel", "allLevel", "newGradLevel", "exHoChiMinh", "exManager",
      "experienced", "manager", "maximum5", "maximum3", "hasExist", "directorAndAbove", "requiredThisField",
      "genderMale", "genderFemale", "exMale", "exYob", 'exDay', 'day', 'week', 'month', "maximum50", "whoJoinAndWhyEx"]).then(function (translate) {
      $rootScope.translate = translate;
    });
  }

  var campaign = $location.search();
  var langKey = (campaign && campaign.lang);
  langKey !== $translate.use() && ($translate.use(langKey));
  $rootScope.$on('$translateChangeSuccess', function () {
    langKey !== $translate.use() && ($translate.use(langKey));
    doTranslate();
  });

  doTranslate();

  $rootScope.jsonValue = jsonValue;

  $('html, body').animate({scrollTop: 0});

  var param = $location.search();
  if (!$.isEmptyObject(param)) {
    switch (param.action) {
      case "registerVnwUser":
        localStorageService.set("lastName", param.lastName);
        localStorageService.set("firstName", param.firstName);
        localStorageService.set("email", param.email);
        break;

      //TODO route user to login by social
      case "loginBySocial":
        securityService.login(param.code, param.social, param.social);
        break;

      case "redirectJA":
        window.location.href = param.targetUrl;
        break;
    }
  }

  $rootScope.today = moment().format(jsonValue.dateFormat);
});