
//initialize selector functions
$(document).ready(function() {
    $('select').formSelect();
});


google.charts.load('current', {'packages':['corechart', 'bar']});
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

	var json = {};
	axios.get('localhost:8080' + path)
			.then(function(response) {
				console.log(response);
				json = response;
			})
			.catch(function(error) {
				console.log(error);
			});

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

}

var stats = new Vue({
	el: '#statistics',
	data: {
		total: 13742,
		distance: 0,
		start: 'Broadway & 3rd',
		end: 'Broadway & 3rd',
		avgdist: 1.55,
		commuter: 0,
	},

	mounted: function () {
		axios.get('https://api.coindesk.com/v1/bpi/currentprice.json')
			.then(function(response) {
				console.log(response);
				/*
				this.total = response.totalTrips;
				this.avgdist = response.averageDistance;
				this.dist = this.avgdist * this.total;
				this.start = response.mostPopularStartingLocation;
				this.end = response.mostPopularEndingLocation;
				this.commuter = response.commuteTrips;
				*/
			})
	}
});

var select = new Vue({
	el: '#selector',
	data: {
		selected: 0
	}
});

select.$watch('selected', function (value, oldValue) {

	loadAndDrawChart(value);
});

/*
var graph = new Vue({

	el: '#graph'
	data: {
		chartdata: {},
		selection: 0
	}

	mounted: function() {
		axios.get('/graph')
			.then(function(response) {
				this.chartdata = response;
				drawChart(this.chartdata);
			})
	}
})
*/