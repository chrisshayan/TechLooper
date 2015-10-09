techlooper.run(function (shortcutFactory, connectionFactory, loadingBoxFactory, cleanupFactory,
                         signInService, historyFactory, userService, routerService, $location,
                         utils, $rootScope, $translate, jsonValue, localStorageService, securityService,
                         apiService, resourcesService, seoService, joinAnythingService) {
  $rootScope.apiService = apiService;
  $rootScope.resourcesService = resourcesService;
  $rootScope.jsonValue = jsonValue;

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

  $('html, body').animate({scrollTop: 0});

  var param = $location.search();
  if (!$.isEmptyObject(param)) {
    switch (param.action) {
      case "registerVnwUser":
        localStorageService.set("lastName", param.lastName);
        localStorageService.set("firstName", param.firstName);
        localStorageService.set("email", param.email);
        break;

      case "loginBySocial":
        securityService.login(param.code, param.social, param.social);
        break;

      case "redirectJA":
        console.log("redirect");
        window.location.href = param.targetUrl;
        break;

      case "cancel":
        $location.url("/");
        break;
    }
  }

  //$rootScope.$watchCollection([
  //  function () {
  //    return localStorageService.get("lastFoot");
  //  },
  //  function () {
  //    return localStorageService.get("priorFoot");
  //  }
  //], function () {
  //  $rootScope.priorFoot = localStorageService.get("priorFoot");
  //  $rootScope.lastFoot = localStorageService.get("lastFoot");
  //});

  $rootScope.today = moment().format(jsonValue.dateFormat);

  //Exec all services
  shortcutFactory.initialize();
  connectionFactory.initialize();
  loadingBoxFactory.initialize();
  cleanupFactory.initialize();
  historyFactory.initialize();
  routerService.initialize();
  userService.initialize();
  securityService.initialize();
  seoService.initialize();
  joinAnythingService.initialize();

  $rootScope.vnwDomainName = (window.location.host.indexOf("staging") >= 0 ? "staging.vietnamworks.com" : "vietnamworks.com");
});