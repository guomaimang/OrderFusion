function contentsPreparation(){
    let id = getQueryParam("id");

    //请求数据
    $.get("goods/info?id=" + id, function (r) {
        if (r.code === 0 && r.data != null) {
            //填充数据 至 card
            $('#card-id').text(r.data.id);
            $('#card-name').text(r.data.name);
            $('#card-title').text(r.data.title);
            $('#card-price').text(priceFormatter(r.data.price));
            $('#card-stock').text(r.data.stock);
            $('#card-isAvailable').text(isAvailableFormatter(r.data.isAvailable));
            $('#card-imageUri').text(r.data.imageUri);
            editorD.txt.html(r.data.description);
            if (r.data.isAvailable == 1){
                let buyButton = document.getElementById("buyButton");
                buyButton.disabled = false;
            }
        }
    });
}


let editorD;
//富文本编辑器
const E = window.wangEditor;
editorD = new E('#wangEditor')
// 设置编辑区域高度为 400px
editorD.config.height = 260
//配置服务端图片上传地址
editorD.config.uploadImgServer = 'images/upload'
editorD.config.uploadFileName = 'file'
//限制图片大小 2M
editorD.config.uploadImgMaxSize = 2 * 1024 * 1024
//限制一次最多能传几张图片 一次最多上传 1 个图片
editorD.config.uploadImgMaxLength = 1
//插入网络图片的功能
editorD.config.showLinkImg = true
editorD.config.uploadImgHooks = {
    // 图片上传并返回了结果，图片插入已成功
    success: function (xhr) {
        console.log('success', xhr)
    },
    // 图片上传并返回了结果，但图片插入时出错了
    fail: function (xhr, editor, resData) {
        console.log('fail', resData)
    },
    // 上传图片出错，一般为 http 请求的错误
    error: function (xhr, editor, resData) {
        console.log('error', xhr, resData)
    },
    // 上传图片超时
    timeout: function (xhr) {
        console.log('timeout')
    },
    customInsert: function (insertImgFn, result) {
        if (result != null && result.resultCode == 200) {
            // insertImgFn 可把图片插入到编辑器，传入图片 src ，执行函数即可
            insertImgFn(result.data)
        } else {
            alert("error");
        }
    }
}
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