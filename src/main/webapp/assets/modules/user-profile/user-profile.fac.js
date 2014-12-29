angular.module('UserProfile').factory('userProfileFactory', function () {
  var instance = {
    customScrollBar: function(){
      $('.bg-item-container').jScrollPane();
    },
    resizeScreen: function(){
      $(window).resize(function () {
        $('.bg-item-container').jScrollPane();
      });
    },
    collapseContent: function(){
      var list = $('.bg-item');
      list.find('h4').on("click", function(){
        if(!$(this).parent().hasClass('active')){
          list.removeClass('active');
          $(this).parent().addClass('active');
          $(this).parent().find('.bg-item-container').jScrollPane();
        }
      });
    }
  }
  return instance;
});