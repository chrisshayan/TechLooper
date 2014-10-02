$( document ).ready(function() {
	var h = $(window).height();
	$('.bg-landingpage').css({
		height:h - 30
	});

	$('#bubble').parallax({
	  calibrateX: false,
	  calibrateY: true,
	  invertX: false,
	  invertY: true,
	  limitX: false,
	  limitY: 10,
	  scalarX: 2,
	  scalarY: 8,
	  frictionX: 0.2,
	  frictionY: 0.8,
	  originX: 0.0,
	  originY: 1.0
	});
	$('#main-page').parallax();
});
