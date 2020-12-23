var userApp = angular.module('user', []);

userApp.controller('userAddInfoCtrl', function($scope, $http) {
	
	
})

userApp.controller('userGetInfoCtrl', function($scope, $http) {
	$scope.userInfo = {
		"id":1,
		"dateOfBirth":"1998-01-01",
		"maritalStatus":0,
		"familyNum":4,
		"childNum":1,
		"monthlyIncome":100000,
		"monthlyExpense":30000,
		"livingExpense":10000,
		"housingExpense":10000,
		"taxExpense":100000,
		"miscelExpense":10000,
		"expectedProfit":1,
		"expectedRisk":0.6,
		"investmentGoal":10000000,
		"targetYears":10,
		"totalAsset":5000000,
		"debt":1000000,
		"debtRate":3.25,
		"equity":2000000,
		"cashFlow":2000000,
		"commission":0.5,
		"dividendCollectionFee":3
	}
	$scope.userNewInfo = {
		"id":1,
		"dateOfBirth":"1998-01-01",
		"maritalStatus":0,
		"familyNum":4,
		"childNum":1,
		"monthlyIncome":100000,
		"monthlyExpense":30000,
		"livingExpense":10000,
		"housingExpense":10000,
		"taxExpense":100000,
		"miscelExpense":10000,
		"expectedProfit":1,
		"expectedRisk":0.6,
		"investmentGoal":10000000,
		"targetYears":10,
		"totalAsset":5000000,
		"debt":1000000,
		"debtRate":3.25,
		"equity":2000000,
		"cashFlow":2000000,
		"commission":0.5,
		"dividendCollectionFee":3
	}
})

userApp.controller('userUpdateInfoCtrl', function($scope, $http) {
	
	
})