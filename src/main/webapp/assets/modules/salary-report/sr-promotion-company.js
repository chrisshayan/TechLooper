techlooper.directive("srPromotionCompany", function ($http, validatorService, vnwConfigService, localStorageService) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/salary-report/sr-promotion-company.tem.html",
    link: function (scope, element, attr, ctrl) {
      scope.showPromotion = function () {
        delete scope.state.showAskPromotion;
        scope.state.showPromotionForm = true;
        //if($('#txtEmailPromotion').val() == ''){
        //  $('#txtEmailPromotion').val(scope.$parent.email);
        //}
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

        scope.$emit("email changed", scope.promotion.email);

        scope.promotion.salaryReviewId = scope.salaryReview.createdDateTime;
        $http.post("promotion/citibank/creditCard", scope.promotion)
          .success(function () {
            localStorageService.set('PROMOTION-KEY', 'yes');
          }).error(function() {
            localStorageService.set('PROMOTION-KEY', 'yes');
          });
        $('.partner-company-content').hide();
        //delete scope.state.showPromotion;
        scope.state.showThanksBankTransfer = true;
      }

      $http.get("promotion/citibank/title/" + vnwConfigService.getLang(), {transformResponse: function (d, h) {return d;}})
        .success(function (text) {
          scope.promotionCitibankTitle = text;
        });

      scope.$on("email changed", function(event, email) {
        if (scope.promotion && !scope.promotion.email) {
          scope.promotion.email = email;
        }
      });
      scope.$on("state change success", function() {scope.promotion = {email: ""};})
    }
  }
})