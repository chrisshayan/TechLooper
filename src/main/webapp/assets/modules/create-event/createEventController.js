techlooper.controller("createEventController", function ($scope, $translate, jsonValue) {

  $scope.selectize = {
    selectionTimeFrom: {
      items: jsonValue.hours,
      config: {
        valueField: 'id',
        labelField: 'value',
        delimiter: '|',
        maxItems: 1,
        searchField: ['value'],
        placeholder: '06:30 PM',
        onInitialize: function (selectize) {
          $scope.selectize.selectionTimeFrom.$elem = selectize;
        }
      }
    },
    selectionTimeTo: {
      items: jsonValue.hours,
      config: {
        valueField: 'id',
        labelField: 'value',
        delimiter: '|',
        maxItems: 1,
        searchField: ['value'],
        placeholder: '07:30 PM',
        onInitialize: function (selectize) {
          $scope.selectize.selectionTimeTo.$elem = selectize;
        }
      }
    }
  };

  $('.selection-date').find('.date').datepicker({
    autoclose:  true,
    format: 'dd/mm/yyyy'
  });
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