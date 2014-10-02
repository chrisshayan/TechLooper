$( document ).ready(function() {
	var h = $(window).height() - 80,
		w = $(window).width() - 80;
	$('.bg-landingpage').css({
		height:h
	});
	$('#main-page').css('max-width',w);
	$('#bubble').parallax();
	$('#main-page').parallax();
});
