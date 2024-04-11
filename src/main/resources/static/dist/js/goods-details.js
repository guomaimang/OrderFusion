function contentsPreparation(){
    let id = getQueryParam("id");

    $.ajax({
        url: "/goods/info",
        data: {id: id},
        type: 'GET',
        beforeSend: function (request) {
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0 && r.data != null) {
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
            console.log(textStatus, errorThrown);
        }
    });
}


let editorD;
const E = window.wangEditor;
editorD = new E('#wangEditor')
editorD.config.height = 800
editorD.config.uploadImgServer = 'images/upload'
editorD.config.uploadFileName = 'file'
editorD.config.uploadImgMaxSize = 2 * 1024 * 1024
editorD.config.uploadImgMaxLength = 1
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