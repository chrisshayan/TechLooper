techlooper.directive("popupEmail", function (apiService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/email/popupEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {
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