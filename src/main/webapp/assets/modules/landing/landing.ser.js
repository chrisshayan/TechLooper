techlooper.factory('landingService',
  function () {
    var $$ = {
      invalidationData: function(){
        var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
            fName = $('#landing-first-name'),
            lName = $('#landing-last-name'),
            email = $('#landing-email').val(),
            errorContent ='';
        var inputVal = new Array(fName, lName, email);
        var inputMessage = new Array("first name", "last name", "email address");
        $('.error-messages').html('');
        $.each(inputVal, function( index, value ) {
          if(value == ""){
            if(errorContent ==''){
              errorContent = inputMessage[index];
            }else{
              errorContent = errorContent + ', ' + inputMessage[index];
            }
          }
        });
        if(email != '' && !emailReg.test(email)){
          if(errorContent == ''){
            errorContent = 'Email address is not valid';
          }else{
            errorContent = errorContent+ ', Email address is not valid';
          }
        }
        if(errorContent != ''){
          $('.error-messages').append('Please enter your <strong>' + errorContent+ '</strong>');
        }else{
          aler('thanks you')
        }
      }
    };
    var instance = {
      init: function(){
        $$.invalidationData();
      }
    };
    return instance;
  });