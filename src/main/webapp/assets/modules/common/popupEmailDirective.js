techlooper.directive("popupEmail", function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/popupEmail.html",
    scope: {
      composeEmail: "="
    },
    link: function (scope, element, attr, ctrl) {
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
    }
  }
});