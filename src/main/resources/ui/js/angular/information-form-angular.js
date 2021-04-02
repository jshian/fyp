var userApp = angular.module('user', []);
var addUserInfoUrl ="user/info/add";

userApp.controller('userAddInfoCtrl', function($scope, $http) {
	$scope.submitUserInfo =
	function()
		{
			var method = "POST";
			var url = addUserInfoUrl;
			var data = $scope.userInfo;
			data.maritalStatus = parseInt(data.maritalStatus);
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