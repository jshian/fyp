var accountApp = angular.module('account', []);
var accountGetAccountForStatementApiUrl = "/account/GetAllForStatement";
var accountGetTradeApiUrl = "/account/trade/Get?stockInTradeId={{stockInTradeId}}&days={{days}}";
var accountGetTransactionApiUrl = "/account/transaction/Get?accountId={{accountId}}&days={{days}}";
//For Historical Statement
accountApp.controller('accountGetAccountAndStockInTradeListCtrl', function($scope, $http) {
  //$http.get("/account/GetList/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var accounts = [];
	var stockInTrades = [];
	$.ajax({
		type: "GET",
		url: accountGetAccountForStatementApiUrl,
		headers : 
		{
			"Authorization" : getCookie("Authorization")
		},
		success: function(data)
			{
				accounts = data.data;
				console.log(JSON.stringify(accounts));
				accounts.forEach(function(account){
					if (account.category == "Stock"){
						stockInTrades = account.stockInTrades;
						stockInTrades.unshift(
							{
								"stockInTradeId" : 0,
								"code":""
							}
						);
						console.log(JSON.stringify(stockInTrades));
					}
				});
				$scope.accounts = accounts;
				$scope.stockInTrades = stockInTrades;
				$scope.data.stockSelected = stockInTrades.find(element => element.stockInTradeId == 0);
				$scope.data.accountSelected = accounts.find(element => element.category == "Cash");
				$scope.$broadcast("AccountOnChangeEvent");
				$scope.$apply();
			}
	});
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
	$scope.data.periodSelected = periods.find(element => element.value == 30);
	$scope.data.stockSelected = {};
	$scope.data.accountSelected = {};
	$scope.accounts = accounts;
	$scope.stockInTrades = stockInTrades;
	$scope.periods = periods;
	$scope.updateStatement = function() {
		//$http.get("/account/GetList/{userId}")
	  //.then(function (response) {$scope.names = response.data.records;});
	  
	   if($scope.data.stockSelected.stockInTradeId == 0){
		   document.getElementById("transaction-table").style.display = "block";
		   document.getElementById("trade-table").style.display = "none";
		   $scope.$broadcast("AccountOnChangeEvent");
	   }else{
		   document.getElementById("transaction-table").style.display = "none";
		   document.getElementById("trade-table").style.display = "block";
		   $scope.$broadcast("StockOnChangeEvent");
	   }
	};
	$scope.updateAccount = function() {
		if ($scope.data.accountSelected.category == "Stock"){
			document.getElementById("selected-stock-div").style.visibility = "visible";
		}else{
			document.getElementById("selected-stock-div").style.visibility = "hidden";
			$scope.data.stockSelected = stockInTrades.find(element => element.stockInTradeId == 0);
		}
	    $scope.updateStatement();
	};
});

accountApp.controller('accountGetTradeCtrl', function($scope, $http) {
	$scope.stockStatement = [];
	$scope.$on("StockOnChangeEvent",getTrade);
	function getTrade(evt){
		var url = accountGetTradeApiUrl;
		url = url.replace("{{stockInTradeId}}", $scope.data.stockSelected.stockInTradeId);
		url = url.replace("{{days}}", $scope.data.periodSelected.value);
		$.ajax({
			type: "GET",
			url: url,
			headers : 
			{
				"Authorization" : getCookie("Authorization")
			},
			success: function(data)
				{
					$scope.stockStatement = data.data;
					$scope.stockStatement.forEach(function(trade){
						trade.date = trade.date.substring(0, 10);
						if(trade.profit == 0){
							trade.profit = null;
						}
						if (trade.action){
							trade.action = "Buy";
						}else{
							trade.action = "Sell";
							
						}
					});
					console.log(JSON.stringify($scope.stockStatement));
					$scope.$apply();
				}
		});
	}
});

accountApp.controller('accountGetTransactionCtrl', function($scope, $http) {
	$scope.statement = [];
	$scope.$on("AccountOnChangeEvent",getTransaction);
	function getTransaction(evt){
		var url = accountGetTransactionApiUrl;
		url = url.replace("{{accountId}}", $scope.data.accountSelected.id);
		url = url.replace("{{days}}", $scope.data.periodSelected.value);
		$.ajax({
			type: "GET",
			url: url,
			headers : 
			{
				"Authorization" : getCookie("Authorization")
			},
			success: function(data)
				{
					$scope.statement = data.data;
					console.log(JSON.stringify($scope.statement));
					$scope.$apply();
				}
		});
	}
});