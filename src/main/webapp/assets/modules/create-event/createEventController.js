techlooper.controller("createEventController", function ($scope, $translate) {
  var placeholder = $translate.instant('whoJoinAndWhyEx');
  $('#txtWhyEvent').val(placeholder);
  $('#txtWhyEvent').focus(function(){
    if($(this).val() === placeholder){
      $(this).val('');
      $(this).removeClass('change-color');
    }
  });

  $('#txtWhyEvent').blur(function(){
    if($(this).val() ===''){
      $(this).val(placeholder);
      $(this).addClass('change-color');
    }
  });
});