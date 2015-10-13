techlooper.directive("feedbackForm", function (apiService, $timeout) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/feedback/feedback.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl, composeEmail) {
      scope.options = {
        height: 150,
        focus: true,
        airMode: true,
        toolbar: [
          ['edit',['undo','redo']],
          ['headline', ['style']],
          ['style', ['bold', 'italic', 'underline', 'superscript', 'subscript', 'strikethrough', 'clear']],
          ['fontface', ['fontname']],
          ['textsize', ['fontsize']],
          ['fontclr', ['color']],
          ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
          ['height', ['height']],
          ['table', ['table']],
          ['insert', ['link','hr']],
          ['view', ['fullscreen', 'codeview']],
          ['help', ['help']]
        ]
      };
      if(scope.composeEmail.registrantLastName){
        scope.composeEmail.names = scope.composeEmail.registrantFirstName + ' ' + scope.composeEmail.registrantLastName;
      }else{
        scope.composeEmail.names = scope.composeEmail.registrantFirstName;
      }

      scope.send = function(){
        if(scope.feedbackContent == undefined || scope.feedbackContent == ''){
          return;
        }else{
          scope.composeEmail.content = scope.feedbackContent;
        }
        $('.feedback-loading').css('visibility', 'inherit');
        apiService.sendEmailToDailyChallengeRegistrants(scope.composeEmail.challengeId, scope.composeEmail.registrantId, scope.composeEmail)
        .finally(function () {
          $timeout(function(){
            $('.feedback-loading').css('visibility', 'hidden');
              scope.cancel();
          }, 1200);
        });
      }
      scope.cancel = function () {
        if (!scope.composeEmail.visible) return;
        scope.composeEmail.subject = '';
        scope.feedbackContent = '';
        delete scope.composeEmail.visible;
        $('.feedback-loading').css('visibility', 'hidden');
      }
    }
  }
});