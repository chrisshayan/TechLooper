techlooper.directive("popupEmail", function (apiService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/email/popupEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.loadEmailTemplate = function (templateId) {
        apiService.getTemplateById(templateId)
          .success(function (template) {
            scope.composeEmail.subject = template.subject;
            scope.feedbackContent = template.body;
          })
      }
    }
  }
}).controller('feedbackCtrl', function($scope) {
  $scope.options = {
    height: 150,
    toolbar: [
      ['headline', ['style']],
      ['fontface', ['fontname']],
      ['textsize', ['fontsize']],
      ['style', ['bold', 'italic', 'underline', 'superscript', 'subscript', 'strikethrough', 'clear']],
      ['fontclr', ['color']],
      ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
      ['height', ['height']],
      ['table', ['table']],
      ['insert', ['link','hr']],
      ['view', ['fullscreen', 'codeview']]
    ]
  };
});;