techlooper.directive("popupEmail", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/email/popupEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {
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
    }
  }
});