techlooper.directive("srPromotionCompany", function ($http, validatorService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-promotion-company.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.showPromotion = function () {
        delete scope.state.showAskPromotion;
        scope.state.showPromotionForm = true;
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

        scope.promotion.salaryReviewId = scope.salaryReview.createdDateTime;
        $http.post("promotion/citibank/creditCard", scope.promotion)
          .success(function () {
            localStorage.setItem('PROMOTION-KEY', 'yes');
          });
        $('.partner-company-content').hide();
        //delete scope.state.showPromotion;
        scope.state.showThanksBankTransfer = true;
      }
    }
  }
})