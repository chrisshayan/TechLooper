angular.module("Common").factory("termService", function (jsonValue) {

  var instance = {
    toViewTerms: function(terms) {
      //var fnColor = d3.scale.category20c();
      $.each(terms, function(i, term) {
        //term.color = fnColor(term.term);
        var notHit = true;
        $.each(jsonValue.viewTerms.listedItems, function(j, viewTerm) {
          if (viewTerm.term === term.term) {
            terms[i] = $.extend({}, term, viewTerm);
            return notHit = false;
          }
        });
        if (notHit) {
          for (var prop in jsonValue.viewTerms.unlistedItems) {
            var value = jsonValue.viewTerms.unlistedItems[prop];
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