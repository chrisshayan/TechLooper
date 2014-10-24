angular.module('Jobs').controller('searchResultController',
  function ($scope, $location, $routeParams, connectionFactory, jsonValue, searchBoxService) {
    searchBoxService.initializeIntelligent($scope);
    var keyWords = '';
    var skills = $routeParams.text.split(" ");
    $('.selectator_chosen_items').css('display', 'block');
    $.each(skills, function (index, skill) {
      $("select.termsList option:contains('"+skill+"')").attr("selected", "selected");
      searchBoxService.refresh();
    });

	$('.search-result').click(function () {
		if (!$('.selectator_chosen_items').is(':empty') || $('input.selectator_input').val() != '') {
		  keyWords = "";
		  getKeyWords();
		  if (keyWords != '') {
		    $location.path("/jobs/search/" + keyWords);
		    $scope.$apply();
		  }
		}
	});
	function getKeyWords() {
        var $this = $('.selectator_chosen_items').find('.selectator_chosen_item_title');
        $this.each(function () {
          keyWords = keyWords + ' ' + $(this).text();
        });
        var val = $('input.selectator_input').val();
        if(val != ''){
          keyWords = keyWords + ' ' + $('input.selectator_input').val();
        }
        keyWords = keyWords.substring(1);
        return keyWords;
      }

    connectionFactory.initialize($scope);
    $scope.search = {
      jobs: [],
      totalPages : 0,
      pageNumber : 1,
      busy: false,
      nextPage: function () {
        if (this.busy) return;
        this.busy = true;
        if ($scope.search.pageNumber == 1 || $scope.search.pageNumber <= $scope.search.totalPages) {
          connectionFactory.findJobs({"terms": $routeParams.text, "pageNumber": $scope.search.pageNumber});
        } else {
          $scope.search.busy = false;
        }
      }
    };


    $scope.$on(jsonValue.events.foundJobs, function (event, data) {
      if ($scope.search.pageNumber == 1) {
        if (data.total % data.jobs.length == 0) {
          $scope.search.totalPages = Math.floor(data.total / data.jobs.length);
        } else {
          $scope.search.totalPages = Math.floor(data.total / data.jobs.length) + 1;
        }
      }
      $scope.search.jobs = $scope.search.jobs.concat(data.jobs);
      $scope.search.busy = false;
      $scope.search.pageNumber++;
      alignContent();
      $scope.$apply();
    });
	$scope.playVideo = function(event) {
		var url = $(event.currentTarget).attr('ng-url');
		var myUrl = '//www.youtube.com/embed/'+ getURL(url) + '?autoplay=1';
		$('.modal-body').find('iframe').attr('src',myUrl);
	}
	function getURL(url) {
	    var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
	    var match = url.match(regExp);
	    if (match && match[2].length == 11) {
	        return match[2];
	    } else {
	        return 'error';
	    }
	}
	function alignContent(){
		var list = $('.job-item'),
			h = list.height();
		$('.company-logo').css('height',h);
		$('.company-video').css('height',h);
		$('.job-infor').css('height',h);
		
	}
	$scope.openDetail =function(event){
		var url = $(event.currentTarget).attr('data-url');
		window.open(url);
	}
});
