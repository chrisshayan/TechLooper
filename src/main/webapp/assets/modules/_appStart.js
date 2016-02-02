techlooper.run(function (connectionFactory, loadingBoxFactory, cleanupFactory, userService, $location,
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
      case "cancel-social-register":
        localStorageService.set("email", param.email);
      case "registerVnwUser":
        if (/^.+@\w+(\.\w+)+$/.test(param.email)) {
          localStorageService.set("lastName", param.lastName);
          localStorageService.set("firstName", param.firstName);
          localStorageService.set("email", param.email);
        }
        else {
          localStorageService.remove("lastName");
          localStorageService.remove("firstName");
          localStorageService.remove("email");
        }
        break;

      case "loginBySocial":
        securityService.login(param.code, param.social, param.social);
        break;

      case "employerLogin":
        localStorageService.set("employerLogin", true);
        //if (!$rootScope.userInfo) $location.path("/user-type");
        break;

      case "redirectJA":
        window.location.href = (param.targetUrl.startsWith("http://") || param.targetUrl.startsWith("https://")) ? param.targetUrl : "http://" + param.targetUrl;
        break;
    }
  }
  else {//remove all operation flags
    localStorageService.remove("joiningChallengeId");
    localStorageService.remove("joinNow");
    //localStorageService.remove("submitNow");
    localStorageService.remove("internalEmail");
    localStorageService.remove("passcode");
    localStorageService.remove("failedJoinChallenge");
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
  connectionFactory.initialize();
  loadingBoxFactory.initialize();
  cleanupFactory.initialize();
  userService.initialize();
  securityService.initialize();
  seoService.initialize();
  joinAnythingService.initialize();

  $rootScope.vnwDomainName = (window.location.host.indexOf("staging") >= 0 ? "staging.vietnamworks.com" : "vietnamworks.com");
});