let id;
let goodsId;
let title;
let seckillPrice;
let seckillStock;
let startTime;
let endTime;
let isAvailable;
let purchaseLimitNum;

function contentsPreparation(){
    id = getQueryParam("id");

    //请求数据
    $.ajax({
        url: "/seckill/info",
        type: "GET",
        data: {
            id: id
        },
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function(r) {
            if (r.code === 0 && r.data != null) {
                // 获取数据
                id = r.data.id;
                goodsId = r.data.goodsId;
                title = r.data.title;
                seckillPrice = r.data.seckillPrice;
                seckillStock = r.data.seckillStock;
                startTime = r.data.startTime;
                endTime = r.data.endTime;
                isAvailable = r.data.isAvailable;
                purchaseLimitNum = r.data.purchaseLimitNum;

                //填充数据
                $('#seckill-id').text(id);
                $('#seckill-title').text(title);
                $('#seckill-goodsId').text(goodsId);
                $('#seckill-price').text(priceFormatter(seckillPrice));
                $('#seckill-stoke').text(seckillStock);
                $('#seckill-purchaseLimitNum').text(purchaseLimitNum);
                $('#seckill-isAvailable').text(isAvailableFormatter(isAvailable));
                $('#seckill-startTime').text(utcToLocalFormatter(startTime));
                $('#seckill-endTime').text(utcToLocalFormatter(endTime));

                if (isAvailable === 1){
                    countTime();
                }
            }else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // handle error
            console.error("AJAX Error: ", textStatus, errorThrown);
        }
    });

}

function calculatePrice(){
    let goodsAmount = $("#goodsAmount").val();
    let goodsPrice = seckillPrice;
    let totalPrice;
    if (isNull(goodsAmount) || goodsAmount <=0) {
        totalPrice = 0;
    }else {
        totalPrice = parseInt(goodsAmount) * goodsPrice;
    }

    document.getElementById("totalPrice").innerText = totalPrice;
}

function submitOrderButtonClick() {
    // 获取表单数据
    let goodsAmount = $("#goodsAmount").val();

    //验证数据
    if (goodsAmount == null || goodsAmount <=0 || goodsAmount > seckillStock || goodsAmount > purchaseLimitNum) {
        swal("Please enter the correct quantity!", {
            icon: "warning",
        });
        return;
    }
    if (grecaptcha.getResponse() == null || grecaptcha.getResponse() === ""){
        swal("Please complete the reCAPTCHA verification!", {
            icon: "warning",
        });
        return;
    }

    // 将即将发送数据封装为Json, 和 Pojo 对应
    let data = {
        "seckillEventId": id,
        "goodsAmount": goodsAmount,
    };

    let url = "/order/seckill/create";
    let method = "POST";

    document.getElementById("submitOrderButton").disabled = true;
    document.getElementById("submitOrderButton").innerHTML = "Submitting...";

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
            request.setRequestHeader("recaptchaToken", grecaptcha.getResponse());
        },
        success: function (r) {
            if (r.data.status === 0) {
                document.getElementById("goodsAmount").disabled = true;
                swal(r.data.message, {
                    icon: "success",
                });
            } else if(r.data.status === 2){
                alert(r.data.message);
                window.location.href = "/order-details.html?id=" + r.data.orderId;
            }
            else {
                swal(r.data.message, {
                    icon: "warning",
                });
            }
            document.getElementById("submitOrderButton").disabled = false;
            document.getElementById("submitOrderButton").innerHTML = "Submit";
        },
        error: function () {
            swal("Operation failure, please contact the support!", {
                icon: "error",
            });
            document.getElementById("submitOrderButton").disabled = false;
            document.getElementById("submitOrderButton").innerHTML = "Submit";
        }

    });

    grecaptcha.reset();
}

/**
 * isFrozen formatter
 * @returns {string}
 */
function priceFormatter(cellValue) {
    return "HKD " + cellValue;
}

function isAvailableFormatter(cellValue) {
    return cellValue == 1 ? "Yes" : "No";
}

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}

function countTime(){
    let submitOrderButton = document.getElementById("submitOrderButton");

    function updateCountdown() {
        let start = new Date(startTime);
        let now  = new Date();
        let end = new Date(endTime);
        let countdown = document.getElementById("countdown");

        if (now < start) {
            // 秒杀还未开始，显示距离开始的倒计时
            let diff = Math.floor((start - now) / 1000);  // 剩余的秒数
            let hours = Math.floor(diff / 3600);
            let minutes = Math.floor(diff % 3600 / 60);
            let seconds = Math.floor(diff % 60);

            countdown.innerText = 'Countdown to start: ' + hours + ':' + minutes + ':' + seconds;
        } else if (now < end) {
            // 秒杀已经开始，但还未结束，显示距离结束的倒计时，并使按钮可用
            let diff = Math.floor((end - now) / 1000);  // 剩余的秒数
            let hours = Math.floor(diff / 3600);
            let minutes = Math.floor(diff % 3600 / 60);
            let seconds = Math.floor(diff % 60);
            countdown.innerText = 'Countdown to end：' + hours + ':' + minutes + ':' + seconds;
            submitOrderButton.disabled = false;
        } else {
            // 秒杀已经结束，使按钮不可用
            countdown.innerText = 'The seckill was over!';
            submitOrderButton.disabled = true;
        }
    }

    setInterval(updateCountdown, 1000);
}

contentsPreparation();

