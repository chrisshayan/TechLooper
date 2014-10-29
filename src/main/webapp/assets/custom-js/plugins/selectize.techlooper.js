Selectize.define('techlooper', function (options) {
  var self = this;

  self.onOptionSelect = (function (e) {
    var original = self.onOptionSelect;
    return function (e) {
      var fn = original.apply(this, arguments);
      self.close();
      return fn;
    };
  })();

  var onReturn = options.onReturn;
  self.onKeyDown = (function (e) {
    var original = self.onKeyDown;
    return function (e) {
      var dropdownOpened = false;
      switch (e.keyCode) {
        case 13:
          dropdownOpened = self.isOpen;
          break;
        case 27:
          self.isOpen ? e.preventDefault() : "";
          break;
      }

      var fn = original.apply(this, arguments);
      switch (e.keyCode) {
        case 13:
          !dropdownOpened && $.type(onReturn) === "function" ? onReturn(e) : "";
          break;
      }
      return fn;
    };
  })();
});