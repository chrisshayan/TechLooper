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
      instance.refineSalary(term);
      instance.refineLogo(term);
      return term;
    },

    refineLogo: function(term) {
      $.each(jsonValue.technicalSkill, function(i, skill) {
        if (skill.term === term.term) {
          term.logo = skill.logo;
          return false;
        }
      });
    },

    refineSalary: function(term) {
      if (term['averageSalaryMin'] === undefined && term['averageSalaryMax'] === undefined) {
        term.salRange = "";
        return ;
      }
      if ($.isNumeric(term.averageSalaryMin) &&  $.isNumeric(term.averageSalaryMax)) {
        term.salRange = "$" + term.averageSalaryMin.toLocaleString() + " - " + "$" + term.averageSalaryMax.toLocaleString();
      }
      else if ($.isNumeric(term.averageSalaryMin)) {
        term.salRange = "From $" + term.averageSalaryMin.toLocaleString();
      }
      else {
        term.salRange = "Up To $" + term.averageSalaryMax.toLocaleString();
      }
    }
  }

  return instance;
});