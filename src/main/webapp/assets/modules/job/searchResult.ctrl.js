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
      $scope.$apply();
    });
	$scope.playVideo = function(event) {
		//var url = '//www.youtube.com/embed/' + $(event.currentTarget).attr('href').substr($(event.currentTarget).attr('href').indexOf("=") + 1) + '?autoplay=1';
		var url = $(event.currentTarget).attr('href');
		var myUrl = '//www.youtube.com/embed/'+ getId(url) + '?autoplay=1';

		$('.modal-body').find('iframe').attr('src',myUrl);
	}
	function getId(url) {
	    var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
	    var match = url.match(regExp);

	    if (match && match[2].length == 11) {
	        return match[2];
	    } else {
	        return 'error';
	    }
	}
});
