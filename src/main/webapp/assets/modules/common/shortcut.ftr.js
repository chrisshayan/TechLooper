angular.module("Common").factory("shortcutFactory", function (jsonValue) {
  var traps = [];

  var instance = {
    initialize: function($traps) {
      traps = $traps;
      $.each(traps, function(index, trap) {
        Mousetrap.bindGlobal(trap.key, function(event) {
          if (event.defaultPrevented) {
            console.log("prevent from mousestrap");
            return;
          }
          if (trap.fn(event)) {
            event.preventDefault();
          }
        });
      });
    },

    clear: function() {
      Mousetrap.reset();
    }
  }

  return instance;
});