techlooper.directive("popupEmail", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/email/popupEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {
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