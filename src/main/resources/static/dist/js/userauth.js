
/**
 * Check if logged in
 */
async function checkLogin() {
    if (window.localStorage.getItem("jwt") == null || window.localStorage.getItem("jwt") === ""){
        alert("You are not logged in, please go to login first!")
        window.location.href = "login.html";
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

function logout(){
    window.localStorage.clear();
    window.location.href = "/login.html";
}