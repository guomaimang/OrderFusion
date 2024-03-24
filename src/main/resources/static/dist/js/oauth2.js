const queryParams = new URLSearchParams(window.location.search);
const code = queryParams.get("code");
let data = {"code": code}

$.ajax({
    type: "POST",//方法类型
    dataType: "json",//预期服务器返回的数据类型
    url: "/userauth/oauth2/callback",
    contentType: "application/json; charset=utf-8",
    data: JSON.stringify(data),

    success: function (result) {
        if (result.code === 0) {
            window.localStorage.setItem("jwt", result.data.jwt);
            window.localStorage.setItem("email", result.data.user.email);
            window.localStorage.setItem("name", result.data.user.name);
            window.localStorage.setItem("isAdmin", result.data.user.isAdmin);
            window.location.href = "/";
        }else {
            alert("Login failed! The account is not authenticated!")
            window.location.href = "/login.html";
        }
    },
    error: function () {
        alert("Interface exception, please contact the administrator!")
        window.location.href = "/login.html";
    }
});