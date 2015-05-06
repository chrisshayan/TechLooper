techlooper.controller("salaryReviewController", function ($scope, salaryReviewService, $rootScope, jsonValue, $http) {
  salaryReviewService.init();
  var jobLevels = $.extend(true, [], jsonValue.jobLevels.filter(function (value) {return value.id > 0;}));
  $.each(jobLevels, function (i, jobLevel) {jobLevel.translate = $rootScope.translate[jobLevel.translate];});

  $scope.selectize = {
    locations: {
      items: jsonValue.locations.filter(function (location) {return location.id > 0; }),
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        placeholder: 'Ex: Ho Chi Minh',
        maxItems: 1,
        searchField: ['name']
      }
    },
    jobLevels: {
      items: jobLevels,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        placeholder: 'Ex: Manager',
        maxItems: 1,
        searchField: ['translate']
      }
    },
    industries: {
      items: jsonValue.industriesArray,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        placeholder: 'Ex: IT - Software, IT - Hardware/Networking',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name']
      }
    },
    companySize: {
      items: jsonValue.companySizesArray,
      config: {
        valueField: 'id',
        labelField: 'size',
        delimiter: '|',
        placeholder: 'Ex: 1-49',
        maxItems: 1,
        searchField: ['size']
      }
    }
  }

  $scope.error = {};
  $scope.salaryReview = {
    jobTitle: '',
    skills: [],
    locationId: '',
    jobLevelIds: [],
    jobCategories: [],
    companySizeId: '',
    netSalary: '',
    reportTo: ''
  };

  $scope.removeSkill = function (skill) {
    $scope.salaryReview.skills.splice($scope.salaryReview.skills.indexOf(skill), 1);
    $scope.error = {};
  }

  $scope.addNewSkill = function () {
    if ($scope.newSkillName === undefined) {
      return;
    }
    var newSkillName = $scope.newSkillName.trim();
    $scope.newSkillName = newSkillName;
    if (newSkillName.length == 0) {
      $scope.error = {};
      return;
    }

    if ($scope.salaryReview.skills.length === 3) {
      var translate = $rootScope.translate;
      $scope.error.newSkillName = translate.maximum3;
      return;
    }
    delete $scope.error.newSkillName;

    var skillLowerCases = $scope.salaryReview.skills.map(function (skill) {return skill.toLowerCase();});
    if (skillLowerCases.indexOf(newSkillName.toLowerCase()) >= 0) {
      var translate = $rootScope.translate;
      $scope.error.existSkillName = translate.hasExist;
      return;
    }
    delete $scope.error.existSkillName;

    if (newSkillName.length > 0) {
      $scope.salaryReview.skills.push(newSkillName);
      $scope.newSkillName = "";
      $("#txtTopSkills").focus();
    }
  }

  $scope.doNextStep = function () {
    var obj = $scope.salaryReview;
    if(obj.jobTitle.length < 1){
      $scope.error.jobTitle = 'Job title is required ';
    }else{
      delete $scope.error.jobTitle;
    }
    if(obj.jobLevelIds.length < 1){
      $scope.error.jobLevel = 'Job level is required ';
    }else{
      delete $scope.error.jobLevel;
      $scope.salaryReview.jobLevelIds = jsonValue.jobLevelsMap['' + $scope.salaryReview.jobLevelIds].ids;
    }
    if(obj.netSalary == null){
      $scope.error.netSalary = 'Net salary is required ';
    }else{
      delete $scope.error.netSalary;
    }
    if(obj.reportTo.length < 1){
      $scope.error.reportTo = 'Who are you report to? is required ';
    }else{
      delete $scope.error.reportTo;
    }

    if($.isEmptyObject($scope.error)){
      var nextStepNavi = $('.navi-step-salary-review').find('li[date-content=your-employer-content]');
      var nextStepContent = nextStepNavi.attr('date-content');
      nextStepNavi.addClass('active');
      $('.data-content').removeClass('active');
      $('.'+nextStepContent).addClass('active');
    }
  },
  $scope.doSalaryReport = function () {
    var obj = $scope.salaryReview;
    if(obj.locationId.length < 1){
      $scope.error.locationId = 'Location is required ';
    }else{
      delete $scope.error.locationId;
    }
    if(obj.jobCategories.length < 1){
      $scope.error.jobCategories = 'Categories is required ';
    }else{
      delete $scope.error.jobCategories;
    }
    if($.isEmptyObject($scope.error)){
      var nextStepNavi = $('.navi-step-salary-review').find('li[date-content=your-report-content]');
      var nextStepContent = nextStepNavi.attr('date-content');
      nextStepNavi.addClass('active');
      $('.data-content').removeClass('active');
      $('.'+nextStepContent).addClass('active');
    }
    //$http.post(jsonValue.httpUri.salaryReview, $scope.salaryReview)
    //.success(function (data, status, headers, config) {
    //  console.log(data);
    //})
    //.error(function (data, status, headers, config) {
    //});
  }
})
;
