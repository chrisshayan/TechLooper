techlooper.factory("salarySharingService", function () {
  var $$ = {
    checkError: function(){
      var txtJobTitile = $('#txtJobTitle').val();

      var txtJobLevel = $("#selJobLevel").selectize();
      var valJobLevel = txtJobLevel[0].selectize.getValue();

      var txtLocation = $("#selLocation").selectize();
      var valLocation = txtLocation[0].selectize.getValue();

      var txtCategories = $("#selJobLevel").selectize();
      var valCategories = txtCategories[0].selectize.getValue();

      var txtBaseSalary = $('#txtBaseSalary').val();
      var txtReporting = $('#txtReporting').val();

      var inputVal = new Array(txtJobTitile, valJobLevel , valLocation, valCategories, txtBaseSalary, txtReporting);
      var inputENMessage = new Array('Job title', 'Job level', 'Where do you work?', 'Job categories', 'Net salary', 'Who are you reporting to?');
      var errorMassages = '';
      $('.error-messages').hide();
      for(var i = 0; i < inputVal.length ; i ++){
        if(inputVal[i] == ""){
          if(errorMassages != ''){
            errorMassages = errorMassages + ', ';
          }
          errorMassages = errorMassages + inputENMessage[i];
        }
      }
      if(errorMassages != ''){
        $('.error-messages strong').text(errorMassages);
        $('.error-messages').show();
      }else{
        $('.error-messages').hide();
      }

    }
};
  var instance = {
    init: function(){
      $('.selection-box').selectize({
        allowEmptyOption: true
      });
      $('.selection-categories').selectize({
        allowEmptyOption: true,
        maxItems: 3,
        sortField: 'text'
      });
    },
    validationForm: function(){
      $$.checkError();
    }
  };

  return instance;
});