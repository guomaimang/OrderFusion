$(function () {

    $('.alert-danger').css("display", "none");

    $('#goodsModal').modal('hide');

    $("#jqGrid").jqGrid({
        url: '/admin/order/list',
        loadBeforeSend: function(jqXHR) {
            jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        datatype: "json",
        colModel: [
            {label: 'Order ID', name: 'id', index: 'id', width: 10, key: true, hidden: false},
            {label: 'User ID', name: 'userId', index: 'userId', width: 10},
            {label: 'Goods ID', name: 'goodsId', index: 'goodsId', width: 10},
            {label: 'Goods Name', name: 'goodsName', index: 'goodsName', width: 20},
            {label: 'Payment', name: 'payment', index: 'payment', width: 10, editable: true, formatter:paymentFormatter},
            {label: 'Channel', name: 'channel', index: 'channel', width: 10, editable: true, formatter:channelFormatter},
            {label: 'Receiver Name', name: 'deliveryReceiver', index: 'deliveryReceiver', width: 10},
            {label: 'Create Time', name: 'createTime', index: 'createTime', width: 20, editable: true, formatter:utcToLocalFormatter},
            {label: 'Status', name: 'status', index: 'status', width: 10, editable: true, formatter:statusFormatter},
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
            page: "pageNum",
            rows: "pageSize",
            order: "order",
        },
        gridComplete: function () {
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
        onSelectRow: function () {
            let selectedRowIndex = $("#" + this.id).getGridParam('selrow');
            window.open("/order-details-admin.html?id=" + selectedRowIndex);
        },
    });

    $("#applyButton").click(function(){
        let searchId = $("#searchId").val();
        let searchName = $("#searchName").val();
        let selectStatus = $("#selectStatus").val();
        let selectChannel = $("#selectChannel").val();

        $("#jqGrid").jqGrid('setGridParam',{
            postData: {
                'searchId': searchId,
                'searchName': searchName,
                'selectStatus': selectStatus,
                'selectChannel': selectChannel
            },
            page: 1
        }).trigger("reloadGrid");
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

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
function paymentFormatter(cellValue) {
    return "HKD " + cellValue;
}

function channelFormatter(cellValue) {
    if (cellValue == 0) {
        return "General";
    }else if (cellValue == 1) {
        return "Seckill";}
    else if (cellValue == 2) {
        return "Admin Create";
    }
}

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}

function statusFormatter(cellValue) {
    if (cellValue == 0) {
        return "Not Paid";
    }else if (cellValue == 1) {
        return "Paid";}
    else if (cellValue == 2) {
        return "Sent Out";
    }else if (cellValue == 3) {
        return "Finished";
    }else if (cellValue == 4) {
        return "Refunded";
    }else if (cellValue == 5) {
        return "Cancelled";
    }
}