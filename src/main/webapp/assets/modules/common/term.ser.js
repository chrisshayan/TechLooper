angular.module("Common").factory("termService", function (jsonValue, utils) {

  var instance = {
    toViewTerms: function(terms) {
      var fnColor = d3.scale.category20c();
      $.each(terms, function(i, term) {
        term.color = fnColor(term.term);
        $.each(jsonValue.viewTerms, function(j, viewTerm) {
          if (viewTerm.term === term.term) {
            term.label = viewTerm.label;
            return false;
          }
        });
      });
      return terms;
    }
  }

  return instance;
});