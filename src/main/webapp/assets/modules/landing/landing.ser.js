techlooper.factory("landingService", function ($http, jsonValue, $http, $timeout, $q) {
  var maxTotal = function(number) {
    return Math.max(5000 - parseInt(number), 0);
  }

  var instance = {
    init: function(){
      instance.showNumberTalent();
    },

    getNumberTalent: function(){
      var deferred = $q.defer();
      $http.get(jsonValue.httpUri.userRegisterCount)
        .success(function(data) {
          var newNumber = instance.formationNumber(maxTotal(data), 4);
          deferred.resolve(newNumber);
        });

      return deferred.promise;
    },

    formationNumber: function(number, pad) {
      var pad = new Array(1 + pad).join('0');
      return  (pad + number).slice(-pad.length);
    },

    // load page
    showNumberTalent: function(){
      instance.getNumberTalent().then(function(realNumber) {
        var arrayNumber = realNumber.toString().split('');
        var newNumber = $('.show-number');
        angular.forEach(arrayNumber, function(value, key) {
          newNumber.append('<span class="counters-digit">'+ value +'</span>');
        });
      });
    },

    //after form save
    updateNumberTalent: function(){
      instance.getNumberTalent().then(function(countdownTotal) {
        var newNumber = instance.formationNumber(countdownTotal, 4) - 1;
        var arrayNumber = newNumber.toString().split('');
        $('.show-number').html('');
        angular.forEach(arrayNumber, function(value, key) {
          $('.show-number').append('<span class="counters-digit">'+ value +'</span>');
        });
        $('.show-number').addClass('updateNumber');
        setTimeout(function(){
          $('.show-number').removeClass('updateNumber');
        }, 1000);
      });
      //var serNumber = instance.getNumberTalent();

    },
    validateForm: function(){
      var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/,
          fName = $('#landing-first-name').val(),
          lName = $('#landing-last-name').val(),
          email = $('#landing-email').val(),
          errorContent = '';
      var inputVal = new Array(fName, lName, email);
      var inputMessage = new Array(" tên", " họ", " Địa chỉ email");
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
          errorContent = 'Email không đúng định dạng';
        }
        else {
          errorContent = errorContent + ', Email không đúng định dạng';
        }
      }
      if (errorContent != '') {
        $('.alert').append('Xin vui kiểm tra <strong>' + errorContent + '</strong>').addClass('alert-danger').animate({
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
          instance.updateNumberTalent();
          $('.alert').animate({
            opacity: 0
          }, 1000, function(){
            $(this).removeClass('alert-success');
          });

          //call server to update number
          //instance.updateNumberTalent();
        }).error(function (data) {
          $('.alert').addClass('alert-danger').append('đăng kí thành công!!').animate({
            opacity: 1
          }, 1000);
        });
      }
    }
  };
  return instance;

});