angular.module('Jobs').controller('searchResultController',
  function ($scope, $location, $routeParams, connectionFactory, jsonValue, searchBoxService) {
    searchBoxService.initializeIntelligent($scope);
    var keyWords = '';
    var skills = $routeParams.text.split(" ");
    $('.selectator_chosen_items').css('display', 'block');
    $.each(skills, function (index, skill) {
      $("select.termsList option:contains('" + skill + "')").attr("selected", "selected");
      searchBoxService.refresh();
    });
    var txtInput = '';
    $('.search-result').click(function () {
      if (!$('.selectator_chosen_items').is(':empty') || $('input.selectator_input').val() != '') {
        keyWords = "";
        getKeyWords();
        if (keyWords != '') {
          $location.path("/jobs/search/" + keyWords);
          $scope.$apply();
          $('input.selectator_input').val(txtInput).css('width', 'auto');
        }
      }
    });
    function getKeyWords() {
      var $this = $('.selectator_chosen_items').find('.selectator_chosen_item_title');
      $this.each(function () {
        keyWords = keyWords + ' ' + $(this).text();
      });
      txtInput = $('input.selectator_input').val();
      if (txtInput != '') {
        keyWords = keyWords + ' ' + txtInput;
      }
      keyWords = keyWords.substring(1);
      return keyWords;
    }

    connectionFactory.initialize($scope);

    var count = 0;
    $scope.search = {
      jobs: [],
      pageNumber: 0,
      busy: false,
      total: 1,
      nextPage: function () {
        if (this.jobs.length === this.total || this.busy) {
          return;
        }
        this.busy = true;
        connectionFactory.findJobs({
          "terms": $routeParams.text,
          "pageNumber": ++this.pageNumber
        });
      }
    };


    $scope.$on(jsonValue.events.foundJobs, function (event, data) {
      var search = $scope.search;
      search.total = data.total;
      search.jobs = search.jobs.concat(data.jobs);
      search.busy = false;
      $scope.$apply();
      alignLogo();
      $('.search-block').css('min-height',$(window).height());
      $('.jobs-total strong').text(search.total);
      $('.jobs-total').show();
    });

    $scope.playVideo = function (event) {
      var url = $(event.currentTarget).attr('ng-url');
      var myUrl = '//www.youtube.com/embed/' + getURL(url) + '?autoplay=1';
      $('.modal-body').find('iframe').attr('src', myUrl);
    }
    function getURL(url) {
      var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
      var match = url.match(regExp);
      if (match && match[2].length == 11) {
        return match[2];
      }
      else {
        return 'error';
      }
    }


    $scope.openDetail = function (event) {
      var url = $(event.currentTarget).attr('data-url');
      window.open(url);
    }
    function alignLogo() {
      var list = $('.job-item'),
        h = list.height();
      $('.company-logo').css({
        'height': h,
        'line-height': h+'px'
      });
      $('.company-video').css('height', h);
      $('.job-infor').css('height', h);
    }
  });
