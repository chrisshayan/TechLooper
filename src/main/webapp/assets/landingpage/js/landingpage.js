$( document ).ready(function() {
	var h = $(window).height();
	$('.bg-landingpage').css({
		height:h - 30
	});

	$('#bubble').parallax();
	$('#main-page').parallax();
	$("iframe").contents().find("#u_0_3").css("display", "none");
});
