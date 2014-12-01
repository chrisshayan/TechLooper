angular.module("Common").factory("termService", function (jsonValue) {
  var fnColor = d3.scale.category20();

  var instance = {
    toViewTerms: function (terms) {
      $.each(terms, function (i, term) {
        terms[i] = instance.toViewTerm(term);
      });
      return terms;
    },

    toViewTerm: function (term) {
      var notHit = true;
      $.each(jsonValue.viewTerms.listedItems, function (j, viewTerm) {
        if (viewTerm.term === term.term) {
          term = $.extend({}, viewTerm, term);
          return notHit = false;
        }
      });
      if (notHit) {
        for (var prop in jsonValue.viewTerms.unlistedItems) {
          var value = jsonValue.viewTerms.unlistedItems[prop];
          term[prop] = (typeof value === "function") ? value(term) : value;
        }
        term.color = fnColor(term.term);//allow d3js select color for unlisted items
      }
      return term;
    }
  }

  return instance;
});