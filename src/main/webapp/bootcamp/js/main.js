$(document).ready(function() {
  scrollToRegistration();
  menuManager();
  scrollManager();
  $(".birthDay").pickadate();
  menuAnimate();
  //contentAnimate();
	invalidationData();
	shareSocial();
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
	$('button.register').click(function(event){
		event.preventDefault();
		var numberReg =  /^[0-9]+$/;
		var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
		var errorContent = '';
		var fName = $('#txtFirstName').val(),
				//lName = $('#txtLastName').val(),
				job = $('#txtCurrentJob').val(),
				email = $('#txtEmail').val(),
				phoneNumber = $('#txtPhone').val(),
				companyName = $('#txtCompany').val(),
				location = $('#txtLocation').val();
				//birthday = $('#txtBirthDay').val();

		var inputVal = new Array(fName, companyName, job, email, phoneNumber, location);
		var inputMessage = new Array("first name", "company name", "current job title" , "email address", "telephone number", 'location');
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
				errorContent = 'Telephone Numbers only';
			}else{
				errorContent = errorContent + ', Telephone Numbers only';
			}
		}
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
			var today = currentDate();
			var subject = today + ' – LeanVietnamBootCamp - ' + fName;
			$.ajax({
				type: 'POST',
				url: 'http://leads.navigosgroup.com/api/leads',
				contentType: "application/json; charset=utf-8",
				data: JSON.stringify({
					Subject: subject,
					EmailAddress1: email,
					Telephone1: phoneNumber,
					"FirstName": fName,
					"CompanyName": companyName,
					Qntt_Source: 'ONLINE',
					LeadQualityCode: 'HOT',
					Qntt_LegalName: companyName,
					CampaignId: 'ONLINE-LeanVietnamBootCamp',
					Qntt_City: location
				})
				, success: function (data) {
					resetField();
					alert('Thank you for registering for Lean Vietnam Bootcamp');
				}, error: function (err) {
					switch (err.status) {
						case 500: // internal server error or duplication found
							console.log(err.responseJSON.Message);
							break;
						case 400: // validation failed. The error details is the array ModelState
							$.each(err.responseJSON.ModelState, function (index, validationError) {
								console.log(validationError);
							});
							break;
						default:
							console.debug(err.responseJSON);
							break;
					}
				}
			});
		}

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

function currentDate(){
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
		dd='0'+dd;
	}

	if(mm<10) {
		mm='0'+mm;
	}

	return today = mm+'/'+dd+'/'+yyyy;
}

function shareSocial(){
	$('a.share-facebook').on('click', function() {
		window.open(jQuery(this).attr('href'), 'wpcomfacebook', 'menubar=1,resizable=1,width=600,height=400');
		return false;
	});
	$('a.share-linkedin').on('click', function() {
		window.open(jQuery(this).attr('href'), 'wpcomlinkedin', 'menubar=1,resizable=1,width=580,height=450');
		return false;
	});
	$('a.share-google-plus-1').on('click', function() {
		window.open(jQuery(this).attr('href'), 'wpcomgoogle-plus-1', 'menubar=1,resizable=1,width=480,height=550');
		return false;
	});
	$('a.share-twitter').on('click', function() {
		window.open(jQuery(this).attr('href'), 'wpcomtwitter', 'menubar=1,resizable=1,width=600,height=350');
		return false;
	});
}