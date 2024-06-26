function checkLoginInStatus(){
    if (!isNull(window.localStorage.getItem("jwt"))){
        let jwtArr = localStorage.getItem('jwt').split(".");
        let payload = JSON.parse(atob(jwtArr[1]));
        if (payload.exp * 1000 > Date.now()) {
            window.location.href = "index.html";
        }
    }
}

function prepareSSOLink(){
    let clientId = "45792ac5-5f4c-49a7-ba2d-1845333171a1";
    let redirectUri = window.location.protocol + "//" + window.location.host + "/oauth2.html";

    document.getElementById("sso-link").href =
        "https://login.microsoftonline.com/common/" +
        "oauth2/v2.0/authorize"+
        "?client_id=" + clientId +
        "&response_type=code" +
        "&redirect_uri=" + redirectUri +
        "&response_mode=query" +
        "&scope=openid+profile+email" +
        "&state=12345";
}


/**
 *
 * @param email
 * @returns {boolean}
 */
function validUserName(email) {
    let pattern = /^[A-Za-z0-9]+([_\.][A-Za-z0-9]+)*@([A-Za-z0-9\-]+\.)+[A-Za-z]{2,6}$/;
    return pattern.test(email.trim());
}

/**
 *
 * @param password
 * @returns {boolean}
 */
function validPassword(password) {
    let pattern = /^[a-zA-Z0-9]{6,20}$/;
    return pattern.test(password.trim());
}

async function login() {

    let email = $("#email").val();
    let password = $("#password").val();

    if (isNull(email)) {
        showErrorInfo("Please enter your email!");
        return;
    }
    if (!validUserName(email)) {
        showErrorInfo("Please enter the correct email!");
        return;
    }
    if (isNull(password)) {
        showErrorInfo("Please enter your password!");
        return;
    }
    if (!validPassword(password)) {
        showErrorInfo("Please enter the correct password!");
        return;
    }

    if (grecaptcha.getResponse() == null || grecaptcha.getResponse() === ""){
        showErrorInfo("Please verify the reCaptcha!");
        return;
    }

    document.getElementById("loginButton").disabled = true;
    document.getElementById("loginButton").innerHTML = "Loading...";

    let data = {"email": email, "password":await sha256(password)};
    $.ajax({
        type: "POST",
        dataType: "json",
        url: "userauth/login",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),

        beforeSend: function (request) {
            request.setRequestHeader("recaptchaToken", grecaptcha.getResponse());
        },

        success: function (result) {
            if (result.code === 0) {
                $('.alert-danger').css("display", "none");
                window.localStorage.setItem("jwt", result.data.jwt);

                window.localStorage.setItem("email", result.data.user.email);
                window.localStorage.setItem("name", result.data.user.name);
                window.localStorage.setItem("isAdmin", result.data.user.isAdmin);

                window.location.href = "/";
            }
            else{
                showErrorInfo(result.msg);
                document.getElementById("loginButton").disabled = false;
                document.getElementById("loginButton").innerHTML = "Login";
                grecaptcha.reset();
            }
        },

        error: function () {
            $('.alert-danger').css("display", "none");
            showErrorInfo("Interface exception, please contact the administrator!");
            document.getElementById("loginButton").disabled = false;
            document.getElementById("loginButton").innerHTML = "Login";
            grecaptcha.reset();
        },
    });
}

checkLoginInStatus();
prepareSSOLink();