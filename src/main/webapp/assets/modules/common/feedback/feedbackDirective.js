techlooper.directive("feedbackForm", function (apiService, $timeout, resourcesService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/feedback/feedback.html",
    scope: {
      registrants: "=",
      hide: "="
    },
    link: function (scope, element, attr, ctrl) {

      resourcesService.getEmailTemplates().then(function (eTemplates) {scope.emailTemplates = eTemplates;});

      scope.composeEmail = {};
      scope.$watch("registrants", function () {
        scope.composeEmail.names = _.reduce(scope.registrants, function (fullName, registrant) {
          return registrant.registrantFirstName + " " + registrant.registrantLastName + ", " + fullName;
        }, "");

        scope.composeEmail.names = scope.composeEmail.names.slice(0, -2);
      });

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
        if($('#emailCompose').length){
          $('#emailCompose').modal('hide');
        }
        //if (!scope.composeEmail.visible) return;
        //scope.composeEmail.subject = '';
        ////scope.feedbackContent = '';
        //scope.composeEmail.error = true;
        //scope.composeEmail = {names: scope.composeEmail.names};
       // scope.composeEmail.templateId = 0;
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