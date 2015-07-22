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
      switch (type) {
        default:
          return $http.get("user/vnw-current");
      }
    },

    logout: function () {
      return $http.get("logout");
    },

    getFBLoginUrl: function() {
      return $http.get("social/FACEBOOK_REGISTER/loginUrl", {transformResponse: function (d, h) {return d;}});
    },

    getContestDetail: function(id) {
      return $http.get("challenge/" + id);
    },

    joinContest: function(contestId, firstName, lastName , registrantEmail, lang) {
      return $http.post("challenge/join",
        {challengeId: contestId, registrantFirstName: firstName, registrantLastName: lastName, registrantEmail: registrantEmail, lang: lang},
        {transformResponse: function (d, h) {return d;}});
    },

    searchContests: function() {
      return $http.get("challenge/list");
    },

    getSuggestSkills: function(text) {
      return $http.get("suggestion/skills/" + text);
    },

    postFreelancerProject: function(projectRequest) {
      return $http.post("project/post", projectRequest, {transformResponse: function (d, h) {return d;}})
    },

    getProject: function(id) {
      return $http.get("project/" + id);
    },

    getProjects: function() {
      return $http.get("project/list");
    },

    joinNowByFB: function() {
      localStorageService.set("lastFoot", $location.url());
      utils.sendNotification(jsonValue.notifications.loading);
      instance.getFBLoginUrl().success(function (url) {
        localStorageService.set("lastFoot", $location.url());
        localStorageService.set("joinNow", true);
        window.location = url;
      });
    },

    joinProject: function(projectId, firstName, lastName, email, phoneNumber, resumeLink, lang) {
      if (!resumeLink.startsWith("http")) {
        resumeLink = "http://" + resumeLink;
      }
      return $http.post("project/join",
        {projectId: projectId, registrantFirstName: firstName, registrantLastName: lastName, registrantEmail: email,
          registrantPhoneNumber: phoneNumber, resumeLink: resumeLink, lang: lang},
        {transformResponse: function (d, h) {return d;}});
    }
  }

  return instance;
});