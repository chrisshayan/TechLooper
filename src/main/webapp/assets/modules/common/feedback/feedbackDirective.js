techlooper.directive("feedbackForm", function (apiService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/feedback/feedback.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl, composeEmail) {
      $('.summernote').summernote({
        toolbar: [
          ['fontname', ['fontname']],
          ['fontsize', ['fontsize']],
          ['style', ['bold', 'italic', 'underline', 'clear']],
          ['color', ['color']],
          ['para', ['ul', 'ol', 'paragraph']],
          ['height', ['height']],
          ['table', ['table']],
          ['insert', ['link']],
          ['misc', ['undo', 'redo', 'codeview', 'fullscreen']]
        ]
      });
      if(scope.composeEmail.registrantLastName){
        scope.composeEmail.names = scope.composeEmail.registrantFirstName + ' ' + scope.composeEmail.registrantLastName;
      }else{
        scope.composeEmail.names = scope.composeEmail.registrantFirstName;
      }

      scope.send = function(){
        scope.composeEmail.content = $('.summernote').code();
        if(scope.composeEmail.content == '<p><br></p>'){
          return;
        }
        apiService.sendEmailToDailyChallengeRegistrants(scope.composeEmail.challengeId, scope.composeEmail.registrantId, scope.composeEmail)
        .finally(function () {
           scope.cancel();
        });
      }
      scope.cancel = function () {
        if (!scope.composeEmail.visible) return;
        scope.composeEmail.subject = '';
        $('.summernote').code('');
        delete scope.composeEmail.visible;
      }
    }
  }
});