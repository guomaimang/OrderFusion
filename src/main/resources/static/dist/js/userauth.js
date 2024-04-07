
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

    await setPageText();

    // check if there is any need for refresh token
    // if the token is about to expire in 6 hours, refresh it
    if (payload.exp * 1000 - Date.now() < 1000 * 60 * 60 * 6){
        let data = {"jwt": window.localStorage.getItem("jwt")};
        $.ajax({
            type: "PUT", // method type
            dataType: "json",// Data type expected to be returned by the server
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

async function setPageText() {
    let name = document.getElementById("nameField");
    name.textContent = isNull(window.localStorage.getItem("name"))? "Guest User": window.localStorage.getItem("name");

    let identifier = document.getElementById("identifierField");
    if (window.localStorage.getItem("isAdmin") === "1"){
        identifier.textContent = "Admin";
        toggleDisplay("adminMenu", "block");
    } else {
        identifier.textContent = "Customer";
    }
}

/**
 * Check if you are a logged in administrator
 */
async function checkLoggedInAdmin() {
    await checkLogin();
    if (window.localStorage.getItem("isAdmin") !== "1") {
       window.alert("You are not an administrator, please log in as an administrator and then do this action!")
       window.location.href = "index.html";
    }
}

function logout(){
    window.localStorage.clear();
    window.location.href = "/login.html";
}