function contentsPreparation(){
    let id = getQueryParam("id");

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "/order/info/"+id,     //url
        contentType: "application/json; charset=utf-8",
        beforeSend: function (request) {
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0) {
                // load data

                document.getElementById("goodsInfo-id").innerHTML = r.data.goods.id;
                document.getElementById("goodsInfo-name").innerHTML = r.data.goods.name;
                document.getElementById("goodsInfo-title").innerHTML = r.data.goods.title;
                document.getElementById("card-imageUri").src = isNull(r.data.imageUri)? "https://pic.hanjiaming.com.cn/2024/03/24/fec4e63dcce3f.jpg": r.data.imageUri;

                document.getElementById("orderInfo-id").value = r.data.order.id;
                document.getElementById("orderInfo-amount").value = r.data.order.goodsAmount;
                document.getElementById("orderInfo-payment").value = r.data.order.payment;
                document.getElementById("orderInfo-channel").value = r.data.order.channel;
                document.getElementById("orderInfo-status").value = r.data.order.status;
                document.getElementById("orderInfo-createTime").value = r.data.order.createTime === null? "":utcToLocalFormatter(r.data.order.createTime);
                document.getElementById("orderInfo-payTime").value = r.data.order.payTime === null ? "":utcToLocalFormatter(r.data.order.payTime);
                document.getElementById("orderInfo-sentTime").value = r.data.order.sentTime === null ? "":utcToLocalFormatter(r.data.order.sentTime);

                if (r.data.pay !== null){
                    document.getElementById("paymentInfo-transactionId").value = r.data.pay.transactionId;
                    document.getElementById("paymentInfo-method").value = r.data.pay.method;
                }

                document.getElementById("receiverInfo-userId").value = r.data.order.userId;
                document.getElementById("receiverInfo-deliveryReceiver").value = r.data.order.deliveryReceiver;
                document.getElementById("receiverInfo-deliveryPhone").value = r.data.order.deliveryPhone;
                document.getElementById("receiverInfo-deliveryAddress").value = r.data.order.deliveryAddress;
                document.getElementById("receiverInfo-userRemark").value = r.data.order.userRemark;
                document.getElementById("receiverInfo-adminRemark").value = r.data.order.adminRemark;

                if (r.data.seckillEvent !== null){
                    document.getElementById("seckillInfo").style.display = "block";
                    document.getElementById("seckillInfo-eventId").innerHTML = r.data.seckillEvent.id;
                    document.getElementById("seckillInfo-eventTitle").innerHTML = r.data.seckillEvent.title;
                }

                // if not pay, can edit
                if (r.data.order.status === 0){
                    document.getElementById("receiverInfo-deliveryReceiver").readOnly = false;
                    document.getElementById("receiverInfo-deliveryPhone").readOnly = false;
                    document.getElementById("receiverInfo-deliveryAddress").readOnly = false;
                    document.getElementById("receiverInfo-userRemark").readOnly = false;
                    document.getElementById("receiverInfoButton").disabled = false;
                    document.getElementById("receiverInfoButton").style.display = "block";
                    alert("Please note that the order has not been paid yet, please pay as soon as possible! Overtime may result in order cancellation.");
                }
            } else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        },
        error: function () {
            swal("Operation failure, please contact the support!", {
                icon: "error",
            });
        }

    });

}

function goodsInfoButtonClick() {
let id = document.getElementById("goodsInfo-id").innerHTML;
    window.open("/goods-details.html?id=" + id);
}

function receiverInfoButtonClick() {

    let id = $("#orderInfo-id").val();
    let deliveryReceiver = $("#receiverInfo-deliveryReceiver").val();
    let deliveryAddress = $("#receiverInfo-deliveryAddress").val();
    let deliveryPhone = $("#receiverInfo-deliveryPhone").val();
    let userRemark = $("#receiverInfo-userRemark").val();
    if (isNull(deliveryReceiver) || deliveryReceiver.length > 20) {
        alert("Please enter the consignee name within 20 characters!");
        return;
    }
    if (isNull(deliveryAddress) || deliveryAddress.length > 60) {
        alert("Please enter the shipping address within 60 characters!");
        return;
    }
    if (isNull(deliveryPhone)) {
        alert("Please enter the phone number!");
        return;
    }

    let virtualPayConfirm = confirm(
        "Please confirm that the payment information is correct and the payment is successful! \n" +
        "Payment amount: " + document.getElementById("orderInfo-payment").value + " HKD" + "\n" +
        "Payment Method: Virtual Payment" + "\n" +
        "Click OK to confirm payment, or Cancel to cancel payment."
    );

    if (!virtualPayConfirm) {
        return;
    }

    document.getElementById("receiverInfoButton").disabled = true;
    document.getElementById("receiverInfoButton").innerHTML = "Submitting...";

    let data = {
        "id": id,
        "deliveryAddress": deliveryAddress,
        "deliveryPhone": deliveryPhone,
        "deliveryReceiver": deliveryReceiver,
        "userRemark": userRemark,
    };

    let url = "/order/pay";
    let method = "POST";

    $.ajax({
        type: method,
        dataType: "json",
        url: url,
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0) {
                alert("Payment successful!");
            }else {
                alert(r.msg);
            }
            window.location.reload();
        },
        error: function () {
            swal("Operation failure, please contact the support!", {
                icon: "error",
            });
            document.getElementById("receiverInfoButton").disabled = false;
            document.getElementById("receiverInfoButton").innerHTML = "Submit & Pay";
        },
    });


}

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}

function seckillInfoButtonClick() {
    let id = document.getElementById("seckillInfo-eventId").innerHTML;
    window.open("/seckill-details.html?id=" + id);
}

contentsPreparation();