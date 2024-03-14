/**
 * get the sha256 value of the input
 * @param input
 * @returns {Promise<string>}
 */

async function sha256(input) {
    return crypto.subtle
        .digest('SHA-256', new TextEncoder('utf-8').encode(input))
        .then(h => {
            let hexes = [],
                view = new DataView(h);
            for (let i = 0; i < view.byteLength; i += 4)
                hexes.push(('00000000' + view.getUint32(i).toString(16)).slice(-8));
            return hexes.join('');
        });
}


/**
 * 参数长度验证
 *
 * @param obj
 * @param length
 * @returns {boolean}
 */

function validLength(obj, length) {
    return obj.trim().length < length;

}


/**
 * 获取jqGrid选中的一条记录
 * @returns {*}
 */
function getSelectedRow() {
    let grid = $("#jqGrid");
    let rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    let selectedIDs = grid.getGridParam("selarrrow");
    if (selectedIDs.length > 1) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    return selectedIDs[0];
}


/**
 * 获取jqGrid选中的多条记录
 * @returns {*}
 */
function getSelectedRows() {
    let grid = $("#jqGrid");
    let rowKey = grid.getGridParam("selrow");
    if (!rowKey) {
        swal("Please select one record!", {
            icon: "error",
        });
        return;
    }
    return grid.getGridParam("selarrrow");
}

/**
 * display error info
 * @param info
 */
function showErrorInfo(info) {
    $('.alert-danger').css("display", "block");
    $('.alert-danger').html(info);
}

function utcToLocalReadable(s) {
    let date = new Date(s);
    return date.toLocaleString();
}

function toggleDisplay(elementId, displayStyle) {
    let element = document.getElementById(elementId);
    if (element) {
        element.style.display = displayStyle;
    }
}

/**
 * 获取url参数
 * @param name
 * @returns {null|string}
 */

function getQueryParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return null;
}

/**
 * 判断是否为空
 * @param obj
 * @returns {boolean}
 */
function isNull(obj) {
    if (obj == null || obj == undefined || obj.trim() === "") {
        return true;
    }
    return false;
}