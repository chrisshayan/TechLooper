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
	$('.btn-register').find('a').click(function(){
		$('html,body').animate({ scrollTop: $('#registration').offset().top},800);
	});
}
function menuAnimate(){
	
	$(window).scroll(function(){
		var windscroll = $(window).scrollTop();
		if (windscroll > 0) {
  	if ($('.what-is-it-block').position().top - 60 <= windscroll) {
  		$('.header-block').addClass('changed');
    }else{
    	$('.header-block').removeClass('changed');
    }
  }
	});
}