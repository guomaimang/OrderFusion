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

    if (window.localStorage.getItem("_grecaptcha") == null || window.localStorage.getItem("_grecaptcha") === ""){
        showErrorInfo("Please verify the captcha!");
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
            request.setRequestHeader("_grecaptcha", window.localStorage.getItem("_grecaptcha"));
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
            }
        },

        error: function () {
            $('.alert-danger').css("display", "none");
            showErrorInfo("Interface exception, please contact the administrator!");

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

<!-- cookie操作 start-->

/**
 * 写入cookie
 *
 * @param name
 * @param value
 */
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=/";

}

/**
 * 读取cookie
 * @param name
 * @returns {null}
 */
function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

/**
 * 删除cookie
 * @param name
 */
function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

/**
 * 检查cookie
 */
function checkLogin() {
    if (window.localStorage.getItem("jwt") == null || window.localStorage.getItem("jwt") === ""){
        window.location.href = "login.html";
        return;
    }else {
        let name = document.getElementById("nameField");
        name.textContent = window.localStorage.getItem("name");
    }

    let identifier = document.getElementById("identifierField");
    if (window.localStorage.getItem("isAdmin") === "0"){
        identifier.textContent = "Member";
    } else {
        identifier.textContent = "Admin";
    }


}

/**
 * 检查cookie
 */
function checkResultCode(code) {
    if (code == 402) {
        window.location.href = "login.html";
    }
}

<!-- cookie操作 end-->

function showErrorInfo(info) {
    $('.alert-danger').css("display", "block");
    $('.alert-danger').html(info);
}

/**
 * 获取jqGrid选中的一条记录
 * @returns {*}
 */
function getSelectedRow() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    var selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    return selectedIDs[0];
}

/**
 * 获取jqGrid选中的多条记录
 * @returns {*}
 */
function getSelectedRows() {
    var grid = $("#jqGrid");
    var rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    return grid.getGridParam("selarrrow");
}