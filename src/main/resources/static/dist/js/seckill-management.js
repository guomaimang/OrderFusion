function contentsPreparation(){

    $(function () {

        
        $('.alert-danger').css("display", "none");

        $("#jqGrid").jqGrid({
            
            url: '/seckill/list',
            loadBeforeSend: function(jqXHR) {
                jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
            },
            datatype: "json",
            colModel: [
                
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
                
                $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
            },
        });

        
        $("#searchButton").click(function(){
            let keyword = $("#searchInput").val(); 
            $("#jqGrid").jqGrid('setGridParam',{
                postData: {'keyword': keyword}, 
                page: 1
            }).trigger("reloadGrid"); 
        });

        $(window).resize(function () {
            $("#jqGrid").setGridWidth($(".card-body").width());
        });
    });
}


function reload() {
    let page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function reset() {
    
    $('.alert-danger').css("display", "none");
    
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

function addSeckillEvent() {
    reset();
    $('.modal-title').html('Add');
    $('#seckillEventModal').modal('show');

    document.getElementById("modal-goodsId").disabled = false;
    document.getElementById("modal-startTime").disabled = false;
    document.getElementById("modal-title").disabled = false;
    document.getElementById("modal-price").disabled = false;
    document.getElementById("modal-stock").disabled = false;
    document.getElementById("modal-purchaseLimitationNumber").disabled = false;
}
function editSeckillEvent() {
    reset();
    $('.modal-title').html('Edit');

    let id = getSelectedRow();
    if (id == null) {
        return;
    }

    
    $.ajax({
        url: "/seckill/info",
        data: {id: id},
        type: "GET",
        dataType: "json",
        beforeSend: function (request) {
            
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function (r) {
            if (r.code === 0 && r.data != null) {
                
                $('#modal-id').val(r.data.id);
                $('#modal-goodsId').val(r.data.goodsId);
                $('#modal-title').val(r.data.title);
                $('#modal-price').val(r.data.seckillPrice);
                $('#modal-stock').val(r.data.seckillStock);
                $('#modal-startTime').val(utcToLocal(r.data.startTime));
                $('#modal-endTime').val(utcToLocal(r.data.endTime));
                $('#modal-purchaseLimitationNumber').val(r.data.purchaseLimitNum);
                $('#modal-isAvailable').val(r.data.isAvailable);

                console.log("startTime: " + r.data.startTime);
                console.log("new Date().getTime(): " + new Date().getTime());
                if (new Date(r.data.startTime) < new Date()) {
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
    
    let date = new Date(time);
    
    return date.toISOString();
}

function utcToLocal(dateStr) {

    let date = new Date(dateStr);

    
    
    let formatter = new Intl.DateTimeFormat('default', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit',
        hour12: false,
    });

    
    let formattedDate = formatter.format(date);

    
    let parts = formattedDate.split(/[\s,/]+/);
    formattedDate = `${parts[0]}-${parts[1]}-${parts[2]}T${parts[3]}`;

    return formattedDate;

}


$('#saveButton').click(async function () {
    
    if (validObject()) {

        
        
        let id = $('#modal-id').val();
        let goodsId = $('#modal-goodsId').val();
        let title = $('#modal-title').val();
        let price = $('#modal-price').val();
        let stock = $('#modal-stock').val();
        let startTime = localToUtc($('#modal-startTime').val());
        let endTime = localToUtc($('#modal-endTime').val());
        let purchaseLimitationNumber = $('#modal-purchaseLimitationNumber').val();
        let isAvailable = $('#modal-isAvailable').val();

        
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

        
        if (id === null || id === "" || id === undefined || id < 0) {
            url = '/admin/seckill/add';
            method = 'POST';
        }else {
            
            url = '/admin/seckill/edit';
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



