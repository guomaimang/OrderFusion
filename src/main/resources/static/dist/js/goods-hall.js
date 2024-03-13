let editorD;

$(function () {
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


    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    $('#goodsModal').modal('hide');

    $("#jqGrid").jqGrid({
        // 设置API
        url: 'goods/list',
        loadBeforeSend: function(jqXHR) {
            jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        datatype: "json",
        colModel: [
            // 设置列表表头
            {label: 'ID', name: 'id', index: 'id', width: 30, key: true, hidden: false},
            {label: 'Name', name: 'name', index: 'name', width: 60},
            {label: 'Title', name: 'title', index: 'title', width: 150},
            {label: 'Price', name: 'price', index: 'price', width: 30,  editable: true, formatter:priceFormatter},
            {label: 'Stock', name: 'stock', index: 'stock', width: 30},
            {label: 'Available', name: 'isAvailable', index: 'isAvailable', width: 30, editable: true, formatter:isAvailableFormatter},
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: 'Information reading in progress...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.rows",
            records: "data.count",
            page: "data.currentPage",
            total: "data.totalPage",
        },
        prmNames: {
            page: "pagenum",
            rows: "pagesize",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
        onSelectRow: function () {
            //返回选中的id
            selectedRowIndex = $("#" + this.id).getGridParam('selrow');
            //返回点击这行xlmc的值
            window.open("/goods-details.html?id=" + selectedRowIndex);
        },
    });

    // 搜索功能
    $("#searchButton").click(function(){
        let searchEmail = $("#searchInput").val(); //获取输入框的值
        $("#jqGrid").jqGrid('setGridParam',{
            postData: {'keyword': searchEmail}, //设置postData参数
            page: 1
        }).trigger("reloadGrid"); //重新加载JqGrid
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

/**
 * 数据验证
 */
function validObject() {
    let name = $('#modal-name').val();
    if (isNull(name))  {
        showErrorInfo("The name can not be blank!");
        return false;
    }
    if (!validLength(name, 50)) {
        showErrorInfo("Name length cannot be larger than 50!");
        return false;
    }

    let title = $('#modal-title').val();
    if (isNull(title))  {
        showErrorInfo("The title can not be blank!");
        return false;
    }
    if (!validLength(name, 120)) {
        showErrorInfo("Title length cannot be larger than 120!");
        return false;
    }

    let price = $('#modal-price').val();
    if (isNull(price)) {
        showErrorInfo("Price cannot be empty!");
        return false;
    }
    if (!validLength(price, 8)) {
        showErrorInfo("Price length cannot be larger than 8!");
        return false;
    }

    let stock = $('#modal-stock').val();
    if (isNull(stock)) {
        showErrorInfo("Stock cannot be empty!");
        return false;
    }
    if (!validLength(stock, 6)) {
        showErrorInfo("Stock length cannot be larger than 6!");
        return false;}
    if (stock < 0){
        showErrorInfo("Stock must >= 0!")
        return false;
    }

    let imageUri = $('#modal-imageUri').val();
    if (isNull(imageUri))  {
        $('#modal-imageUri').val("https://pic.hanjiaming.com.cn/2024/03/11/745889d753016.jpg");
        return false;
    }
    if (!validLength(imageUri, 254)) {
        showErrorInfo("Stock length cannot be larger than 254!");
        return false;}

    return true;
}

/**
 * 重置 modal 表单数据
 */
// Grid 顶部的操作按钮
function addGoods() {
    reset();
    $('.modal-title').html('Add');
    $('#goodsModal').modal('show');
}
function editGoods() {
    reset();
    $('.modal-title').html('Edit');

    let id = getSelectedRow();
    if (id == null) {
        return;
    }
    //请求数据
    $.get("goods/info?id=" + id, function (r) {
        if (r.code === 0 && r.data != null) {
            //填充数据 至 modal
            $('#modal-id').val(r.data.id);
            $('#modal-name').val(r.data.name);
            $('#modal-title').val(r.data.title);
            $('#modal-price').val(r.data.price);
            $('#modal-stock').val(r.data.stock);
            $('#modal-imageUri').val(r.data.imageUri);
            $('#modal-isAvailable').val(r.data.isAvailable);
            editorD.txt.html(r.data.description);
        }
    });
    //显示 modal
    $('#goodsModal').modal('show');
}
function previewGoods() {
    let id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.open("/goods-detail.html?id=" + id);
}

//绑定 modal 表单上的 SAVE 按钮
$('#saveButton').click(async function () {
    //验证数据
    if (validObject()) {

        // Ajax 发送网络请求

        // 获取表单数据
        let id = $("#modal-id").val();
        let name = $("#modal-name").val();
        let title = $("#modal-title").val();
        let price = $("#modal-price").val();
        let stock = $("#modal-stock").val();
        let imageUri = $("#modal-imageUri").val();
        let isAvailable = $("#modal-isAvailable").val();
        let description = editorD.txt.html();

        // 将即将发送数据封装为Json, 和 Pojo 对应
        let data = {
            "id": id,
            "name": name,
            "title": title,
            "price": price,
            "stock": stock,
            "isAvailable": isAvailable,
            "description": description,
            "imageUri": imageUri
        };

        let url;
        let method;

        // 表示新增操作
        if (id === null || id === "" || id === undefined || id < 0) {
            url = 'admin/goods/add';
            method = 'POST';
        }else {
            // id>=0表示编辑操作
            url = 'admin/goods/edit';
            method = 'PUT';
        }

        // 执行方法
        $.ajax({
            type: method,           //方法类型
            dataType: "json",       //预期服务器返回的数据类型
            url: url,               //url
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify(data),
            success: function (r) {
                if (r.code === 0) {
                    $('#goodsModal').modal('hide');
                    swal("Operation success!", {
                        icon: "success",
                    });
                    reload();
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
});

function reset() {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");
    //清空数据
    $('#modal-id').val('');
    $('#modal-name').val('');
    $('#modal-title').val('');
    $('#modal-price').val('');
    $('#modal-stock').val('0');
    $('#modal-imageUri').val('');
    $('#modal-isAvailable').val(1);
    editorD.txt.html('');
}
/**
 * jqGrid 重新加载
 */
function reload() {
    reset();
    let page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

/**
 * jqGrid isFrozen formatter
 * @returns {string}
 */
function priceFormatter(cellValue) {
    return "HKD " + cellValue;
}

function isAvailableFormatter(cellValue) {
    return cellValue == 1 ? "Yes" : "No";
}