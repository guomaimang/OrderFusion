$(function () {

    $('.alert-danger').css("display", "none");

    $('#goodsModal').modal('hide');

    $("#jqGrid").jqGrid({
        url: '/goods/list',
        loadBeforeSend: function(jqXHR) {
            jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        datatype: "json",
        colModel: [
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
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
        onSelectRow: function () {
            let selectedRowIndex = $("#" + this.id).getGridParam('selrow');
            window.open("/goods-details.html?id=" + selectedRowIndex);
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