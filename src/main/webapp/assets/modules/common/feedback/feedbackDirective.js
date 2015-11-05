techlooper.directive("feedbackForm", function (apiService, $timeout, resourcesService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/feedback/feedback.html",
    scope: {
      registrants: "=",
      hide: "=",
      announceWinner: "="
    },
    link: function (scope, element, attr, ctrl) {
      resourcesService.getEmailTemplates().then(function (eTemplates) {
        scope.emailTemplates = eTemplates;
        scope.setDefaultValue();
      });

      scope.setDefaultValue = function() {
        if (scope.announceWinner == true) {
          var template = _.findWhere(scope.emailTemplates, {templateId: 104});
          if (!template) template = _.findWhere(scope.emailTemplates, {templateId: 4});
          scope.composeEmail.templateId = template.templateId;
          scope.loadEmailTemplate();
        }
      }

      scope.composeEmail = {};
      if (scope.announceWinner != true) {
        scope.$watch("registrants", function () {
          scope.composeEmail.names = _.reduce(scope.registrants, function (fullName, registrant) {
            return registrant.registrantFirstName + " " + registrant.registrantLastName + ", " + fullName;
          }, "");

          scope.composeEmail.names = scope.composeEmail.names.slice(0, -2);
        });
      }

      scope.send = function () {
        if (scope.feedbackForm.$invalid) return;
        var registrant = scope.registrants[0];
        apiService.sendFeedbackToRegistrant(registrant.registrantId, scope.composeEmail)
          .success(function () {
              scope.cancel();
          });
      }

      scope.cancel = function () {
        //if (!scope.composeEmail.visible) return;
        //scope.composeEmail.subject = '';
        ////scope.feedbackContent = '';
        //scope.composeEmail.error = true;
        //scope.composeEmail = {names: scope.composeEmail.names};
        // scope.composeEmail.templateId = 0;
        scope.setDefaultValue();
        scope.hide();
        //delete scope.composeEmail.visible;
        $('.feedback-loading').css('visibility', 'hidden');
      }

      scope.loadEmailTemplate = function () {
        var template = _.findWhere(scope.emailTemplates, {templateId: parseInt(scope.composeEmail.templateId)});
        scope.composeEmail.subject = template.subject;
        scope.composeEmail.content = template.body;
      }
    }
  }
});