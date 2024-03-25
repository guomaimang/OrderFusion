$(function () {

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
            {label: 'Name', name: 'name', index: 'name', width: 100},
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
            let selectedRowIndex = $("#" + this.id).getGridParam('selrow');
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
 * jqGrid 重新加载
 */
function reload() {
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