techlooper.factory("landingService", function ($http, jsonValue, $timeout) {

  var instance = {
    init: function(){
      instance.showNumberTalent();
    },
    getNumberTalent: function(){
      var serNumber = '310';
      var newNumber = instance.formationNumber(parseInt(serNumber), 4); // 00014
      return newNumber;
    },
    formationNumber: function(n, p, c) {
      var pad_char = typeof c !== 'undefined' ? c : '0';
      var pad = new Array(1 + p).join(pad_char);
      return (pad + n).slice(-pad.length);
    },
    showNumberTalent: function(){
      var realNumber = instance.getNumberTalent();
      var arrayNumber = realNumber.toString().split('');
      var newNumber = $('.show-number');
      angular.forEach(arrayNumber, function(value, key) {
        newNumber.append('<span class="counters-digit">'+ value +'</span>');
      });
    },
    updateNumberTalent: function(){
      var serNumber = instance.getNumberTalent();
      var subractionOne = parseInt(serNumber) - 1;
      if(subractionOne < 0){
        subractionOne = 0
      }
      var newNumber = instance.formationNumber(parseInt(subractionOne), 4);
      var arrayNumber = newNumber.toString().split('');
      $('.show-number').html('');
      angular.forEach(arrayNumber, function(value, key) {
        $('.show-number').append('<span class="counters-digit">'+ value +'</span>');
      });
      $('.show-number').addClass('updateNumber');
      setTimeout(function(){
        $('.show-number').removeClass('updateNumber');
      }, 1000);
    },
    validateForm: function(){
      var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
          fName = $('#landing-first-name').val(),
          lName = $('#landing-last-name').val(),
          email = $('#landing-email').val(),
          errorContent = '';
      var inputVal = new Array(fName, lName, email);
      var inputMessage = new Array("first name", "last name", "email address");
      $('.alert').html('');
      $.each(inputVal, function (index, value) {
        if (value == "") {
          if (errorContent == '') {
            errorContent = inputMessage[index];
          }
          else {
            errorContent = errorContent + ', ' + inputMessage[index];
          }
        }
      });

      if (email != '' && !emailReg.test(email)) {
        if (errorContent == '') {
          errorContent = 'Email address is not valid';
        }
        else {
          errorContent = errorContent + ', Email address is not valid';
        }
      }
      if (errorContent != '') {
        $('.alert').append('Please enter your <strong>' + errorContent + '</strong>').addClass('alert-danger').animate({
          opacity: 1
        }, 1000);
      }
      else {
        $('.alert').removeClass('alert-danger').animate({
          opacity: 1
        }, 1000);
        //$('#landing-first-name').val('');
        //$('#landing-last-name').val('');
        //$('#landing-email').val('');

        $http.post(jsonValue.httpUri.userRegister, {
          emailAddress: email,
          firstName: fName,
          lastName: lName
        }).success(function (data) {
          $('.alert').removeClass('alert-danger').addClass('alert-success').append('Bạn đã đăng kí thành công. Chào mừng bạn đến với cộng đồng Techlooper!').animate({
            opacity: 1
          }, 1000);
          $('.error-messages').hide();
          $('#landing-first-name').val('');
          $('#landing-last-name').val('');
          $('#landing-email').val('');
          instance.updateNumberTalent();
          $('.alert').animate({
            opacity: 0
          }, 1000, function(){
            $(this).removeClass('alert-success');
          });
        }).error(function (data) {
          $('.alert').addClass('alert-danger').append('Register failed!!').animate({
            opacity: 1
          }, 1000);
        });
      }
    }
  };
  return instance;

});