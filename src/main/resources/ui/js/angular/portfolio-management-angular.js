var getAllStockApiUrl = "/stock/GetAll";
var addTradeApiUrl = "/account/trade/Add";
var stockGetRecommendationApiUrl = "/stock/recommendation";
var accountGetStockInTradeApiUrl = "/account/stockInTrade/detail";
var portfolioApp = angular.module('portfolio', ['frapontillo.bootstrap-switch', 'ui.bootstrap']);

portfolioApp.controller('pageCtrl', function($scope, $http) {
	$scope.shared = {};
	$scope.selectStock = selectStock;
	$scope.shared.trade = {};
	$scope.shared.trade.action = false;
	$scope.loading=true;
	var stocks = [];
	$.ajax({
		type: "GET",
		url: getAllStockApiUrl,
		headers : 
		{
			"Authorization" : getCookie("Authorization")
		},
		success: function(data)
			{
				stocks = data.data;
				$scope.stocks = stocks;
			},
		always: function(data)
			{
				$scope.loading=false;
			}
	});
	function selectStock(e, action){
		$scope.shared.trade.isInTrade = true;
		$scope.shared.trade.noOfShare = 0;
		$scope.shared.trade.price = 0;
		$scope.shared.trade.stock = $scope.stocks.find(o => o.code == $(e.target).attr('for'));
		$scope.shared.trade.action = action;
		$(".select2-selection__rendered").html($scope.shared.trade.stock.code);
	}
	$scope.$on('on-traded', updateInfo);
	function updateInfo(){
		$scope.$broadcast('on-traded');
	}
});
//For Portfolio Managemet
portfolioApp.controller('stockGetRecommendationCtrl', function($scope, $filter) {
  //$http.get("/account/stockInTrade/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var stockRecommendations = [];
	function getStockRecommendations(){
		$.ajax({
			type: "GET",
			url: stockGetRecommendationApiUrl,
			headers : 
			{
				"Authorization" : getCookie("Authorization")
			},
			success: function(data)
				{
					stockRecommendations = data.data;
					$scope.stockRecommendations = stockRecommendations;
					$scope.filterList = $filter('filter')($scope.stockRecommendations, {});
					$scope.currentPage = 1;
					$scope.$apply();
				}
		});
	}
	$scope.$on('on-traded', getStockRecommendations);
	getStockRecommendations();
	$scope.stockRecommendations= stockRecommendations;
	
	$scope.filtered = {};
	$scope.perPage = 10;
	$scope.maxSize = 5;
	$scope.setPage = function (pageNo) {
		$scope.currentPage = pageNo;
	};

	$scope.$watch('searchText', function (term) {
		var obj = term;
		$scope.filterList = $filter('filter')($scope.stockRecommendations, obj);
		$scope.currentPage = 1;
	});
});
portfolioApp.filter('start', function () {
	return function (input, start) {
		if (!input || !input.length) { return; }
		start = +start;
		return input.slice(start);
	};
});
portfolioApp.controller('accountGetStockInTradeCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	
	var stockInTrades = [];
	function getStockInTrades(){
		$.ajax({
			type: "GET",
			url: accountGetStockInTradeApiUrl,
			headers : 
			{
				"Authorization" : getCookie("Authorization")
			},
			success: function(data)
				{
					stockInTrades = data.data;
					stockInTrades.forEach(function(stockInTrade){
						stockInTrade.textClass = "";
						stockInTrade.arrowClass = "";
						if(stockInTrade.profit > 0){
							stockInTrade.textClass = "text-success mr-1";
							stockInTrade.arrowClass = "fas fa-arrow-up ";
						};
						if(stockInTrade.profit < 0){
							stockInTrade.textClass = "text-danger mr-1";
							stockInTrade.arrowClass = "fas fa-arrow-down ";
						};
					});
					$scope.stockInTrades = stockInTrades;
					$scope.$apply();
				}
		});
	}
	$scope.$on('on-traded', getStockInTrades);
	getStockInTrades();
	$scope.stockInTrades = stockInTrades;
});

portfolioApp.controller('stockTradeCtrl', function($scope, $http) {
  //$http.get("/account/stockInTrade/GetAll/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
		$scope.submit = submit;
		function submit(){
			var method = "POST";
			var url = addTradeApiUrl;
			$scope.shared.trade.stockId = $scope.shared.trade.stock.id;
			$scope.shared.trade.stock = null;
			var json = JSON.stringify($scope.shared.trade);
			$scope.loading=true;
			$.ajax({
				type: method,
				url: url,
				contentType: "application/json; charset=utf-8",
				headers : 
					{
						"Authorization" : getCookie("Authorization")
					},
				dataType: "json",
				data: json,
				success: function(data)
					{
						$scope.$emit('on-traded');
					},
				always: function(data)
					{
						$scope.loading=false;
					}
			});
		}
	});
