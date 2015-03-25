techlooper.controller('landingController', function ($scope, $http, jsonValue) {
  $scope.validationRegister = function () {
    var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
      fName = $('#landing-first-name').val(),
      lName = $('#landing-last-name').val(),
      email = $('#landing-email').val(),
      errorContent = '';
    var inputVal = new Array(fName, lName, email);
    var inputMessage = new Array("first name", "last name", "email address");
    $('.error-messages').html('');
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
      $('.error-messages').append('Please enter your <strong>' + errorContent + '</strong>').show();
    }
    else {
      $('.error-messages').hide();
      $('#landing-first-name').val('');
      $('#landing-last-name').val('');
      $('#landing-email').val('');

      $http.post(jsonValue.httpUri.userRegister, {
        emailAddress: email,
        firstName: fName,
        lastName: lName
      }).success(function (data) {
        alert("Cam on");
        $('.error-messages').hide();
        $('#landing-first-name').val('');
        $('#landing-last-name').val('');
        $('#landing-email').val('');
      }).error(function (data) {
          $('.error-messages').append('Register failed!!').show();
        });
    }
  };
});