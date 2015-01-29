$(document).ready(function() {
  loadHCMMap();
  google.maps.event.addDomListener(window, 'load', loadHCMMap);
  loadHNMap();
  google.maps.event.addDomListener(window, 'load', loadHNMap);
  scrollToRegistration();
  menuManager();
  scrollManager();
  $(".birthDay").pickadate();
  menuAnimate();
  contentAnimate();
	invalidationData();
});
function menuManager(){
	var icMenu = $('.fa-bars'),
			listMenu = $('.menu-list');
	icMenu.click(function(){
		if(listMenu.hasClass('active')){
				listMenu.removeClass('active');
		}else{
			listMenu.addClass('active');
		}
	});
}
function loadHCMMap() {
  var mapCanvas = document.getElementById('HCM-map');
  var mapOptions = {
      center: new google.maps.LatLng(10.771091, 106.688442),
      zoom: 17,
      mapTypeId: google.maps.MapTypeId.ROADMAP
  }
  var map = new google.maps.Map(mapCanvas, mapOptions)
}
function loadHNMap() {
  var mapCanvas = document.getElementById('HN-map');
  var mapOptions = {
      center: new google.maps.LatLng(21.017329, 105.848996),
      zoom: 17,
      mapTypeId: google.maps.MapTypeId.ROADMAP
  }
  var map = new google.maps.Map(mapCanvas, mapOptions)
}

function scrollManager(){
	var navi = $('.menu-list');
	navi.find('a').on('click', function(){
		var scrollPath = $(this).attr('href');
		$('html,body').animate({ scrollTop: $(scrollPath).offset().top},800);
		navi.removeClass('active');
	});
}
function scrollToRegistration(){
	$('.register-form').click(function(){
		$('html,body').animate({ scrollTop: $('#registration').offset().top},800);
	});
}
function menuAnimate(){
	var windscroll = $(window).scrollTop();
	if($('.what-is-it-block').position().top >  - 60 <= windscroll){
		$('.header-block').addClass('changed');
	}
	$(window).scroll(function(){
		windscroll = $(window).scrollTop();
		if (windscroll > 0) {
	  	if ($('.what-is-it-block').position().top - 60 <= windscroll) {
	  		$('.header-block').addClass('changed');
	    }else{
	    	$('.header-block').removeClass('changed');
	    }
	  }
	});
}
function contentAnimate(){
	$(window).scroll(function(){
		var windscroll = $(window).scrollTop();
		if (windscroll > 0) {
	  	if ($('.what-is-it-block').position().top - 200 <= windscroll) {
	  		$('.img-desc').addClass('transition');
	    }
	    
	    if ($('.why-lean-vietnam-block').position().top - 300 <= windscroll) {
	  		$('.why-content li').addClass('transition');
	    }

			if($('.key-benefits-block').position().top - 350 <= windscroll){
				$('.benefits-content').find('.right-side').addClass('transition');
			}

			if($('.who-should-attend-block').position().top - 450 <= windscroll){
				$('.who-should-attend-content').addClass('transition');
			}

			if ($('.speakers-block').position().top - 300 <= windscroll) {
				$('.speaker-user').find('li').addClass('transition');
			}
	  }
	});
}

function invalidationData() {
	$('.register').click(function(event){
		event.preventDefault();
		//var nameReg = /^[A-Za-z]+$/;
		var numberReg =  /^[0-9]+$/;
		var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
		var errorContent = '';
		var fName = $('#txtFirstName').val(),
				lName = $('#txtLastName').val(),
				job = $('#txtCurrentJob').val(),
				email = $('#txtEmail').val(),
				phoneNumber = $('#txtPhone').val(),
				companyName = $('#txtCompany').val(),
				location = $('#txtLocation').val(),
				birthday = $('#txtBirthDay').val();

		var inputVal = new Array(fName, lName, job, email, phoneNumber);
		var inputMessage = new Array("first name", "last name", "current job title" , "email address", "telephone number");
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
		if(phoneNumber != '' &&  !numberReg.test(phoneNumber)){
			if(errorContent == ''){
				errorContent = 'Numbers only';
			}else{
				errorContent = errorContent + ', Numbers only';
			}
		}
		if(email != '' && !emailReg.test(email)){
			if(errorContent == ''){
				errorContent = 'Please enter a valid email address';
			}else{
				errorContent = errorContent+ ', Please enter a valid email address';
			}
		}
		if(errorContent != ''){
			$('.error-messages').append('Please enter your <strong>' + errorContent+ '</strong>');
		}else{
			var userInfo = '{"firstName": "' + fName + '", "lastName" : "' + lName + '", "currentJobTitle": "' + job + '", "emailAddress" : "' + email + '", "phoneNumber": "' + phoneNumber + '", "companyName" : "' + companyName + '", "location" : "' + location + '", "birthday" : "' + birthday + '"}';
			sendData(userInfo);
		}

	});
}
function sendData(userInfo){
	$.ajax({
		type: 'POST',
		url: 'users',
		data: userInfo,
		success: function(data) {resetField();alert('Thank you'); },
		contentType: "application/json; charset=UTF-8",
		dataType: 'json'
	});
}
function resetField(){
	$('#txtFirstName').val(''),
	$('#txtLastName').val(''),
	$('#txtCurrentJob').val(''),
	$('#txtEmail').val(''),
			$('#txtPhone').val(''),
	$('#txtCompany').val(''),
	$('#txtLocation').val(''),
	$('#txtBirthDay').val('');
}