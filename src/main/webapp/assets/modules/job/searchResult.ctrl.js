angular.module('Jobs').controller('searchResultController',
  ["$scope", "$routeParams", "connectionFactory", "jsonValue", "searchBoxService", 
    function ($scope, $routeParams, connectionFactory, jsonValue, searchBoxService) {
    	searchBoxService.initializeIntelligent($scope);

 	function transferKeyWords(){
    	var skills = $routeParams.text.split(" ");;
    	console.log(skills)
    }
    transferKeyWords();

// //console.log($routeParams.text);
      connectionFactory.initialize($scope);
      $scope.search = {
        jobs: [],
        busy: false,
        nextPage: function () {
          if (this.busy) return;
          this.busy = true;
          console.log("scrolling me");
          connectionFactory.findJobs({"terms": $routeParams.text, "pageNumber": "1"});
        }
      };


      $scope.$on(jsonValue.events.foundJobs, function (event, data) {
        $scope.search.jobs = $scope.search.jobs.concat(data.jobs);
        $scope.search.busy = false;
        $scope.$apply();
      });

  // var pageNumber = 0,
  //       termKeys = {},
  //       oldKeys ="",
  //       newKeys = "",
  //       totalPage = 0,
  //       countItems = 0,
  //       jobItems = '';

  // $scope.$on(jsonValue.events.foundJobs, function(event, data){
  //       $('.jobs-total').show().find('strong').text(data.total);
  //       addDataResult(data);
  //       totalPage = Math.ceil(data.total/countItems);
  //       $('.loading-paging').fadeOut();
  //   });
  //   searchFormController.getKeyWords();
  //   function showSearchResult(){
  //       if(!$('.technical-Skill-List').hasClass('hide')){
  //           var sForm = $('.search-form-container');
  //           sForm.parent().animate({
  //               'padding': '20px 40px'
  //           },{
  //               duration: '10000',
  //               easing: 'easeOutQuad'
  //           });

  //           sForm.animate({
  //               'width': '100%'
  //           },{
  //               duration: '10000',
  //               easing: 'easeOutQuad'
  //           });
  //           $('.technical-Skill-List').addClass('hide');
  //           $('body').css("background-color","#201d1e");
  //           $('.search-block').css({
  //               "position":"inherit",
  //               "height": "auto"
  //           });
  //           $('.search-form-container').css("max-width","100%");
  //       }
  //       connectionFactory.findJobs(termKeys);
  //   }
  //   function addDataResult(json){
  //       countItems = 0;
  //       jobItems ="";
  //       var video = "";
  //       $.each(json.jobs, function(index, job){
  //           if(job.logoUrl ==''){
  //               companyInfo = job.company;
  //           }else{
  //               companyInfo = '<img src="'+ job.logoUrl +'" title="'+ job.company +'">';
  //           }
  //           if(job.videoUrl == null){
  //               video = '';
  //           }else{
  //               video = '<p><a class="play-video" href="'+ job.videoUrl +'" data-toggle="modal" data-target="#myModal"><i class="fa fa-play-circle"></i></a></p>';
  //           }
  //           jobItems = jobItems + '<div class="job-item"><div class="job-infor"><div class="job-title">';
  //           jobItems = jobItems + '<a href="'+ job.url +'" target="_blank">'+ job.title +'</a></div>';
  //           jobItems = jobItems + '<ul><li><i class="fa fa-money"></i>Negotiable</li><li><i class="fa fa-map-marker"></i>'+ job.location +'</li><li><i class="fa fa-male"></i>'+ job.level +'';
  //           jobItems = jobItems + '</li><li><i class="fa fa-calendar"></i>Posted: '+ job.postedOn +'</li></ul></div>';
  //           jobItems = jobItems + '<div class="company-logo">'+ companyInfo + video +'</div>';
  //           jobItems = jobItems + '<div class="view-more"><a href="'+ job.url +'" target="_blank">View More</a></div></div></div>';
  //           countItems = ++countItems;
  //       });
  //       $('.search-result-container-block').append(jobItems);
  //       $('.play-video').on("click",function(){
  //           var url = '//www.youtube.com/embed/' + $(this).attr('href').substr($(this).attr('href').indexOf("=") + 1) + '?autoplay=1';
            
  //           $('.modal-body').find('iframe').attr('src',url);
  //       });
  //   }
  //    $(window).scroll(function() {
  //      if($(window).scrollTop() + $(window).height() == $(document).height()) {
  //          addMoreData();
  //      }
  //   });
  //   function addMoreData(){
  //       if (pageNumber >= totalPage) {
  //           $('.loading-paging').addClass('hide');
  //           return;
  //       }
  //       $('.loading-paging').fadeIn();
  //       termKeys["pageNumber"] = '' + (++pageNumber);
  //       connectionFactory.findJobs(termKeys);
  //   }
}]);

