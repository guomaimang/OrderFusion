function contentsPreparation(){

    $(function () {

        //隐藏错误提示框
        $('.alert-danger').css("display", "none");

        $("#jqGrid").jqGrid({
            // 设置API
            url: '/seckill/list',
            loadBeforeSend: function(jqXHR) {
                jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
            },
            datatype: "json",
            colModel: [
                // 设置列表表头
                {label: 'ID', name: 'id', index: 'id', width: 30, key: true},
                {label: 'Goods ID', name: 'goodsId', index: 'goodsId', width: 30},
                {label: 'Title', name: 'title', index: 'title', width: 60},
                {label: 'Price', name: 'seckillPrice', index: 'seckillPrice', width: 30,  editable: true, formatter:priceFormatter},
                {label: 'Stock', name: 'seckillStock', index: 'seckillStock', width: 30},
                {label: 'Start Time', name: 'startTime', index: 'startTime', width: 50, editable: true, formatter:utcToLocalFormatter},
                {label: 'End Time', name: 'endTime', index: 'endTime', width: 50, editable: true, formatter:utcToLocalFormatter},
                {label: 'Purchase Limit', name: 'purchaseLimitNum', index: 'purchaseLimitNum', width: 30},
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
                //隐藏grid底部滚动条
                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
            },
        });

        // 搜索功能
        $("#searchButton").click(function(){
            let keyword = $("#searchInput").val(); //获取输入框的值
            $("#jqGrid").jqGrid('setGridParam',{
                postData: {'keyword': keyword}, //设置postData参数
                page: 1
            }).trigger("reloadGrid"); //重新加载JqGrid
        });

        $(window).resize(function () {
            $("#jqGrid").setGridWidth($(".card-body").width());
        });
    });
}

/**
 * jqGrid 重新加载
 */
function reload() {
    let page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function reset() {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");
    //清空数据
    $('#modal-id').val('');
    $('#modal-goodsId').val('');
    $('#modal-title').val('');
    $('#modal-price').val('0');
    $('#modal-stock').val('0');
    $('#modal-startTime').val('');
    $('#modal-endTime').val('');
    $('#modal-purchaseLimitationNumber').val('0');
    $('#modal-isAvailable').val(1);
}

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

contentsPreparation();

/**
 * 数据验证
 */
function validObject() {
    let goodsId = $('#modal-goodsId').val();
    if (isNull(goodsId))  {
        showErrorInfo("The goodsId can not be blank!");
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

    let startTime = $('#modal-startTime').val();
    if (isNull(startTime)) {
        showErrorInfo("Start time cannot be empty!");
        return false;
    }
    let endTime = $('#modal-endTime').val();
    if (isNull(endTime)) {
        showErrorInfo("End time cannot be empty!");
        return false;
    }
    if (startTime >= endTime) {
        showErrorInfo("Start time must be earlier than end time!");
        return false;
    }
    let purchaseLimitationNumber = $('#modal-purchaseLimitationNumber').val();
    if (isNull(purchaseLimitationNumber)) {
        showErrorInfo("Purchase limitation number cannot be empty!");
        return false;
    }
    let isAvailable = $('#modal-isAvailable').val();
    if (isNull(isAvailable)) {
        showErrorInfo("Is available cannot be empty!");
        return false;
    }

    return true;
}

/**
 * 重置 modal 表单数据
 */
// Grid 顶部的操作按钮
function addSeckillEvent() {
    reset();
    $('.modal-title').html('Add');
    $('#seckillEventModal').modal('show');
}
function editSeckillEvent() {
    reset();
    $('.modal-title').html('Edit');

    let id = getSelectedRow();
    if (id == null) {
        return;
    }

    //请求数据
    $.ajax({
        url: "/seckill/info",
        data: {id: id},
        type: "GET",
        dataType: "json",
        beforeSend: function (request) {
            //设置header值
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0 && r.data != null) {
                //填充数据 至 modal
                $('#modal-id').val(r.data.id);
                $('#modal-goodsId').val(r.data.goodsId);
                $('#modal-title').val(r.data.title);
                $('#modal-price').val(r.data.seckillPrice);
                $('#modal-stock').val(r.data.seckillStock);
                $('#modal-startTime').val(utcToLocal(r.data.startTime));
                $('#modal-endTime').val(utcToLocal(r.data.endTime));
                $('#modal-purchaseLimitationNumber').val(r.data.purchaseLimitNum);
                $('#modal-isAvailable').val(r.data.isAvailable);

                if (r.data.startTime < new Date().getTime()) {
                    document.getElementById("modal-goodsId").disabled = true;
                    document.getElementById("modal-startTime").disabled = true;
                    document.getElementById("modal-title").disabled = true;
                    document.getElementById("modal-price").disabled = true;
                    document.getElementById("modal-stock").disabled = true;
                    document.getElementById("modal-purchaseLimitationNumber").disabled = true;
                }

            }else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        }
    });

    //显示 modal
    $('#seckillEventModal').modal('show');
}
function previewSeckillEvent() {
    let id = getSelectedRow();
    if (id == null) {
        return;
    }
    window.open("/seckill-details.html?id=" + id);
}
function localToUtc(time) {
    // 创建一个新的Date对象
    let date = new Date(time);
    // 转换为ISO格式的字符串
    return date.toISOString();
}

function utcToLocal(dateStr) {
// 创建一个新的 Date 实例
    let date = new Date(dateStr);

    // 使用 Intl.DateTimeFormat 来格式化日期
    // options 对象定义了我们想要的日期和时间的格式
    let formatter = new Intl.DateTimeFormat('default', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
    });

    // 使用 formatter.format 来格式化日期
    let formattedDate = formatter.format(date);

    // 将 formattedDate 从 "MM/DD/YYYY, HH:MM:SS" 转换为 "YYYY-MM-DDTHH:MM:SS"
    let parts = formattedDate.split(/[\s,/]+/);
    formattedDate = `${parts[0]}-${parts[1]}-${parts[2]}T${parts[3]}`;

    return formattedDate;

}

//绑定 modal 表单上的 SAVE 按钮
$('#saveButton').click(async function () {
    //验证数据
    if (validObject()) {

        // Ajax 发送网络请求
        // 获取表单数据
        let id = $('#modal-id').val();
        let goodsId = $('#modal-goodsId').val();
        let title = $('#modal-title').val();
        let price = $('#modal-price').val();
        let stock = $('#modal-stock').val();
        let startTime = localToUtc($('#modal-startTime').val());
        let endTime = localToUtc($('#modal-endTime').val());
        let purchaseLimitationNumber = $('#modal-purchaseLimitationNumber').val();
        let isAvailable = $('#modal-isAvailable').val();

        // 将即将发送数据封装为Json, 和 Pojo 对应
        let data = {
            "id": id,
            "goodsId": goodsId,
            "title": title,
            "seckillPrice": price,
            "seckillStock": stock,
            "startTime": startTime,
            "endTime": endTime,
            "purchaseLimitNum": purchaseLimitationNumber,
            "isAvailable": isAvailable
        };

        let url;
        let method;

        // 表示新增操作
        if (id === null || id === "" || id === undefined || id < 0) {
            url = '/admin/seckill/add';
            method = 'POST';
        }else {
            // id>=0表示编辑操作
            url = '/admin/seckill/edit';
            method = 'PUT';
        }

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
                    $('#seckillEventModal').modal('hide');
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



