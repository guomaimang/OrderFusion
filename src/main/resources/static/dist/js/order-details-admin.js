function contentsPreparation(){
    let id = getQueryParam("id");

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "/admin/order/info/"+id,     //url
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
                    document.getElementById("paymentInfoButton").disabled = false;
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

function orderInfoButtonClick() {
    let id = $("#orderInfo-id").val();
    let payment = $("#orderInfo-payment").val();
    let status = $("#orderInfo-status").val();

    let data = {
        "id": id,
        "payment": payment,
        "status": status,
    };

    let url = "/admin/order/update";
    let method = "PUT";

    document.getElementById("orderInfoButton").disabled = true;
    document.getElementById("orderInfoButton").innerHTML = "Submitting...";

    // 执行方法
    $.ajax({
        type: method,           //方法类型
        dataType: "json",       //预期服务器返回的数据类型
        url: url,               //url
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0) {
                alert("Update success!");
                window.location.reload();
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
        },
    });
    document.getElementById("orderInfoButton").disabled = false;
    document.getElementById("orderInfoButton").innerHTML = "Update";
}

function receiverInfoButtonClick() {
    let id = $("#orderInfo-id").val();
    let deliveryReceiver = $("#receiverInfo-deliveryReceiver").val();
    let deliveryAddress = $("#receiverInfo-deliveryAddress").val();
    let deliveryPhone = $("#receiverInfo-deliveryPhone").val();
    let userRemark = $("#receiverInfo-userRemark").val();
    let adminRemark = $("#receiverInfo-adminRemark").val();

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

    let data = {
        "id": id,
        "deliveryAddress": deliveryAddress,
        "deliveryPhone": deliveryPhone,
        "deliveryReceiver": deliveryReceiver,
        "userRemark": userRemark,
        "adminRemark": adminRemark,
    };

    let url = "/admin/order/update";
    let method = "PUT";

    document.getElementById("receiverInfoButton").disabled = true;
    document.getElementById("receiverInfoButton").innerHTML = "Submitting...";

    // 执行方法
    $.ajax({
        type: method,           //方法类型
        dataType: "json",       //预期服务器返回的数据类型
        url: url,               //url
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify(data),
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0) {
                alert("Update success!");
                window.location.reload();
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
        },
    });
    document.getElementById("receiverInfoButton").disabled = false;
    document.getElementById("receiverInfoButton").innerHTML = "Update";

}

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}

contentsPreparation();