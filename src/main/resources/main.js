
//initialize selector functions
$(document).ready(function() {
    $('select').formSelect();
});

/*
google.charts.load('current', {'packages':['corechart', 'bar']});
google.charts.setOnLoadCallback(drawChart);

function drawChart(data, title) {


}
*/

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