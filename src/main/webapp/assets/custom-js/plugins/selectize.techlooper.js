Selectize.define('techlooper', function (options) {
  var self = this;

  self.setPlaceholder = function(placeholder) {
    self.settings.placeholder = placeholder;
    self.updatePlaceholder();
  }

  //var techlooperObserver = $.microObserver.get("techlooper");
  //
  //techlooperObserver.on("update placeholder " + options.elem, function(placeholder) {
  //  console.log(placeholder);
  //  self.setPlaceholder(placeholder);
  //});
});