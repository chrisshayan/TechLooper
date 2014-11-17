$.expr[":"].containsIgnoreCase = jQuery.expr.createPseudo(function(arg) {
  return function( elem ) {
    return jQuery(elem).text().toLowerCase() === arg.toLowerCase();
  };
});

$.extend({
  distinct : function(array) {
    var result = [];
    $.each(array, function(i,v){
      if ($.inArray(v, result) == -1) result.push(v);
    });
    return result;
  }
});