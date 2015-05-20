techlooper.controller("priceJobController", function ($scope, $rootScope, jsonValue, $http, utils, $translate, $route, $location) {

  var jobLevels = $.extend(true, [], jsonValue.jobLevels.filter(function (value) {return value.id > 0;}));

  $scope.$watch("translate", function () {
    if (utils.getView() !== jsonValue.views.salaryReview || $rootScope.translate === undefined) {
      return;
    }
    var translate = $rootScope.translate;
    $.each(jobLevels, function (i, jobLevel) {jobLevel.translate = translate[jobLevel.translate];});

    $.each([
      {item: "locations", translate: "exHoChiMinh"},
      {item: "jobLevels", translate: "exManager"},
      {item: "industries", translate: "exItSoftware"},
      {item: "companySize", translate: "ex149"}
    ], function (i, select) {
      if (!$scope.selectize[select.item].$elem) {
        return true;
      }
      $scope.selectize[select.item].$elem.settings.placeholder = translate[select.translate];
      $scope.selectize[select.item].$elem.updatePlaceholder();
    });
  });

  $scope.selectize = {
    locations: {
      items: jsonValue.locations.filter(function (location) {return location.id > 0; }),
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 1,
        searchField: ['name'],
        placeholder: $translate.instant("exHoChiMinh"),
        onInitialize: function (selectize) {
          $scope.selectize.locations.$elem = selectize;
        }
      }
    },
    jobLevels: {
      items: jobLevels,
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate'],
        placeholder: $translate.instant("exManager"),
        onInitialize: function (selectize) {
          $scope.selectize.jobLevels.$elem = selectize;
        }
      }
    },
    industries: {
      items: jsonValue.industriesArray,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name'],
        placeholder: $translate.instant("exItSoftware"),
        onInitialize: function (selectize) {
          $scope.selectize.industries.$elem = selectize;
        }
      }
    },
    companySize: {
      items: jsonValue.companySizesArray,
      config: {
        valueField: 'id',
        labelField: 'size',
        delimiter: '|',
        maxItems: 1,
        searchField: ['size'],
        placeholder: $translate.instant("ex149"),
        onInitialize: function (selectize) {
          $scope.selectize.companySize.$elem = selectize;
        }
      }
    }
  }
  $scope.selectedTime = $translate.instant("day");
  $scope.error = {};
  $scope.salaryReview = {
    genderId: '',
    jobTitle: '',
    skills: [],
    locationId: '',
    jobLevelIds: [],
    jobCategories: [],
    companySizeId: '',
    netSalary: '',
    reportTo: '',
    timeToSendId: ''
  };

  $scope.step = "step1";
  $scope.validate = function(){
    return true;
  }
  $scope.nextStep = function (step, priorStep) {
    if ((($scope.step === priorStep || step === "step3") && !$scope.validate()) || $scope.step === "step3") {
      return;
    }
    var swstep = step || $scope.step;
    $scope.step = swstep;

    switch (swstep) {
      case "step3":

        break;
    }
  }
});