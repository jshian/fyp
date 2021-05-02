var getAllStockApiUrl = "/stock/GetAll";
var accountGetChartsApiUrl = "/account/GetCharts";
var accountGetStockInTradeApiUrl = "/account/stockInTrade";
var stockGetStockEventApiUrl = "/stock/GetAllEvent";
var indexApp = angular.module('index', ['frapontillo.bootstrap-switch', 'ui.bootstrap']);

indexApp.filter('start', function () {
	return function (input, start) {
		if (!input || !input.length) { return; }
		start = +start;
		return input.slice(start);
	};
});

indexApp.controller('stockGetNewsCtrl', function($scope, $http) {
  //$http.get("/stock/news/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	$scope.stockNews = [];
	$.ajax({
		type: "GET",
		url: stockGetStockEventApiUrl,
		headers : 
		{
			"Authorization" : getCookie("Authorization")
		},
		success: function(data)
			{
				$scope.stockNews = data.data;
				$scope.currentPage = 1;
				$scope.$apply();
			}
	});
	$scope.perPage = 5;
    $scope.maxSize = 5;
    $scope.setPage = function (pageNo) {
        $scope.currentPage = pageNo;
    };
});


//For main page
indexApp.controller('accountGetAccountChartsCtrl', function($scope, $http) {
  //$http.get("/account/account/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
   var accounts = []
	$.ajax({
		type: "GET",
		url: accountGetChartsApiUrl,
		headers : 
		{
			"Authorization" : getCookie("Authorization")
		},
		success: function(data)
			{
				accounts = data.data;
				$scope.totalAsset = getAccountChartsAndSum(accounts);
				$scope.$apply();
			}
	});
});

//For main page
indexApp.controller('accountGetStockInTradeChartsCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetCharts/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	var stocks = []
	$.ajax({
		type: "GET",
		url: accountGetStockInTradeApiUrl,
		headers : 
		{
			"Authorization" : getCookie("Authorization")
		},
		success: function(data)
			{
				stocks = data.data;
				$scope.stocks = getStockTables(stocks);
				$scope.stockSum = getStockCharts(stocks);
				$scope.$apply();
			}
	});
});

//For main page
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
	
	$.plot('#stock-donut-chart', donutData, {
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
	
	return stockSum;
}

//For main page
function getStockTables(stocks){
	var stockDtos = [];
	stocks.forEach(function(stock) {
		var stockDto = {};
		stock.amount = stock.currentPrice * stock.noOfShare;
		stock.change = (stock.currentPrice-stock.cost)* stock.noOfShare;
		stock.changePercent = (stock.currentPrice-stock.cost)/ stock.cost *100;
		Object.assign(stockDto, stock);
		if(stock.change > 0){
			stockDto.textClass = "text-success mr-1";
			stockDto.arrowClass = "fas fa-arrow-up ";
		}
		if(stock.change < 0){
			stockDto.textClass = "text-danger mr-1";
			stockDto.arrowClass = "fas fa-arrow-down";
		}
		stockDtos.push(stockDto);
	});
	return stockDtos;
}

//For main page
function getAccountChartsAndSum(accounts){
	var assetSum = 0;
	var donutData = [];
	for(var i = 0; i < accounts.length; i++){
		var account = accounts[i];
		assetSum += account.amount;
		var data = {};
		data.label = account.category;
		data.data = account.amount;
		data.color = "#3333"+ Math.round(255/accounts.length * (i+1)).toString(16);
		donutData.push(data);
	};
	$.plot('#account-donut-chart', donutData, {
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
	return assetSum;
}


function labelFormatter(label, series) {
	return '<div style="font-size:13px; text-align:center; padding:2px; color: #fff; font-weight: 600;">'
	  + label
	  + '<br>'
	  + Math.round(series.percent) + '%</div>'
}
