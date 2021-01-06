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

function checkSession() {
    var token = getCookie("Authorization");

    if (token == null) {
        location.replace('./Login.html');
    }
}

function checkFirstLogin() {
	var token = getCookie("IsFirstLogin");
	
    if (token == null && !location.href.includes("InformationForm.html")) {
        location.replace('./InformationForm.html');
    }
}
function deleteAllCookies() {
    var cookies = document.cookie.split(";");

    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        var eqPos = cookie.indexOf("=");
        var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
        document.cookie = name + "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
    }
}

function logout() {
    deleteAllCookies();
	checkSession()
}

//Handle Authentication
$( document ).ready(function() {
    checkSession();
	checkFirstLogin();
});