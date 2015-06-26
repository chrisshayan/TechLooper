techlooper.controller('contestController', function ($scope) {
  if(localStorage.NG_TRANSLATE_LANG_KEY === 'vi'){
    $scope.vietmanese = true;
  }else{
    $scope.vietmanese = false;
  }
});