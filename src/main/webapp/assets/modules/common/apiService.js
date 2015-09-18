techlooper.factory("apiService", function ($rootScope, $location, jsonValue, $http, localStorageService, utils) {
  var instance = {

    login: function (techlooperKey) {
      return $http.post("login",
        $.param(techlooperKey), {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}});
    },

    /**
     * Get current login user info
     * */
    getCurrentUser: function (type) {
      //switch (type) {
      //  case "social":
      //    return $http.get("user/current");
      //
      //  default:
      //    return $http.get("user/vnw-current");
      //}
      return $http.get("user/current");
    },

    logout: function () {
      return $http.get("logout");
    },

    getFBLoginUrl: function () {
      return $http.get("social/FACEBOOK_REGISTER/loginUrl", {transformResponse: function (d, h) {return d;}});
    },

    getSocialLoginUrl: function (provider) {
      return $http.get("social/" + provider + "/loginUrl", {transformResponse: function (d, h) {return d;}});
    },

    getContestDetail: function (id) {
      return $http.get("challenge/" + id);
    },

    joinContest: function (contestId, firstName, lastName, registrantEmail, lang) {
      return $http.post("challenge/join",
        {
          challengeId: contestId,
          registrantFirstName: firstName,
          registrantLastName: lastName,
          registrantEmail: registrantEmail,
          lang: lang
        },
        {transformResponse: function (d, h) {return d;}});
    },

    searchContests: function () {
      return $http.get("challenge/list");
    },

    getSuggestSkills: function (text) {
      return $http.get("suggestion/skills/" + text);
    },

    postFreelancerProject: function (projectRequest) {
      return $http.post("project/post", projectRequest)
    },

    getProject: function (id) {
      return $http.get("project/" + id);
    },

    getProjects: function () {
      return $http.get("project/list");
    },

    joinNowByFB: function () {
      $('.loading-data').css("height", $(window).height());
      $('body').addClass('noscroll');
      utils.sendNotification(jsonValue.notifications.loading);
      instance.getFBLoginUrl().success(function (url) {
        localStorageService.set("joinNow", true);
        window.location = url;
      });
    },

    joinProject: function (projectId, firstName, lastName, email, phoneNumber, resumeLink, lang) {
      if (!resumeLink.startsWith("http")) {
        resumeLink = "http://" + resumeLink;
      }
      return $http.post("project/join",
        {
          projectId: projectId, registrantFirstName: firstName, registrantLastName: lastName, registrantEmail: email,
          registrantPhoneNumber: phoneNumber, resumeLink: resumeLink, lang: lang
        },
        {transformResponse: function (d, h) {return d;}});
    },

    /**
     * @see com.techlooper.controller.ProjectController.getProjectStatistic
     * */
    getProjectStatistic: function () {
      return $http.get("project/stats");
    },

    /**
     * @see com.techlooper.controller.ChallengeController.getChallengeStatistics
     * */
    getChallengeStatistic: function () {
      return $http.get("challenge/stats");
    },

    getPersonalHomepage: function () {
      return $http.get("personalHomepage");
    },

    getEmployerDashboardInfo: function () {
      return $http.get("user/employer/dashboard-info");
    },

    /**
     * @see com.techlooper.controller.JobAlertController.getCompany
     * */
    createTechlooperJobAlert: function (email, keyword, location, locationId, lang) {
      return $http.post("jobAlert/register", {
        email: email, keyword: keyword, location: location, locationId: locationId, lang: lang
      });
    },

    listAllJobs: function () {
      return $http.post("jobListing", {
        keyword: null, location: null, page: 1
      });
    },

    filterJob: function (keyword, location, page) {
      return $http.post("jobListing", {
        keyword: keyword, location: location, page: page
      });
    },

    getJobAlertCriteria: function (jobAlertRegistrationId) {
      return $http.get("jobAlertCriteria/" + jobAlertRegistrationId);
    },

    createWebinar: function (webinar) {
      var obj = angular.copy(webinar);
      var atts = [];
      $.each(webinar.attendees, function (i, attendee) {
        atts.push({email: attendee});
      });
      obj.attendees = atts;
      return $http.post("user/employer/webinar", obj);
    },

    /**
     * @see com.techlooper.controller.UserController.findAvailableWebinars
     * */
    findAvailableWebinars: function () {
      return $http.get("user/webinars");
    },

    /**
     * @see com.techlooper.controller.UserController.findWebinarById
     * */
    findWebinarById: function (id) {
      return $http.get("user/webinar/" + id);
    },

    /**
     * @see com.techlooper.controller.UserController.joinWebinar
     * */
    joinWebinar: function (webinarId, firstName, lastName, email) {
      return $http.post("user/webinar/join",
        {id: webinarId, firstName: firstName, lastName: lastName, email: email});
    },

    getlatestTopics: function () {
      return $http.get("forum/latestTopic");
    },

    /**
     * @see com.techlooper.controller.ChallengeController.deleteChallengeById
     * */
    deleteChallengeById: function(id) {
      return $http.delete("challenge/" + id);
    },

    /**
     * @see com.techlooper.controller.ChallengeController.findChallengeById
     * */
    findChallengeById: function(id) {
      return $http.get("challenges/" + id);
    }
  }

  return instance;
});