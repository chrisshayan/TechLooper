techlooper.directive("feedbackForm", function (apiService, $timeout) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/feedback/feedback.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl, composeEmail) {
      if (scope.composeEmail.registrantLastName) {
        scope.composeEmail.names = scope.composeEmail.registrantFirstName + ' ' + scope.composeEmail.registrantLastName;
      }
      else {
        scope.composeEmail.names = scope.composeEmail.registrantFirstName;
      }

      scope.send = function () {
        if (scope.feedbackContent == undefined || scope.feedbackContent == '') {
          return;
        }
        else {
          scope.composeEmail.content = scope.feedbackContent;
        }

        console.log(scope.composeEmail.templateId);

        $('.feedback-loading').css('visibility', 'inherit');

        apiService.sendFeedbackToRegistrant(scope.composeEmail.challengeId, scope.composeEmail.registrantId, scope.composeEmail)
          .success(function(){
            $timeout(function () {
              $('.feedback-loading').css('visibility', 'hidden');
              scope.cancel();
            }, 1200);
          })
          .error(function(){
            scope.composeEmail.error = false;
            $timeout(function () {
              $('.feedback-loading').css('visibility', 'hidden');
            }, 1200);
          });
      }

      scope.cancel = function () {
        if (!scope.composeEmail.visible) return;
        scope.composeEmail.subject = '';
        scope.feedbackContent = '';
        scope.composeEmail.error = true;
        scope.composeEmail.templateId = 0;
        delete scope.composeEmail.visible;
        $('.feedback-loading').css('visibility', 'hidden');
      }

      scope.loadEmailTemplate = function (templateId) {
        apiService.getTemplateById(templateId)
          .success(function (template) {
            scope.composeEmail.subject = template.subject;
            scope.feedbackContent = template.body;
          })
      }
    }
  }
});