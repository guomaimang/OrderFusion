function contentsPreparation(){
    let id = getQueryParam("id");

    //请求数据
    $.ajax({
        url: "/goods/info",
        data: {id: id},
        type: 'GET',
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0 && r.data != null) {
                //填充数据 至 card
                $('#card-id').text(r.data.id);
                $('#card-name').text(r.data.name);
                $('#card-title').text(r.data.title);
                $('#card-price').text(priceFormatter(r.data.price));
                $('#card-stock').text(r.data.stock);
                $('#card-isAvailable').text(isAvailableFormatter(r.data.isAvailable));
                document.getElementById("card-imageUri").src = isNull(r.data.imageUri)? "https://pic.hanjiaming.com.cn/2024/03/24/fec4e63dcce3f.jpg": r.data.imageUri;
                editorD.txt.html(r.data.description);
                if (r.data.isAvailable === 1){
                    let buyButton = document.getElementById("buyButton");
                    buyButton.disabled = false;
                }
            }else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            // 处理错误
            console.log(textStatus, errorThrown);
        }
    });
}


let editorD;
//富文本编辑器
const E = window.wangEditor;
editorD = new E('#wangEditor')
// 设置编辑区域高度为 400px
editorD.config.height = 800
//配置服务端图片上传地址
editorD.config.uploadImgServer = 'images/upload'
editorD.config.uploadFileName = 'file'
//限制图片大小 2M
editorD.config.uploadImgMaxSize = 2 * 1024 * 1024
//限制一次最多能传几张图片 一次最多上传 1 个图片
editorD.config.uploadImgMaxLength = 1
//插入网络图片的功能
editorD.config.showLinkImg = true
editorD.create();
editorD.disable();

function buyButtonClick() {
    let id = getQueryParam("id");
    window.location.href = "order-submission.html?id=" + id;
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

contentsPreparation();