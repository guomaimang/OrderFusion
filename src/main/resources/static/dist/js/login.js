function checkLoginInStatus(){
    if (!isNull(window.localStorage.getItem("jwt"))){
        window.location.href = "/";
    }
}

/**
 * 用户名称验证,限制输入为邮箱
 *
 * @param email
 * @returns {boolean}
 */
function validUserName(email) {
    let pattern = /^[A-Za-z0-9]+([_\.][A-Za-z0-9]+)*@([A-Za-z0-9\-]+\.)+[A-Za-z]{2,6}$/;
    return pattern.test(email.trim());
}

/**
 * 用户密码验证 最少6位，最多20位字母或数字的组合
 *
 * @param password
 * @returns {boolean}
 */
function validPassword(password) {
    let pattern = /^[a-zA-Z0-9]{6,20}$/;
    return pattern.test(password.trim());
}

async function login() {

    document.getElementById("loginButton").disabled = true;
    document.getElementById("loginButton").innerHTML = "Loading...";


    let email = $("#email").val();
    let password = $("#password").val();

    if (isNull(email)) {
        showErrorInfo("Please enter your email!");
        document.getElementById("loginButton").disabled = false;
        document.getElementById("loginButton").innerHTML = "Login";
        return;
    }
    if (!validUserName(email)) {
        showErrorInfo("Please enter the correct email!");
        document.getElementById("loginButton").disabled = false;
        document.getElementById("loginButton").innerHTML = "Login";
        return;
    }
    if (isNull(password)) {
        showErrorInfo("Please enter your password!");
        document.getElementById("loginButton").disabled = false;
        document.getElementById("loginButton").innerHTML = "Login";
        return;
    }
    if (!validPassword(password)) {
        showErrorInfo("Please enter the correct password!");
        document.getElementById("loginButton").disabled = false;
        document.getElementById("loginButton").innerHTML = "Login";
        return;
    }

    if (grecaptcha.getResponse() == null || grecaptcha.getResponse() === ""){
        showErrorInfo("Please verify the reCaptcha!");
        document.getElementById("loginButton").disabled = false;
        document.getElementById("loginButton").innerHTML = "Login";
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