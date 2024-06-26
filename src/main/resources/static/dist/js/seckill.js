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
                window.open("/seckill-details.html?id=" + selectedRowIndex);
            },
        });

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


function reload() {
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

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}

contentsPreparation();