techlooper.directive("general-info", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/main/search-form.tem.html"
  }
}).directive("reasonUsed", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/main/reason.tem.html"
  }
}).directive("ourCustomers", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/main/our-customers.tem.html"
  }
}).directive("customersSay", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/main/customers-say.tem.html"
  }
}).directive("feedbackForm", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/main/feedback.tem.html"
  }
}).directive("locationMap", function () {
  return {
    restrict: "A",
    replace: true,
    templateUrl: "modules/talent-search/main/location.tem.html"
  }
});