angular.module('Skill').factory('skillTableFactory', function () {
  return instance = {
    formatDate: function () {
      var lCur = Date.today().toString("MMM d"),
        fCur = Date.today().add(-7).days().toString("MMM d"),
        current = fCur + ' - ' + lCur;

      var lPre = Date.today().add(-8).days().toString("MMM d"),
        fPre = Date.today().add(-7).days().clone(),
        previous = fPre.add(-7).days().toString("MMM d") + ' - ' + lPre;

      $('span.curDate').text(current);
      $('span.preDate').text(previous);
    },

    reformatData: function (data) {
      var newData = [];
      var icStock = '';
      $.each(data, function (index, value) {
        var per = ((value.currentCount - value.previousCount) * 100) / value.previousCount;
        if (per > 0) {
          icStock = 'fa-arrow-up ic-blue';
          per = per.toFixed(2).replace(/(\.[0-9]*?)0+$/, "$1").replace(/\.$/, "");
        }
        else if (per < 0) {
          icStock = 'fa-arrow-down ic-red';
          per = per.toFixed(2).replace(/(\.[0-9]*?)0+$/, "$1").replace(/\.$/, "");
        }
        else {
          icStock = '';
          per = 0;
        }
        newData.push({
          'name': value.skillName,
          'current': value.currentCount,
          'previous': value.previousCount,
          'change': per,
          'icon': icStock
        });
      });
      return newData;
    }
  };
});