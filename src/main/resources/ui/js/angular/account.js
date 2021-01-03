var accountApp = angular.module('account', []);


//For main page
accountApp.controller('accountGetAccountChartsCtrl', function($scope, $http) {
  //$http.get("/account/account/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	var accounts = [
		{
			"accountCategory" : "ETF",
			"amount" : 1000000
		},
		{
			"accountCategory" : "Cash",
			"amount" : 500000
		},
		{
			"accountCategory" : "Stock",
			"amount" : 577000
		}
	]
	$scope.totalAsset = getAccountChartsAndSum(accounts);
});

//For main page
accountApp.controller('accountGetStockInTradeChartsCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetCharts/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var stocks = [
		{
			"stockInTradeId" : 1,
			"symbol":"AAPL",
			"noOfShare":1000,
			"currentPrice":112,
			"cost":100
		},
		{
			"stockInTradeId" : 2,
			"symbol":"TSLA",
			"noOfShare":500,
			"currentPrice":690,
			"cost":400
		},
		{
			"stockInTradeId" : 3,
			"symbol":"ABNB",
			"noOfShare":1000,
			"currentPrice":120,
			"cost":160
		}
	];
	$scope.stockSum = getStockCharts(stocks);
	$scope.stocks = getStockTables(stocks);
});

//For main page
accountApp.controller('accountGetStockInTradeCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var stockInTrades = [
		{
			"stockInTradeId" : 1,
			"symbol":"AAPL",
			"noOfShare":1000,
			"currentPrice":112,
			"riskIndex" : 0.4,
			"expectedPrice" : 116,
			"expectedDivident": 1.0,
			"holdingPeriod" : 60,
			"cost":100
		},
		{
			"stockInTradeId" : 2,
			"symbol":"TSLA",
			"noOfShare":500,
			"currentPrice":690,
			"riskIndex" : 0.92,
			"expectedPrice" : 600,
			"expectedDivident": 3,
			"holdingPeriod" : 0,
			"cost":400
		},
		{
			"stockInTradeId" : 3,
			"symbol":"ABNB",
			"noOfShare":1000,
			"currentPrice":120,
			"riskIndex" : 0.2,
			"expectedPrice" : 130,
			"expectedDivident": 0.5,
			"holdingPeriod" : 30,
			"cost":160
		}
	];
	$scope.stockInTrades = stockInTrades;
});




//For Historical Statement
accountApp.controller('accountGetAccountAndStockInTradeListCtrl', function($scope, $http) {
  //$http.get("/account/GetList/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var accounts = [
		{
			"accountId" : 1,
			"accountCategory" : "ETF",
			"amount" : 1000000,
			"stockInTrades" : null
		},
		{
			"accountId" : 2,
			"accountCategory" : "Cash",
			"amount" : 500000,
			"stockInTrades" : null
		},
		{
			"accountId" : 3,
			"accountCategory" : "Stock",
			"amount" : 577000,
			"stockInTrades" : [
				{
					"stockInTradeId" : 1,
					"symbol":"AAPL"
				},
				{
					"stockInTradeId" : 2,
					"symbol":"TSLA"
				},
				{
					"stockInTradeId" : 3,
					"symbol":"ABNB"
				},
			]
		}
	];
	var stockInTrades = [];
	accounts.forEach(function(account){
		if (account.accountCategory == "Stock"){
			stockInTrades = account.stockInTrades;
		}
	});
	stockInTrades.unshift(
		{
			"stockInTradeId" : 0,
			"symbol":""
		}
	)
	periods = [
		{
			"value" : 30,
			"label" : "Last 30 days"
		},
		{
			"value" : 60,
			"label" : "Last 60 days"
		},
		{
			"value" : 180,
			"label" : "Last 180 days"
		}
	];
	$scope.data = {};
	$scope.accounts = accounts;
	$scope.stockInTrades = stockInTrades;
	$scope.periods = periods;
	$scope.data.periodSelected = periods.find(element => element.value == 30);
	$scope.data.stockSelected = stockInTrades.find(element => element.stockInTradeId == 0);
	$scope.data.accountSelected = accounts.find(element => element.accountCategory == "Cash");
	$scope.statement = 
	   [
			{
				"id" : 1,
				"date" : "2020-12-22 T11:20:33.123456",
				"accountTo" : "Stock",
				"stockSymbol" : "AAPL",
				"amount" : -100000,
				"balance" : 900000
		   },
		   {
				"id" : 2,
				"date" : "2020-12-22 T11:20:33.123456",
				"accountTo" : "ETF",
				"stockSymbol" : "",
				"amount" : 50000,
				"balance" : 950000
		   },
	   ]
   $scope.stockStatement = 
	   [
		   {
				"id" : 1,
				"date" : "2020-12-22 T11:20:33.123456",
				"numOfShare" : 1000,
				"price" : 100,
				"totalShare" : 1000,
				"avgCost" : 100
		   }
	   ]
	$scope.updateStatement = function() {
		//$http.get("/account/GetList/{userId}")
	  //.then(function (response) {$scope.names = response.data.records;});
	  
	   if($scope.data.stockSelected.stockInTradeId == 0){
		   document.getElementById("transaction-table").style.display = "block";
		   document.getElementById("trade-table").style.display = "none";
	   }else{
		   document.getElementById("transaction-table").style.display = "none";
		   document.getElementById("trade-table").style.display = "block";
		   
	   }
	};
	$scope.updateAccount = function() {
		if ($scope.data.accountSelected.accountCategory == "Stock"){
			document.getElementById("selected-stock-div").style.visibility = "visible";
		}else{
			document.getElementById("selected-stock-div").style.visibility = "hidden";
			$scope.data.stockSelected = stockInTrades.find(element => element.stockInTradeId == 0);
		}
	    $scope.updateStatement();
	};
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
	
	return  new Intl.NumberFormat().format(stockSum);
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
		for (const property in stockDto) {
			if (property != "symbol" && property != "stockInTradeId")
				stockDto[property] = new Intl.NumberFormat().format(stockDto[property]);
		};
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
		data.label = account.accountCategory;
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
	return  new Intl.NumberFormat().format(assetSum);
}


function labelFormatter(label, series) {
	return '<div style="font-size:13px; text-align:center; padding:2px; color: #fff; font-weight: 600;">'
	  + label
	  + '<br>'
	  + Math.round(series.percent) + '%</div>'
}

