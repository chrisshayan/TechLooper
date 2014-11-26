angular.module("Common").factory("termService", function (jsonValue) {

  var instance = {
    toViewTerms: function(terms) {
      var fnColor = d3.scale.category20();
      $.each(terms, function(i, term) {
        var notHit = true;
        $.each(jsonValue.viewTerms.listedItems, function(j, viewTerm) {
          if (viewTerm.term === term.term) {
            terms[i] = $.extend({}, viewTerm, term);
            return notHit = false;
          }
        });
        if (notHit) {
          for (var prop in jsonValue.viewTerms.unlistedItems) {
            var value = jsonValue.viewTerms.unlistedItems[prop];
            term[prop] = (typeof value === "function") ? value(term) : value;
          }
          terms[i].color = fnColor(term.term);//allow d3js select color for unlisted items
        }
      });
      return terms;
    }
  }

  return instance;
});