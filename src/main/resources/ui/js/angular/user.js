var userApp = angular.module('user', []);
var addUserInfoUrl ="user/info/add"
var getUserInfoUrl ="user/info/get"
userApp.controller('userAddInfoCtrl', function($scope, $http) {
	$scope.submitUserInfo =
	function()
		{
			var method = "POST";
			var url = addUserInfoUrl;
			var data = $scope.userInfo;
			data.maritalStatus = parseInt(data.maritalStatus);
			data.dateOfBirth = $('#date-input').val();
			var json = JSON.stringify($scope.userInfo);
			console.log(json)
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
						document.cookie = "IsFirstLogin=0;";
						location.replace('Index.html');
					},
				always: function(data)
					{
						$scope.loading=false;
					}
			});
		}
})

userApp.controller('userGetInfoCtrl', function($scope, $http) {
	$scope.refreshUserInfo = refreshUserInfo;
	maritalStatusOptions = 
		[
			{
				"value" : 0,
				"label" : "Single"
			},
			{
				"value" : 1,
				"label" : "Married"
			},
		];
	$scope.maritalStatusOptions = maritalStatusOptions;
	$scope.loading=true;
	$scope.userInfo = {};
	$scope.userNewInfo = {};
	refreshUserInfo();
	function refreshUserInfo(){
		
		$scope.loading=true;
		$.ajax({
			type: "GET",
			url: getUserInfoUrl,
			headers : 
			{
				"Authorization" : getCookie("Authorization")
			},
			success: function(data)
				{
					var userInfo = data.data;
					if (userInfo.maritalStatus){
						userInfo.maritalStatus = maritalStatusOptions[1];
					}else{
						userInfo.maritalStatus = maritalStatusOptions[0];
					}
					userInfo.dateOfBirth = userInfo.dateOfBirth.substring(0, 10);
					$scope.userInfo = userInfo;
					$scope.userNewInfo = userInfo;
					$scope.$apply();
				},
			always: function(data)
				{
					$scope.loading=false;
				}
		});
	}
})

userApp.controller('userUpdateInfoCtrl', function($scope, $http) {
	
	
})