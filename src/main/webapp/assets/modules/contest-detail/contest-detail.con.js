techlooper.controller('contestDetailController', function ($scope, apiService, localStorageService, $location, $routeParams,
                                                           jsonValue, $translate, utils, $filter) {


  var parts = $routeParams.id.split("-");
  var lastPart = parts.pop();
  if (parts.length < 2 || (lastPart !== "id")) {
    return $location.path("/");
  }

  var contestId = parts.pop();
  var title = parts.join("");
  if (utils.hasNonAsciiChar(title)) {
    title = utils.toAscii(title);
    return $location.url(sprintf("/challenge-detail/%s-%s-id", title, contestId));
  }

  $scope.status = function (type) {
    switch (type) {
      case "able-to-join":
        if (!$scope.contestDetail) return false;
        var joinContests = localStorageService.get("joinContests") || "";
        var email = localStorageService.get("email") || "";
        var contestInProgress = ($scope.contestDetail.progress.translate == jsonValue.status.registration.translate) ||
          ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
        var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
        return contestInProgress && !hasJoined;

      case "already-join":
        if (!$scope.contestDetail) return false;
        var joinContests = localStorageService.get("joinContests") || "";
        var email = localStorageService.get("email") || "";
        var hasJoined = (joinContests.indexOf(contestId) >= 0) && (email.length > 0);
        return !hasJoined;

      case "contest-in-progress":
        if (!$scope.contestDetail) return false;
        return ($scope.contestDetail.progress.translate == jsonValue.status.progress.translate);
    }
  }

  $scope.contestTimeLeft = function (contest) {
    if (contest) {
      switch (contest.progress.translate) {
        case jsonValue.status.progress.translate:
          return $filter("countdown")(contest.submissionDateTime);

        case jsonValue.status.notStarted.translate:
          return $filter("countdown")(contest.startDateTime);

        case jsonValue.status.registration.translate:
          return $filter("countdown")(contest.registrationDateTime);

        case jsonValue.status.closed.translate:
          return contest.submissionDateTime;
      }
    }
    return "";
  }

  $scope.joinNowByFB = function () {
    if (!$scope.status('able-to-join')) {
      return false;
    }
    apiService.joinNowByFB();
  }

  if (localStorageService.get("joinNow")) {
    localStorageService.remove("joinNow");
    var firstName = localStorageService.get("firstName");
    var lastName = localStorageService.get("lastName");
    var email = localStorageService.get("email");
    apiService.joinContest(contestId, firstName, lastName, email, $translate.use())
      .success(function (numberOfRegistrants) {
        if ($scope.contestDetail) {
          $scope.contestDetail.numberOfRegistrants = numberOfRegistrants;
        }

        var joinContests = localStorageService.get("joinContests") || "";
        joinContests = joinContests.length > 0 ? joinContests.split(",") : [];
        if ($.inArray(contestId, joinContests) < 0) {
          joinContests.push(contestId);
        }

        localStorageService.set("joinContests", joinContests.join(","));
      });
  }

  apiService.getContestDetail(contestId)
    .success(function (data) {
      $scope.contestDetail = data;
      $filter("progress")($scope.contestDetail, "challenge");
    })
    .error(function () {$location.url("404");});

  $scope.fbShare = function () {
    ga("send", {
      hitType: "event",
      eventCategory: "facebookshare",
      eventAction: "click",
      eventLabel: "challengedetail"
    });
    utils.openFBShare("/shareChallenge/" + $translate.use() + "/" + contestId);
  }
  $scope.userRegisters =
      [
        {"id":1,"first_name":"Heather","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "12/05/2015", "score": "50"},
        {"id":2,"first_name":"Johnson","last_name":"Pham","email":"thuhoang@navigosgroup.com", "registration": "08/05/2015", "score": "30"},
        {"id":3,"first_name":"Mart","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "06/06/2015", "score": "60"},
        {"id":4,"first_name":"Robert","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "12/05/2015", "score": "70"},
        {"id":5,"first_name":"Nguyen","last_name":"Hoang","email":"trinh.pham@navigosgroup.com", "registration": "18/05/2015", "score": "90"},
        {"id":6,"first_name":"Heather","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "12/05/2015", "score": "50"},
        {"id":7,"first_name":"Johnson","last_name":"Pham","email":"thuhoang@navigosgroup.com", "registration": "08/05/2015", "score": "30"},
        {"id":8,"first_name":"Mart","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "06/06/2015", "score": "60"},
        {"id":9,"first_name":"Robert","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "12/05/2015", "score": "70"},
        {"id":10,"first_name":"Nguyen","last_name":"Hoang","email":"trinh.pham@navigosgroup.com", "registration": "18/05/2015", "score": "90"},
        {"id":11,"first_name":"Heather","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "12/05/2015", "score": "50"},
        {"id":12,"first_name":"Johnson","last_name":"Pham","email":"thuhoang@navigosgroup.com", "registration": "08/05/2015", "score": "30"},
        {"id":13,"first_name":"Mart","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "06/06/2015", "score": "60"},
        {"id":14,"first_name":"Robert","last_name":"Bell","email":"trinh.pham@navigosgroup.com", "registration": "12/05/2015", "score": "70"},
        {"id":15,"first_name":"Nguyen","last_name":"Hoang","email":"trinh.pham@navigosgroup.com", "registration": "18/05/2015", "score": "90"}

      ];

  $scope.sortUsers = function(keyname){
    $scope.sortKey = keyname;   //set the sortKey to the param passed
    $scope.reverse = !$scope.reverse; //if true make it false and vice versa
  };

  //var param = $location.search();
  //if (param.a == "registrants") {
  //  $('.nav-tabs a[href=".registrants"]').tab('show');
  //}

  apiService.getChallengeRegistrants(contestId)
    .success(function(registrants) {
      $scope.registrants = registrants;
    });
});

