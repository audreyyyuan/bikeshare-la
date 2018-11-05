
//initialize selector functions
$(document).ready(function() {
    $('select').formSelect();
});

//statistics top portion
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
		axios.get('https://ady8-bikeshare.herokuapp.com/statistics')
				.then(response => {
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

//selection and graph portion
var select = new Vue({
	el: '#selector',
	data: {
		selected: 0,
		blurb: 'test'
	},

	methods: {

		loadAndDrawChart: function(option) {

			var path = '';
			var title = '';

			if(option == 1) {
				path = '/passholder';
				title = 'Average Trip Duration of Each Pass Plan';
				subtitle = 'in minutes; for each trip type each month';
				this.blurb = 'The chart displays the average length of each time trip, based on the type of trip and what pass the rider has. ' + 
						'It would be expected that the average duration of using the bike for a round trip would be longer than a one way trip on ' + 
						' the bike. While this is true for most of the trips for different pass types, the average duration of those who do a one ' +
						' way bike trip on a staff pass is greater than the average duration of customers who use their staff pass for round trips. ' + 
						'This is interesting, as round trips are normally twice the distance and duration of one way trips. We can see that ' + 
						'the majority of people on the staff pass probably use it to commute, causing it only to be a one way trip. For the other ' + 
						'passes, we can imagine that there are people who use the bike sharing program leisurely and recreationally, and those who ' + 
						'use it as a form of commuting.';
			}

			else if (option == 2) {
				path = '/month';
				title = 'Number of Bike Trips';
				subtitle = 'for each month';
				this.blurb = 'Note: The chart displays that there are no rides for April, May, and June, since the data goes extends from July ' + 
						'2016 to March 2017. \n From this chart, we can see that the bike share program reaches its peak usability during ' + 
						'the late summer months of August and September, and then decreases for the rest of the year.' +
						' Although this bike sharing program is located in Los Angeles, where the weather' + 
						' is approximately the same throughout the year, there is still a large difference between the numer of bike share users '+ 
						'in the summer and winter. The differnce in usage may be due to that it is more enjoyable to bike in summer. ' +
						'We can also tell that the number of people who use the Bike Share program for round trips and one way trips approximately' + 
						' follows the same trend, as the number of one way and round trip is highest in late summer, and both decrease until the next year.';
			}

			else if (option == 3) {
				path = '/avgdur';
				title = 'Average Bike Trip Duration Throughout the Year';
				subtitle = 'in minutes';
				this.blurb = 'Note: The chart displays that there are no rides for April, May, and June, since the data goes extends from July ' + 
						'2016 to March 2017. \n The average duration of a bike trip is the highest at the beginning of the year, in winter, and ' + 
						'decreases throughout the year, where the average duration of a trip in September is lowest. Along with the previous chart,' + 
						' we can determine that people seem to take fewer, but longer bike trips in the early months throughout the year, and then' + 
						' go on shorter trips as the year goes on.';
			}

			else if (option == 4) {
				path = '/morning';
				title = 'Morning Bike Rides';
				subtitle = 'the number of early morning (5:00-8:00) bike rides taken throughout the year';
				this.blurb = 'From this chart, we can see that the number of early morning rides taken each month is very low for round trips, but ' + 
						'higher for one way trips. However, the number of one way trips taken is many times higher than the number of one way trips.' + 
						' Most of these one way trips are probably due to bike share customers commuting to work early in the morning. Therefore, ' +
						'it seems as if many of LA\'s population goes to work early. We can also clearly tell that the number of people who take ' + 
						'recreational bike trips in the morning are very few for the whole month. Therefore, these bikes are most likely used for ' + 
						'commuting and traveling, but not often for others.';
			}

			else if(option == 5) {
				path = '/seasonal';
				title = 'Throughout Seasons-- Average Trip Duration for Each Month'
				subtitle = 'in minutes; for each plan each month';
				this.blurb = 'Note: The chart displays that there are no rides for April, May, and June, since the data goes extends from July ' + 
						'2016 to March 2017. \n We can immediately realize that there are no staff passes bought from January to September, and ' + 
						'businesses seem to buy these plans only near the end of the year. However, the average duration of customers on the staff' + 
						' plans is a lot higher than others. The average duration for those on a plan (flex or monthly) is consistent throughout ' + 
						'whole year, meaning that those who purchase these plans consistenly use the bikes for the same reason every day / trip.' +
						' The large average duration of those customers without a plan tends to that these customers do not have a need to use ' + 
						'the bikes very often, but will go on long trips.';
			}

			else if(option == 6) {
				path = '/starttime';
				title = 'Start Times'
				subtitle = '';
				this.blurb = 'The maximimum number of bike trips occuring is from 16:00-18:00, which is normally when people leave work and ' + 
						'commute home. The next most common time is around noon, during lunch time, meaning that many customers probably use ' + 
						'the bike sharing program to get from one place to another, even throughout the day. We can also tell that there are' + 
						' less people who use bike sharing to commute to work in the morning, than there are in the afternoon to commute from' + 
						' work. There is also a significant number of trips occuring in the evening, meaning that there are people who probably' + 
						'recreationally use these bikes after work.';
			}

			axios.get('http://localhost:8080' + path)
			//axios.get('https://ady8-bikeshare.herokuapp.com' + path)
					.then(response => {
						var json = JSON.stringify(response.data);
						var data = new google.visualization.DataTable(json);

						var options = {
							title: title,
							subtitle: subtitle,
							bar: {groupWidth: "95%"},
						};

						if(option == 1 || option == 2 || option == 4 || option == 5) {

							var chart = new google.charts.Bar(document.getElementById('chart_div'));
							chart.draw(data, google.charts.Bar.convertOptions(options));
						}

						else if(option == 3) {
							chart = new google.charts.Line(document.getElementById('chart_div'));
							chart.draw(data, google.charts.Line.convertOptions(options));
						}

						else if(option == 6) {
							options = {
								title: title,
								subtitle: subtitle,
								bar: {groupWidth: "95%"},
								histogram: {bucketSize: 1},
							}
							chart = new google.visualization.Histogram(document.getElementById('chart_div'));
							chart.buckets
							chart.draw(data, options);
						}

					})
					.catch(function(error) {
						console.log(error);
					});
		}
	},

	mounted() {
		this.loadAndDrawChart(0);
	}
});

select.$watch('selected', (function (value, oldValue) {

	select.loadAndDrawChart(value);
}));


//selections + chart drawing
google.charts.load('current', {'packages':['corechart', 'bar', 'line']});
google.charts.setOnLoadCallback(select.loadAndDrawChart(0));
