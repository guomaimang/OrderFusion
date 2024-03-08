<!-- 正则验证 start-->
/**
 * 判空
 *
 * @param obj
 * @returns {boolean}
 */
function isNull(obj) {
    if (obj == null || obj == undefined || obj.trim() === "") {
        return true;
    }
    return false;
}

/**
 * 用户名称验证,限制输入为邮箱
 *
 * @param email
 * @returns {boolean}
 */
function validUserName(email) {
    var pattern = /^[A-Za-z0-9]+([_\.][A-Za-z0-9]+)*@([A-Za-z0-9\-]+\.)+[A-Za-z]{2,6}$/;
    return pattern.test(email.trim());
}

/**
 * 用户密码验证 最少6位，最多20位字母或数字的组合
 *
 * @param password
 * @returns {boolean}
 */
function validPassword(password) {
    var pattern = /^[a-zA-Z0-9]{6,20}$/;
    return pattern.test(password.trim());
}

<!-- 正则验证 end-->
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

    let data = {"email": email, "password":await sha256(password)};
    $.ajax({
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "userauth/login",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),

        beforeSend: function (request) {
            //设置header值
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
                grecaptcha.reset();
            }
        },

        error: function () {
            $('.alert-danger').css("display", "none");
            showErrorInfo("Interface exception, please contact the administrator!");
            grecaptcha.reset();
        }
    });
}

function logout(){
    window.localStorage.clear();
    window.location.href = "/login.html";
}

function oauth2Login() {
    const queryParams = new URLSearchParams(window.location.search);
    const code = queryParams.get("code");
    var data = {"code": code}

    $.ajax({
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "oauth2/callback",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),

        success: function (result) {
            if (result.resultCode == 200) {
                setCookie("token", result.data.userToken);
                setCookie("userName", result.data.userName);
                setCookie("isStudent", result.data.isStudent);
                window.location.href = "/";
            }
            ;
            if (result.resultCode == 406) {
                alert("Login failed! The account is not authenticated!")
                window.location.href = "/login.html";
                return;
            }
            if (result.resultCode == 500) {
                alert("Server Error!")
                window.location.href = "/login.html";
                return;
            }
        },
        error: function () {
            alert("Interface exception, please contact the administrator!")
            return;
        }
    });
}

/**
 * Check if logged in
 */
async function checkLogin() {
    if (window.localStorage.getItem("jwt") == null || window.localStorage.getItem("jwt") === ""){
        alert("You are not logged in, please go to login first!")
        // window.location.href = "login.html";
        return;
    }

    // Check if the token is expired
    let jwtArr = localStorage.getItem('jwt').split(".");
    let payload = JSON.parse(atob(jwtArr[1]));
    if (payload.exp * 1000 < Date.now()) {
        alert("Your login has expired, please log in again!")
        window.location.href = "login.html";
        return;
    }

    let name = document.getElementById("nameField");
    name.textContent = window.localStorage.getItem("name");

    let identifier = document.getElementById("identifierField");
    if (window.localStorage.getItem("isAdmin") === "0"){
        identifier.textContent = "Customer";
    } else {
        identifier.textContent = "Admin";
        toggleDisplay("adminMenu", "block");
    }

    // check if there is any need for refresh token
    // if the token is about to expire in 6 hours, refresh it
    if (payload.exp * 1000 - Date.now() < 1000 * 60 * 60 * 6){
        let data = {"jwt": window.localStorage.getItem("jwt")};
        $.ajax({
            type: "PUT",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "userauth/refreshtoken",
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            beforeSend: function (request) {
                //设置header值
                request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
            },
            success: function (result) {
                if (result.code === 0) {
                    window.localStorage.setItem("jwt", result.data);
                }
            },
            error: function () {
                console.log("Failed to refresh token.")
            }
        });
    }
}

/**
 * Check if you are a logged in administrator
 */
async function checkLoggedInAdmin() {
    await checkLogin();
    if (window.localStorage.getItem("isAdmin") !== "1") {
       window.alert("You are not an administrator, please log in as an administrator and then do this action!")
       // window.location.href = "index.html";
    }
}

