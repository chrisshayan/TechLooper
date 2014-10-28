Selectize.define('techlooper', function (options) {
  var self = this;
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