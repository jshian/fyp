var getAllStockApiUrl = "/stock/page/";
var stockApp = angular.module('stock', ['frapontillo.bootstrap-switch', 'ui.bootstrap']);

stockApp.controller('pageCtrl', function($scope, $http) {
	const queryString = window.location.search;
	const urlParams = new URLSearchParams(queryString);
	const code = urlParams.get('code');
	console.log(code);
	$scope.data = {};
	$scope.data.stock = {};
	if(code != null){
		$.ajax({
			type: "GET",
			url: getAllStockApiUrl + code,
			success: function(data)
			{
				$scope.data = data.data;
				$scope.currentPage = 1;

				const config = {
				  type: 'line',
				  data: {
					labels : data.data.labels,
					datasets: [{
					  label: "Historical Price",
					  fill: false,
					  borderColor : 'black',
					  borderWidth: 1,
					  radius: 0,
					  data: data.data.historicalPriceList,
					},
					{
					  label: "Prediction",
					  fill: false,
					  borderColor : 'blue',
					  borderWidth: 1,
					  radius: 0,
					  data: data.data.predictedPriceList,
					}]
				  },options: {
					plugins: {
					  title: {
						text: 'Chart.js Time Scale',
						display: true
					  }
					},
					scales: {
					  x: {
						type: 'time',
						time: {
						  format: "YYYY-MM-DD",
						  tooltipFormat: 'll'
						},
						title: {
						  display: true,
						  text: 'Date'
						}
					  },
					  y: {
						title: {
						  display: true,
						  text: 'value'
						}
					  }
					},
				  },
				};
                var ctx = document.getElementById('canvas').getContext('2d');
                window.myLine = new Chart(ctx, config);
				$scope.$apply();

			},
			error: function (request, status, error) {
				console.log(status);
			},
			always: function(data)
			{
				$scope.loading=false;
			}
		});

        $scope.perPage = 5;
        $scope.maxSize = 5;
        $scope.setPage = function (pageNo) {
            $scope.currentPage = pageNo;
        };
	};
});



stockApp.filter('start', function () {
	return function (input, start) {
		if (!input || !input.length) { return; }
		start = +start;
		return input.slice(start);
	};
});

function getStockCharts(stocks){
	var donutData = [];
	var stockSum = 0;
	for(var i = 0; i < stocks.length; i++){
		var stock = stocks[i];
		stockSum += stock.noOfShare * stock.currentPrice;
		var data = {};
		data.label = stock.symbol;
		data.data = stock.noOfShare * stock.currentPrice;
		data.color = "#3333"+ Math.round(255/stocks.length * (i+1)).toString(16);
		donutData.push(data);
	}
	
	$.plot('#price-chart', donutData, {
		series: {
				pie: {
				  show       : true,
				  radius     : 1,
				  innerRadius: 0.5,
				  label      : {
					show     : true,
					radius   : 2 / 3,
					formatter: labelFormatter,
					threshold: 0.1
				}
			}
		},
		legend: {
			show: false
		}
	});
}