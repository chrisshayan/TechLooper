techlooper.directive('autoTagbox', function ($timeout) {
  return {
    restrict: "E",
    replace: true,
    templateUrl: "modules/common/auto-tagbox.tem.html",
    scope: {
      tags: "=",
      config: "="
    },
    link: function (scope, element, attr, ctrl) {
      scope.errors = [];
      scope.removeTag = function (tag) {
        scope.tags.splice(scope.tags.indexOf(tag), 1);
        scope.highlightDemandSkill(tag);
        scope.errors.length = 0;
      }
      scope.tags = scope.tags || [];
      scope.addTag = function (tag) {
        if (!scope.config.newTag || !scope.config.newTag.length) {
          return;
        }
        scope.errors.length = 0;

        var lowerTag = scope.config.newTag.toLowerCase();
        if (scope.tags.length >= 50) {
          return scope.errors.push("maximum50");
        }
        else if (scope.tags.indexOf(lowerTag) > -1) {
          return scope.errors.push("hasExist");
        }
        else if (scope.config.newTag.length > 40) {
          return scope.errors.push("tooLong");
        }

        scope.tags.push(lowerTag);
        scope.config.newTag = "";
      }

      scope.$on("state change success", function () {scope.errors.length = 0;});

      scope.demandList = [
        {name: 'AngularJS'},
        {name: 'Spring'},
        {name: 'Project Management'},
        {name: 'Business and Service Oriented'},
        {name: 'Self Motivation'}];
      scope.addDemandSkill = function(obj) {
        if(!$(obj.target).hasClass('added')){
          var lowerTag = $.trim(obj.target.textContent.toLowerCase());
          console.log(lowerTag);
          if (scope.tags.length >= 50) {
            return scope.errors.push("maximum50");
          }
          if ($.inArray(lowerTag, scope.tags) !== -1)
          {
            return scope.errors.push("hasExist");
          }
          scope.tags.push(lowerTag);
          scope.errors.length = 0;
          $(obj.target).addClass('added');
        }
      }
      scope.highlightDemandSkill = function(tag){
        var list = $('.demand-skills').find('li');
        list.each(function(){
          if($.trim($(this).text().toLowerCase()) == tag.toLowerCase()){
            $(this).removeClass('added');
          }
        });
      }
    }
  }
});