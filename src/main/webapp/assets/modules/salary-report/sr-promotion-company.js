techlooper.directive("srPromotionCompany", function ($http, validatorService, vnwConfigService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-promotion-company.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.showPromotion = function () {
        delete scope.state.showAskPromotion;
        scope.state.showPromotionForm = true;
        if($('#txtEmailPromotion').val() == ''){
          $('#txtEmailPromotion').val(scope.$parent.email);
          $('#txtEmailPromotion').attr('value',scope.$parent.email);
        }
      }

      scope.sendCitibankPromotion = function () {
        var error = validatorService.validate($(".partner-company-form").find('input'));
        scope.error = error;
        if (!$.isEmptyObject(error)) {
          return;
        }

        if (scope.promotion.paymentMethod !== 'BANK_TRANSFER') {
          scope.state.showThanksCash = true;
          return;
        }
        var emailVal = $('#txtEmailPromotion');
        scope.$parent.email = emailVal.val();
        if($('#txtEmailReport').val() == ''){
          $('#txtEmailReport').val(scope.$parent.email);
          $('#txtEmailReport').attr('value',scope.$parent.email);
        }
        if($('#txtEmailJobAlert').val() == ''){
          $('#txtEmailJobAlert').val(scope.$parent.email);
          $('#txtEmailJobAlert').attr('value',scope.$parent.email);
        }
        scope.promotion.salaryReviewId = scope.salaryReview.createdDateTime;
        $http.post("promotion/citibank/creditCard", scope.promotion)
          .success(function () {
            localStorage.setItem('PROMOTION-KEY', 'yes');
          }).error(function() {
             localStorage.setItem('PROMOTION-KEY', 'no');
          });
        $('.partner-company-content').hide();
        //delete scope.state.showPromotion;
        scope.state.showThanksBankTransfer = true;
      }

      $http.get("promotion/citibank/title/" + vnwConfigService.getLang(), {transformResponse: function (d, h) {return d;}})
        .success(function (text) {
          scope.promotionCitibankTitle = text;
        });
    }
  }
})