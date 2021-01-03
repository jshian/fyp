var stockApp = angular.module('stock', []);

stockApp.controller('stockGetNewsCtrl', function($scope, $http) {
  //$http.get("/stock/news/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	var stockNews = [
		{
			"id" : 4,
			"date" : "2020-12-22 T11:20:33.123456",
			"stockSymbol" : "AAPL",
			"title" : "AAPL will go above $150"
		},
		{
			"id" : 3,
			"date" : "2020-12-21 T11:20:33.123456",
			"stockSymbol" : "TSLA",
			"title" : "TSLA Drop Drop Drop, IFC's rooftop is very crowded now"
		},
		{
			"id" : 2,
			"date" : "2020-12-20 T11:20:33.123456",
			"stockSymbol" : "BNTX",
			"title" : "BNTX Up Up Up !!!"
		},
		{
			"id" : 1,
			"date" : "2020-12-19 T11:20:33.123456",
			"stockSymbol" : "PFE",
			"title" : "PFE falls"
		},
		{
			"id" : 5,
			"date" : "2020-12-18 T11:20:33.123456",
			"stockSymbol" : "PFE",
			"title" : "PFE falls"
		},
		{
			"id" : 6,
			"date" : "2020-12-18 T11:20:33.123456",
			"stockSymbol" : "PFE",
			"title" : "PFE rises"
		}
	];
	$scope.stockNews = stockNews;
});

stockApp.controller('stockGetNewsCtrl', function($scope, $http) {
  //$http.get("/stock/news/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	var stockNews = [
		{
			"id" : 4,
			"date" : "2020-12-22 T11:20:33.123456",
			"stockSymbol" : "AAPL",
			"title" : "AAPL will go above $150"
		},
		{
			"id" : 3,
			"date" : "2020-12-21 T11:20:33.123456",
			"stockSymbol" : "TSLA",
			"title" : "TSLA Drop Drop Drop, IFC's rooftop is very crowded now"
		},
		{
			"id" : 2,
			"date" : "2020-12-20 T11:20:33.123456",
			"stockSymbol" : "BNTX",
			"title" : "BNTX Up Up Up !!!"
		},
		{
			"id" : 1,
			"date" : "2020-12-19 T11:20:33.123456",
			"stockSymbol" : "PFE",
			"title" : "PFE falls"
		},
		{
			"id" : 5,
			"date" : "2020-12-18 T11:20:33.123456",
			"stockSymbol" : "PFE",
			"title" : "PFE falls"
		},
		{
			"id" : 6,
			"date" : "2020-12-18 T11:20:33.123456",
			"stockSymbol" : "PFE",
			"title" : "PFE rises"
		}
	];
	$scope.stockNews = stockNews;
});


//For Portfolio Managemet
stockApp.controller('stockGetRecommendationCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var stockRecommendations = [
		{
			"stockId" : 1,
			"symbol":"JPM",
			"noOfShare":1000,
			"currentPrice":112,
			"riskIndex" : 0.4,
			"expectedPrice" : 116,
			"holdingPeriod" : 60,
			"expectedDivident": 0.4
		},
		{
			"stockId" : 2,
			"symbol":"PFE",
			"noOfShare":500,
			"currentPrice":690,
			"riskIndex" : 0.5,
			"expectedPrice" : 600,
			"holdingPeriod" : 0,
			"expectedDivident": 0.2
		},
		{
			"stockId" : 3,
			"symbol":"BNTX",
			"noOfShare":1000,
			"currentPrice":120,
			"riskIndex" : 0.2,
			"expectedPrice" : 130,
			"holdingPeriod" : 30,
			"expectedDivident": 0.1
		}
	];
	$scope.stockRecommendations= stockRecommendations;
});

//For Portfolio Managemet
stockApp.controller('stockGetStockListCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var stocks = [
		{
			"stockId" : 1,
			"symbol":"AAPL"
		},
		{
			"stockId" : 2,
			"symbol":"TSLA"
		},
		{
			"stockId" : 3,
			"symbol":"BNTX"
		}
	];
	$scope.stocks= stocks;
	$scope.trade = {};
	$scope.trade.stock = stocks[0];
});