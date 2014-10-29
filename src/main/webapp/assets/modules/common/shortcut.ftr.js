angular.module("Common").factory("shortcutFactory", function (jsonValue) {
  var traps = [];

  var instance = {
    initialize: function($traps) {
      instance.clear();

      traps = $traps;
      $.each(traps, function(index, trap) {
        Mousetrap.bindGlobal(trap.key, function(event) {
          if (trap.fn(event)){
            event.preventDefault();
            event.stopPropagation();
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