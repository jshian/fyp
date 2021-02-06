var userApp = angular.module('user', []);

var getUserInfoUrl ="user/info/get";
var updateUserInfoUrl ="user/info/update";

userApp.controller('userGetInfoCtrl', function($scope, $http) {
	$scope.refreshUserInfo = refreshUserInfo;
	$scope.userInfo ={};
	$scope.userNewInfo ={};
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
	$scope.updateUserInfo =
	function()
		{
			var method = "PUT";
			var url = updateUserInfoUrl;
			var data = $scope.userNewInfo;
			data.maritalStatus = data.maritalStatus.value;
			data.dateOfBirth = $('#date-input').val();
			var json = JSON.stringify(data);
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
						location.reload();
					},
				always: function(data)
					{
						$scope.loading=false;
					}
			});
		};
	
})