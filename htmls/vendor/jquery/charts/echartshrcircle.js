/*人员统计-比例概况*/
(function() {
	$.fn.personrate = function(options) {
		var x, drag = this,
			defaults = {};
		var settings = $.extend(defaults, options);

		// console.log(JSON.stringify(settings));

		for (var i = 0;i < settings.sex_group_info.length;i ++) {
			var id = settings.sex_group_info[i].id;
			var value = settings.sex_group_info[i].rate;
			$('#' + id).circleProgress({
				value: value,
				size: 81,
				thickness: 5,
				fill: {
					gradient: ["#957dff"]
				}
			});
		}

		for (var i = 0;i < settings.years_group_sql.length;i ++) {
			var id = settings.years_group_sql[i].id;
			var value = settings.years_group_sql[i].rate;
			$('#' + id).circleProgress({
				value: value,
				size: 81,
				thickness: 5,
				fill: {
					gradient: ["#30afff"]
				}
			});
		}
	}
})(jQuery);
	