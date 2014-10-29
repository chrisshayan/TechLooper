Selectize.define('techlooper', function (options) {
  var self = this;

  //self.close = (function (e) {
  //  var original = self.close;
  //  return function (e) {
  //    var fn = original.apply(this, arguments);
  //    console.log("dsfdsf");
  //    return fn;
  //  };
  //})();

  var onReturn = options.onReturn;
  self.onKeyDown = (function (e) {
    var original = self.onKeyDown;
    return function (e) {
      switch (e.keyCode) {
        case 27:
          if (self.isOpen) {
            e.preventDefault();
          }
          break;
      }

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