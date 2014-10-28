Selectize.define('techlooper', function (options) {
  var self = this;
  //$(".inputClass").click(searchText.open);
  //console.log(self.$control_input);

  self.setup = (function() {
    var original = self.setup;
    return function() {
      original.apply(self, arguments);
      self.$control.on('click', function(e) {
        self.open();
      });
    };
  })();

  var onReturn =  options.onReturn;
  self.onKeyDown = (function (e) {
    var original = self.onKeyDown;
    return function (e) {
      var fn = original.apply(this, arguments);
      switch (e.keyCode) {
        case 13:
          self.isOpen ? self.close() : onReturn();
          break;
      }
      return fn;
    };
  })();
});