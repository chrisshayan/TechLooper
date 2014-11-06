angular.module('Skill').factory('skillTableFactory', function() {
	var skillItem = [];
	return {
		initData: function(data){
			$.each(data, function(index, value){
				var per = ((parseInt(value.currentCount) - parseInt(value.previousCount))*100)/parseInt(value.previousCount);
				skillItem.push({
					'name': value.skill,
					'current': value.currentCount,
					'previous': value.previousCount,
					'change': per.toFixed(2)
				});
			});
			var html = '';
			$.each(skillItem, function(index, value){
				var icStock = '';
				if(value.change > 0){
					icStock = 'fa-arrow-up ic-blue';
				}else if(value.change < 0){
					icStock = 'fa-arrow-down ic-red';
				}else{
					icStock = '';
				}
				html = '<li><div class="col-md-5 colSkill">'+ value.name +'</div>';
				html = html + '<div class="col-md-2 colCurrent">'+ value.current +'</div>'
				html = html + '<div class="col-md-2 colPrevious">'+ value.previous +'</div>';
				html = html + '<div class="col-md-2 colPercentChange"><i class="fa '+ icStock +'"></i> '+ value.change +'%</div>';
				html = html + '<div class="col-md-1 colEdit"><i class="fa fa-edit"></i></div></li>';
				$('.ctrl-lineChart-block ul').append(html);
			});
			formatDate();
		}
	};
	function formatDate(){
		var current = Date.today().toString("d-MMM") + '-' + Date.today().add(-7).days().toString("d-MMM");
		var previous = Date.today().add(-8).toString("d-MMM") + '-' + Date.today().add(-15).days().toString("d-MMM");
		$('span.curDate').text(current);
		$('span.preDate').text(previous);
		
	}
});