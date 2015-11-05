techlooper.directive("popupEmail", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/email/popupEmail.html",
    scope: {registrants: "="},
    link: function (scope, element, attr, ctrl) {
      scope.hidePopup = function () {
        $('#emailCompose').modal('hide');
      }
      //scope.loadEmailTemplate = function (templateId) {
      //  apiService.getTemplateById(templateId)
      //    .success(function (template) {
      //      scope.composeEmail.subject = template.subject;
      //      scope.feedbackContent = template.body;
      //    })
      //}
    }
  }
});