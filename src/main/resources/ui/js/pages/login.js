//api url
var loginApiUrl = "/auth/login";
var checkFirstLoginApiUrl = "user/info/get";


// Your web app's Firebase configuration
  // For Firebase JS SDK v7.20.0 and later, measurementId is optional
var firebaseConfig = {
apiKey: "AIzaSyBY-OsjBT4Mgsq3BM21Hd5pOp-5hIYWahA",
authDomain: "fyp2020-80b5e.firebaseapp.com",
databaseURL: "https://fyp2020-80b5e.firebaseio.com",
projectId: "fyp2020-80b5e",
storageBucket: "fyp2020-80b5e.appspot.com",
messagingSenderId: "261006040898",
appId: "1:261006040898:web:295fa606d65926e466b2db",
measurementId: "G-6L6W7XJQGF"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.analytics();

var ui = new firebaseui.auth.AuthUI(firebase.auth());
var uiConfig = {
  callbacks: {
	signInSuccessWithAuthResult: function(authResult, redirectUrl) {
	  // User successfully signed in.
	  // Return type determines whether we continue the redirect automatically
	  // or whether we leave that to developer to handle.
	  firebase.auth().currentUser.getIdToken().then(
		function(data){
			login(data);
		}
	  );
	  return true;
	},
	uiShown: function() {
	  // The widget is rendered.
	  // Hide the loader.
	  document.getElementById('loader').style.display = 'none';
	}
  },
  // Will use popup for IDP Providers sign-in flow instead of the default, redirect.
  signInFlow: 'popup',
  signInSuccessUrl: '#',
  signInOptions: [
	// Leave the lines as is for the providers you want to offer your users.
	firebase.auth.GoogleAuthProvider.PROVIDER_ID,
	firebase.auth.EmailAuthProvider.PROVIDER_ID
  ]
};
ui.start('#firebaseui-auth-container', uiConfig);

function getCookie(name) {
    var dc = document.cookie;
    var prefix = name + "=";
    var begin = dc.indexOf("; " + prefix);
    if (begin == -1) {
        begin = dc.indexOf(prefix);
        if (begin != 0) return null;
    }
    else
    {
        begin += 2;
        var end = document.cookie.indexOf(";", begin);
        if (end == -1) {
        end = dc.length;
        }
    }
    // because unescape has been deprecated, replaced with decodeURI
    //return unescape(dc.substring(begin + prefix.length, end));
    return decodeURI(dc.substring(begin + prefix.length, end));
} 

function backdoor(){
	login("000");
}

function login(token){
	body = {};
	body["token"] = token;
	$.ajax({
		type: "POST",
		url: loginApiUrl,
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		data: JSON.stringify(body),
		success: function(data)
			{
				document.cookie = "Authorization="+data.token+";";
				checkFirstLogin();
			}
	});
}

function checkFirstLogin() {
	$.ajax({
		type: "GET",
		url: checkFirstLoginApiUrl,
		headers : 
		{
			"Authorization" : getCookie("Authorization")
		},
		success: function(data)
			{
				document.cookie = "IsFirstLogin=0;";
				location.replace('Index.html');
			}
		,
		statusCode: 
			{
				403: function() {
					location.replace('InformationForm.html');
				}
			}
	});
}