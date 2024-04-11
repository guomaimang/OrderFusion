let editorD;

$(function () {
    const E = window.wangEditor;
    editorD = new E('#wangEditor')
    editorD.config.height = 260
    editorD.config.uploadImgServer = 'images/upload'
    editorD.config.uploadFileName = 'file'
    editorD.config.uploadImgMaxSize = 2 * 1024 * 1024
    editorD.config.uploadImgMaxLength = 1
    editorD.config.showLinkImg = true
    editorD.create();

    $('.alert-danger').css("display", "none");

    $('#goodsModal').modal('hide');

    $("#jqGrid").jqGrid({
        url: 'goods/list',
        loadBeforeSend: function(jqXHR) {
            jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        datatype: "json",
        colModel: [
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
        multiselect: true,
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
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
    });

    $("#searchButton").click(function(){
        let searchEmail = $("#searchInput").val();
        $("#jqGrid").jqGrid('setGridParam',{
            postData: {'keyword': searchEmail},
            page: 1
        }).trigger("reloadGrid");
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});


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

    $.ajax({
        url: "/goods/info",
        type: "GET",
        data: {
            id: id
        },
        beforeSend: function (request) {

            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0 && r.data != null) {
                $('#modal-id').val(r.data.id);
                $('#modal-name').val(r.data.name);
                $('#modal-title').val(r.data.title);
                $('#modal-price').val(r.data.price);
                $('#modal-stock').val(r.data.stock);
                $('#modal-imageUri').val(r.data.imageUri);
                $('#modal-isAvailable').val(r.data.isAvailable);
                editorD.txt.html(r.data.description);
            }else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        }
    });



    $('#goodsModal').modal('show');
}
function previewGoods() {
    let id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.open("/goods-details.html?id=" + id);
}

$('#saveButton').click(async function () {
    if (validObject()) {

        let id = $("#modal-id").val();
        let name = $("#modal-name").val();
        let title = $("#modal-title").val();
        let price = $("#modal-price").val();
        let stock = $("#modal-stock").val();
        let imageUri = $("#modal-imageUri").val();
        let isAvailable = $("#modal-isAvailable").val();
        let description = editorD.txt.html();

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

        if (id === null || id === "" || id === undefined || id < 0) {
            url = 'admin/goods/add';
            method = 'POST';
        }else {
            url = 'admin/goods/edit';
            method = 'PUT';
        }

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
    $('.alert-danger').css("display", "none");
    $('#modal-id').val('');
    $('#modal-name').val('');
    $('#modal-title').val('');
    $('#modal-price').val('');
    $('#modal-stock').val('0');
    $('#modal-imageUri').val('');
    $('#modal-isAvailable').val(1);
    editorD.txt.html('');
}

function reload() {
    reset();
    let page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}


function priceFormatter(cellValue) {
    return "HKD " + cellValue;
}

function isAvailableFormatter(cellValue) {
    return cellValue == 1 ? "Yes" : "No";
}