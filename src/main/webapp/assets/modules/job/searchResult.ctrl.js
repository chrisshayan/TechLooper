angular.module('Jobs').controller('searchResultController',
  function ($scope, $location, $routeParams, connectionFactory, jsonValue, searchBoxService, utils) {
    //searchBoxService.initializeIntelligent($scope);
    //var keyWords = '';
    //var skills = $routeParams.text.split(" ");
    //$('.selectator_chosen_items').css('display', 'block');
    //$.each(skills, function (index, skill) {
    //  $("select.termsList option:contains('" + skill + "')").attr("selected", "selected");
    //  searchBoxService.refresh();
    //});
    //var txtInput = '';
    //$('.search-result').click(function () {
    //  if (!$('.selectator_chosen_items').is(':empty') || $('input.selectator_input').val() != '') {
    //    keyWords = "";
    //    getKeyWords();
    //    if (keyWords != '') {
    //      $location.path("/jobs/search/" + keyWords);
    //      $scope.$apply();
    //      $('input.selectator_input').val(txtInput).css('width', 'auto');
    //    }
    //  }
    //});
    //function getKeyWords() {
    //  var $this = $('.selectator_chosen_items').find('.selectator_chosen_item_title');
    //  $this.each(function () {
    //    keyWords = keyWords + ' ' + $(this).text();
    //  });
    //  txtInput = $('input.selectator_input').val();
    //  if (txtInput != '') {
    //    keyWords = keyWords + ' ' + txtInput;
    //  }
    //  keyWords = keyWords.substring(1);
    //  return keyWords;
    //}

    connectionFactory.initialize($scope);

    var count = 0;
    $scope.search = {
      jobs: [],
      pageNumber: 0,
      busy: false,
      total: 1,
      nextPage: function () {
        if (this.busy || this.jobs.length === this.total) {
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
      $('.search-block').css('min-height', $(window).height());
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
        'line-height': h + 'px'
      });
      $('.company-video').css('height', h);
      $('.job-infor').css('height', h);
    }


    $scope.searchText = {
      terms: jsonValue.technicalSkill,
      selectedTerms: []
    }

    var tags = utils.setIds(jsonValue.technicalSkill);
    var select2 = $(".termsList2").select2({
      width: "100%",
      tags: tags,
      //tokenSeparators: [","],
      formatSelection: searchBoxService.format,
      formatResult: searchBoxService.format,
      createSearchChoice: function (text) {
        var tag = utils.findBy(tags, "text", text);
        if (tag === undefined) {
          tag = {id: text, text: text};
        }
        // return a csv string (ids)
        //http://ivaynberg.github.io/select2/#doc-tokenSeparators
        //console.log(select2.val());
        console.log($("#e1").select2("data"));
        return tag;
      },
      createSearchChoicePosition: "bottom",
      placeholder: "Enter to search...",
      //allowClear: true,
      openOnEnter: false,
      containerCssClass: "test",
      escapeMarkup: function (markup) { return markup; }
    });

    $('.termsList2 > ul > li > input.select2-input').on('keyup', function (event) {
      if (event.keyCode === 13) {//Enter event
        console.log(13);
      }
    });

    //$(".select2-choices").height(30);

  });