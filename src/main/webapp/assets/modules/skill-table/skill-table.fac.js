angular.module('Skill').factory('skillTableFactory', function() {
	return instance = {
		formatDate: function(){
			var lCur = Date.today().toString("MMM d"),
				fCur = Date.today().add(-7).days().toString("MMM d"),
				current = fCur +' - ' + lCur;

			var lPre = Date.today().add(-8).days().toString("MMM d"),
				fPre = Date.today().add(-7).days().clone(),
				previous = fPre.add(-7).days().toString("MMM d") +' - '+ lPre;

			$('span.curDate').text(current);
			$('span.preDate').text(previous);
		},
		reformatData: function(data){
			var newData = [];
			var icStock = '';
			$.each(data, function(index, value){
				var per = ((parseInt(value.currentCount) - parseInt(value.previousCount))*100)/parseInt(value.previousCount);
				if(per > 0){
					icStock = 'fa-arrow-up ic-blue';
					per = per.toFixed(2);
				}else if(per < 0){
					icStock = 'fa-arrow-down ic-red';
					per = per.toFixed(2);
				}else{
					icStock = '';
					per = 0;
				}
				newData.push({
					'name': value.skill,
					'current': value.currentCount,
					'previous': value.previousCount,
					'change': per,
					'icon': icStock
				});
			});
			return newData;
		}
	};
});