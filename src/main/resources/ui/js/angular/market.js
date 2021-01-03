var marketApp = angular.module('market', []);

marketApp.controller('marketGetNewsCtrl', function($scope, $http) {
  //$http.get("/market/news/{userId}")
  //.then(function (response) {$scope.names = response.data.records;});
	var marketNews = [
		{
			"id" : 4,
			"date" : "2020-12-22 T11:20:33.123456",
			"relatedSector" : "Cosumer-durables",
			"title" : "AAPL will go above $150"
		},
		{
			"id" : 3,
			"date" : "2020-12-21 T11:20:33.123456",
			"relatedSector" : "Cosumer-durables",
			"title" : "TSLA Drop Drop Drop, IFC's rooftop is very crowded now"
		},
		{
			"id" : 2,
			"date" : "2020-12-20 T11:20:33.123456",
			"relatedSector" : "Cosumer-durables",
			"title" : "BNTX Up Up Up !!!"
		},
		{
			"id" : 1,
			"date" : "2020-12-19 T11:20:33.123456",
			"relatedSector" : "Cosumer-durables",
			"title" : "PFE falls"
		},
		{
			"id" : 5,
			"date" : "2020-12-18 T11:20:33.123456",
			"relatedSector" : "Cosumer-durables",
			"title" : "PFE falls"
		},
		{
			"id" : 6,
			"date" : "2020-12-18 T11:20:33.123456",
			"relatedSector" : "Cosumer-durables",
			"title" : "PFE rises"
		}
	];
	$scope.marketNews = marketNews;
});