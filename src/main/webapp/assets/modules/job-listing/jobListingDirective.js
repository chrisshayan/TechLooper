techlooper.directive('showHideBenefits', function() {
    return function(scope, element, attrs) {
      if (scope.$last){
        $('p.offers').on("click", function(){
          $(this).next().toggleClass( "show", 1000);
        });
      }
    };
  });