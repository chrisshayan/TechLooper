techlooper.controller('landingController', function ($scope, $http, jsonValue, $timeout) {
  $scope.validationRegister = function () {
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
      $('#landing-first-name').val('');
      $('#landing-last-name').val('');
      $('#landing-email').val('');

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
  };
});