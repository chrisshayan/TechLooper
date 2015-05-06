techlooper.factory("salaryReviewService", function () {

  var instance = {
    init: function () {
      //instance.naviStepSalaryReview();
    },
    naviStepSalaryReview: function(){
      var tabs = $('.navi-step-salary-review').find('li');
      tabs.on('click', function(){
        tabs.removeClass('active');
        $('.data-content').removeClass('active');
        var dataContent = $(this).attr('date-content');
        $(this).addClass('active');
        $('.'+dataContent).addClass('active');
      });
    }
  };

  return instance;
});