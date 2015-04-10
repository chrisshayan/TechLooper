techlooper.controller("companyProfileController", function ($scope, $sce, companyProfileService, $routeParams,
                                                            $http, jsonValue, $timeout, utils, $rootScope, $location) {
  $scope.follow = companyProfileService.followManager;

  var companyName = $routeParams.companyName;
  if (utils.hasNonAsciiChar(companyName)) {
    $rootScope.companyName = companyName;
    $location.path(utils.toAscii(companyName));
  }
  else if ($rootScope.companyName !== undefined) {
    companyName = $rootScope.companyName;
    $rootScope.companyName = undefined;
  }

  $http.get(jsonValue.httpUri.company + "/" + companyName).success(function (data) {
    $.each(data.benefits, function (i, benefit) {
      benefit.name = jsonValue.benefits[benefit.benefitId].name;
      benefit.iconName = jsonValue.benefits[benefit.benefitId].iconName;
    });
    $.each(data.industries, function (i, industry) {
      industry.value = jsonValue.industries[industry.industryId].value;
    });

    data.totalViews = 0;
    data.totalApplications = 0;
    $.each(data.jobs, function (i, job) {
      data.totalViews += job.numOfViews;
      data.totalApplications += job.numOfApplications;
    });
    data.totalViews = data.totalViews.toLocaleString();
    data.totalApplications = data.totalApplications.toLocaleString();
    data.companySize = jsonValue.companySizes[data.companySizeId];
    $scope.companyInfo = data;
    var fullURL = data.jobVideoURLs[0],
        myUrl = '';

    if(fullURL !== undefined){
      var codeURL = getURL(fullURL);
    }else{
      codeURL = '';
    }

    if(codeURL.length > 0 && codeURL != 'error'){
      myUrl = '//www.youtube.com/embed/' + codeURL;
      $scope.companyInfo.jobVideoURLs = $sce.trustAsResourceUrl(myUrl);
    }else{
      $scope.companyInfo.jobVideoURLs = '';
    }
    console.log($scope.companyInfo)
    $timeout(function () {
      $('[data-toggle="tooltip"]').tooltip({html: true, placement: 'top'});
    }, 200);
  });
  $scope.playVideo = function (event) {
    var url = $(event.currentTarget).attr('ng-url');
    var myUrl = '//www.youtube.com/embed/' + getURL(url) + '?autoplay=1';
    $('.modal-body').find('iframe').attr('src', myUrl);
  }
  function getURL(url) {
    var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    var match = url.match(regExp);
    if (match && match[2].length == 11) {
      return match[2];
    }
    else {
      return 'error';
    }
  }

  $scope.checkPLayVideo = function () {
    $(".playerVideo").attr("src", "");
  }
});
