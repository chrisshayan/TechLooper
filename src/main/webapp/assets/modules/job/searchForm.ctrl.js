angular.module('Jobs').controller('searchFormController', function ($scope, searchBoxService, jsonValue) {
  searchBoxService.initSearchTextbox($scope);

  $scope.skills = jsonValue.technicalSkill;

  searchBoxService.openSearchForm($(window).height());
  $(window).resize(function () {
    searchBoxService.openSearchForm($(window).height());
  });
  searchBoxService.hightlightSKill();
  searchBoxService.alignButtonSeatch();

  $scope.closeSearchForm = function () {
      var isVideoHide = $("#myModal").attr("aria-hidden");
      if ($(".btn-close").is(":visible") && (isVideoHide == undefined || isVideoHide == "true")) {
        $('.btn-close').click();
      }
  };
});