techlooper.directive('contestList', function () {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/contests/contest-list.html",
    link: function (scope, el, attrs) {
      scope.showSubmitForm = function(id){
        $('.submit-phase-contest').removeClass('show');
        var parent = $('#id-'+id);
        var div = parent.find('.submit-phase-contest');
        if(div.hasClass('show')){
          div.removeClass('show');
        }else{
          div.addClass('show');
        }
      }
    }
  };
});