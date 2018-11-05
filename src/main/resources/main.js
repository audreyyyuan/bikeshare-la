
//initialize selector functions
$(document).ready(function() {
    $('select').formSelect();
});

//selections + chart drawing
google.charts.load('current', {'packages':['corechart', 'bar', 'line']});
google.charts.setOnLoadCallback(loadAndDrawChart);

function loadAndDrawChart(option) {

	var path = '';
	var title = '';

	if(option == 1) {
		path = '/passholder';
		title = 'Average Trip Duration of Each Pass Plan';
		subtitle = 'in minutes';
	}

	else if (option == 2) {
		path = '/month';
		title = 'Seasonal-- Number of Bike Trips';
		subtitle = ''
	}

	else if (option == 3) {
		path = '/avgdur';
		title = 'Average Bike Trip Duration Throughout the Year';
		subtitle = 'in minutes';
	}

	axios.get('http://localhost:8080' + path)
			.then(response => {
				console.log(response);
				var json = JSON.stringify(response.data);
				console.log(json);
				var data = new google.visualization.DataTable(json);

				var options = {
					title: title,
					subtitle: subtitle,
					bar: {groupWidth: "95%"},
				};

				if(option == 1 || option == 2) {

					var chart = new google.charts.Bar(document.getElementById('chart_div'));
					chart.draw(data, google.charts.Bar.convertOptions(options));
				}

				else if(option == 3) {
					chart = new google.charts.Line(document.getElementById('chart_div'));
					chart.draw(data, google.charts.Line.convertOptions(options));
				}

			})
			.catch(function(error) {
				console.log(error);
			});

}

//statistics blurb
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

//selection
var select = new Vue({
	el: '#selector',
	data: {
		selected: 0
	},

	mounted() {
		this.loadAndDrawChart(0);
	}
});

select.$watch('selected', function (value, oldValue) {

	loadAndDrawChart(value);
});
