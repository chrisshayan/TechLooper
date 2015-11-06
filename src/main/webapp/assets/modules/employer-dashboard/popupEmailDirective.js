techlooper.directive("dailySummeryEmail", function (apiService, resourcesService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/employer-dashboard/dailySummeryEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {

      resourcesService.getEmailTemplates().then(function (eTemplates) {
        var templates = eTemplates;
        if (scope.announceWinner != true) {
          templates = _.filter(templates, function (tpl) {return tpl.templateId != 104;});
          templates = _.filter(templates, function (tpl) {return tpl.templateId != 4;});
        }
        scope.emailTemplates = templates;
      });

      //scope.loadEmailTemplate = function () {
      //  apiService.getTemplateById(templateId)
      //      .success(function (template) {
      //        //scope.composeEmail.templateId = templateId;
      //        scope.composeEmail.subject = template.subject;
      //        scope.feedbackContent = template.body;
      //      })
      //}

      scope.loadEmailTemplate = function () {
        var template = _.findWhere(scope.emailTemplates, {templateId: parseInt(scope.composeEmail.templateId)});
        scope.composeEmail.subject = template.subject;
        scope.composeEmail.content = template.body;
        //scope.feedbackContent = template.body;
      }
    }
  }
});