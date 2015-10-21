String.prototype.capitalize = function(){
  var sa = this.replace(/-/g,' ');
  var saa = sa.toLowerCase();
  var sb = saa.replace( /(^|\s)([a-z])/g , function(m,p1,p2){ return p1+p2.toUpperCase(); } );
  var sc = sb.replace(/\s+/g, '-');
  return sc;
};