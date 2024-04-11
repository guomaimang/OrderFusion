let stock;

function contentsPreparation()
{
    let id = getQueryParam("id");
    $.ajax({
        url: "/goods/info",
        type: 'GET',
        data: {
            id: id
        },
        beforeSend: function (request) {
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0 && r.data != null) {
                $('#goodsId').val(r.data.id);
                $('#goodsName').val(r.data.name);
                $('#goodsTitle').val(r.data.title);
                $('#goodsPrice').val(r.data.price);
                stock = r.data.stock;
                document.getElementById("goodsAmount").placeholder = "Current Stock: " + stock;
            }else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        },
        error: function (request, status, error) {
            console.log("Error: " + error);
        }
    });

}


function submitButtonClick() {
    let goodsId = $("#goodsId").val();
    let goodsAmount = $("#goodsAmount").val();

    if (goodsAmount == null || goodsAmount <=0 || goodsAmount > stock) {
        alert("Please enter the correct quantity!");
        return;
    }
    if (grecaptcha.getResponse() == null || grecaptcha.getResponse() === ""){
        showErrorInfo("Please verify the reCaptcha!");
        return;
    }

    let data = {
        "goodsId": goodsId,
        "goodsAmount": goodsAmount,
    };

    let url = "/order/general/create";
    let method = "POST";

    document.getElementById("submitButton").disabled = true;
    document.getElementById("submitButton").innerHTML = "Submitting...";

    $.ajax({
        type: method,
        dataType: "json",
        url: url,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
            request.setRequestHeader("recaptchaToken", grecaptcha.getResponse());
        },
        success: function (r) {
            if (r.code === 0) {
                alert("Order submitted successfully! Now you will be redirected to pay the order.");
                window.location.href = "/order-details.html?id=" + r.data;
            } else {
                swal(r.msg, {
                    icon: "error",
                });
                document.getElementById("submitButton").disabled = false;
                document.getElementById("submitButton").innerHTML = "Submit";
            }
        },
        error: function () {
            swal("Operation failure, please contact the support!", {
                icon: "error",
            });
            document.getElementById("submitButton").disabled = false;
            document.getElementById("submitButton").innerHTML = "Submit";
        }

    });

    grecaptcha.reset();
}

function calculatePrice(){
    let goodsAmount = $("#goodsAmount").val();
    let goodsPrice = $("#goodsPrice").val();
    let totalPrice;
    if (isNull(goodsAmount) || goodsAmount <=0) {
        totalPrice = 0;
    }else {
        totalPrice = parseInt(goodsAmount) * goodsPrice;
    }

    document.getElementById("totalPrice").innerText = totalPrice;
}

contentsPreparation();