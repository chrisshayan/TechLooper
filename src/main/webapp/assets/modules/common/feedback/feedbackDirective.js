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
      //$('.summernote').summernote({
      //  height: 300,                 // set editor height
      //  minHeight: null,             // set minimum height of editor
      //  maxHeight: null,             // set maximum height of editor
      //  focus: true                  // set focus to editable area after initializing summernote
      //});

      resourcesService.getEmailTemplates().then(function (eTemplates) {
        var templates = eTemplates;
        if (scope.announceWinner != true) {
          templates = _.filter(templates, function (tpl) {return tpl.templateId != 104;});
          templates = _.filter(templates, function (tpl) {return tpl.templateId != 4;});
        }
        scope.emailTemplates = templates;
        scope.setDefaultValue();
      });

      scope.setDefaultValue = function () {
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
        scope.loadingData = true;
        var registrant = scope.registrants[0];
        apiService.sendFeedbackToRegistrant(registrant.registrantId, scope.composeEmail)
          .success(function () {
            scope.postFeedbackSuccess = true;
          }).finally(function(){
            delete scope.loadingData;
            $timeout(function(){
              delete scope.postFeedbackSuccess;
              scope.cancel();
            }, 2000);
          }
        );
      }

      scope.cancel = function () {
        //if (!scope.composeEmail.visible) return;
        scope.composeEmail.subject = '';
        scope.composeEmail.content = '';
        ////scope.feedbackContent = '';
        //scope.composeEmail.error = true;
        //scope.composeEmail = {names: scope.composeEmail.names};
        scope.composeEmail.templateId = 0;
        scope.setDefaultValue();
        scope.hide();
        //delete scope.composeEmail.visible;
        $('.feedback-loading').css('visibility', 'hidden');
      }

      scope.loadEmailTemplate = function () {
        if (scope.announceWinner == true) {
          if (!_.isEmpty(scope.registrants)) {
            var registrant = scope.registrants[0];
            apiService.getTemplateById(scope.composeEmail.templateId, registrant.challengeId)
              .success(function (template) {
                scope.composeEmail.subject = template.subject;
                scope.composeEmail.content = template.body;
              });
          }
        }
        else {
          var template = _.findWhere(scope.emailTemplates, {templateId: parseInt(scope.composeEmail.templateId)});
          scope.composeEmail.subject = template.subject;
          scope.composeEmail.content = template.body;
        }
      }

      scope.$on("reload-default-email-template", function () {
        scope.setDefaultValue();
      });
    }
  }
});