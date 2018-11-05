
//initialize selector functions
$(document).ready(function() {
    $('select').formSelect();
});


google.charts.load('current', {'packages':['corechart', 'bar', 'line']});
google.charts.setOnLoadCallback(loadAndDrawChart);

function loadAndDrawChart(option) {

	var path = '';
	var title = '';

	if(option == 0) {
		path = '/passholder';
		title = 'Average Trip Duration of Each Pass Plan';
	}

	else if (option == 1) {
		path = '/month';
		title = 'Seasonal-- Number of Bike Trips';
	}

	else if (option == 2) {
		path = '/avgdur';
		title = 'Average Bike Trip Duration';
	}

	axios.get('http://localhost:8080' + path)
			.then(response => {
				console.log(response);
				var json = JSON.stringify(response.data);
				console.log(json);
				var data = new google.visualization.DataTable(json);

				var options = {
					title: title,
					bar: {groupWidth: "95%"},
			        legend: { position: "bottom" },
				};

				if(option == 0 || option == 1) {

					var chart = new google.charts.Bar(document.getElementById('chart_div'));
					chart.draw(data, google.charts.Bar.convertOptions(options));
				}

				else if(option == 2) {
					chart = new google.charts.Line(document.getElementById('chart_div'));
					chart.draw(data, google.charts.Line.convertOptions(options));
				}

			})
			.catch(function(error) {
				console.log(error);
			});

	//console.log('finished request');
	//console.log(this.json);

}

var stats = new Vue({
	el: '#statistics',
	data: {
			total: 0,
			startLoc: 'Loading...',
			endLoc: 'Loading...',
			avgdist: 0,
			commute: 0
	},

	mounted () {
		axios.get('http://localhost:8080/statistics')
				.then(response => {
					console.log(response);
					this.total = response.data.totalTrips;
					this.avgdist = response.data.averageDistance;
					this.startLoc = response.data.mostPopularStartingStation;
					this.endLoc = response.data.mostPopularEndingStation;
					this.commute = response.data.commuteTrips;

				})
				.catch(error => {
					console.log(error);
				});
	},
});

var select = new Vue({
	el: '#selector',
	data: {
		selected: 0
	},

	mounted() {
		loadAndDrawChart(0);
	}
});

select.$watch('selected', function (value, oldValue) {

	loadAndDrawChart(value);
});
