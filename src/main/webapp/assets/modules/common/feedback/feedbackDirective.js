techlooper.directive("feedbackForm", function (apiService, $timeout, $filter) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/feedback/feedback.html",
    scope: {
      registrants: "=",
      ngShow: "="
    },
    link: function (scope, element, attr, ctrl, composeEmail) {
      apiService.getAvailableEmailTemplates()
        .success(function (templateList) {
          _.each(templateList, function (template) {
            template.text = $filter('translate')(template.templateName);
            template.value = template.templateId;
          });
          scope.emailTemplates = templateList;
        });

      scope.composeEmail.names = _.reduceRight(scope.registrants, function (fullName, name) {
        return name.registrantFirstName + " " + name.registrantLastName + fullName;
      }, ";");

      scope.send = function () {
        if (scope.feedbackForm.$invalid) return;

        $('.feedback-loading').css('visibility', 'inherit');

        var registrant = scope.registrants[0];
        apiService.sendFeedbackToRegistrant(registrant.registrantId, scope.composeEmail)
          .success(function () {
            $timeout(function () {
              $('.feedback-loading').css('visibility', 'hidden');
              scope.cancel();
            }, 1200);
          })
          .error(function () {
            //scope.composeEmail.error = false;
            $timeout(function () {
              $('.feedback-loading').css('visibility', 'hidden');
            }, 1200);
          });
      }

      scope.cancel = function () {
        //if (!scope.composeEmail.visible) return;
        //scope.composeEmail.subject = '';
        ////scope.feedbackContent = '';
        //scope.composeEmail.error = true;
        scope.composeEmail = {names: scope.composeEmail.names};
        scope.composeEmail.templateId = 0;
        scope.ngShow = false;
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