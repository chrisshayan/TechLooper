angular.module("Common").factory("termService", function (jsonValue, utils) {

  var instance = {
    toViewTerms: function(terms) {
      var fnColor = d3.scale.category20c();
      $.each(terms, function(i, term) {
        term.color = fnColor(term.term);
        var notHit = true;
        $.each(jsonValue.viewTerms.mappedItems, function(j, viewTerm) {
          if (viewTerm.term === term.term) {
            terms[i] = $.extend({}, term, viewTerm);
            return notHit = false;
          }
        });
        if (notHit) {
          for (var prop in jsonValue.viewTerms.unmappedItems) {
            var value = jsonValue.viewTerms.unmappedItems[prop];
            if (typeof value === "function") {
              term[prop] = value(term);
            }
            else {
              term[prop] = value;
            }
          }
        }
      });
      return terms;
    }
  }

  return instance;
});