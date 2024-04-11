$(function () {
    
    $('.alert-danger').css("display", "none");

    $('#userModal').modal('hide');

    $("#jqGrid").jqGrid({
        
        url: 'admin/user/list',
        loadBeforeSend: function(jqXHR) {
            jqXHR.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        datatype: "json",
        colModel: [
            
            {label: 'ID', name: 'id', index: 'id', width: 30, key: true, hidden: false},
            {label: 'Name', name: 'name', index: 'name', width: 50},
            {label: 'Email', name: 'email', index: 'email', width: 120},
            {label: 'Registration Time', name: 'registerTime', index: 'registerTime', width: 120, editable: true, formatter:utcToLocalFormatter},
            {label: 'Frozen', name: 'isFrozen', index: 'isFrozen', width: 30, editable: true, formatter:isFrozenFormatter},
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
        let searchKeyword = $("#searchInput").val(); 
        $("#jqGrid").jqGrid('setGridParam',{
            postData: {'keyword': searchKeyword}, 
            page: 1
        }).trigger("reloadGrid"); 
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});



/**
 * 数据验证
 */
function validObject() {
    let email = $('#modal-email').val();
    if (isNull(email))  {
        showErrorInfo("The email can not be blank!");
        return false;
    }
    if (!validLength(email, 120)) {
        showErrorInfo("Email length cannot be larger than 120!");
        return false;
    }


    let name = $('#modal-name').val();
    if (isNull(name)) {
        showErrorInfo("Name cannot be empty!");
        return false;
    }
    if (!validLength(name, 50)) {
        showErrorInfo("Name length cannot be larger than 50!");
        return false;
    }

    let password = $('#modal-password').val();
    if (!isNull(password)) {
        if (!validLength(name, 40)) {
            showErrorInfo("Password length cannot be larger than 40!");
            return false;}
    }

    return true;
}


function addUser() {
    reset();
    $('.modal-title').html('Add');
    $('#userModal').modal('show');
}
function editUser() {
    reset();
    $('.modal-title').html('Edit');

    let id = getSelectedRow();
    if (id == null) {
        return;
    }
    
    $.ajax({
        url: "/admin/user/info",
        type: "GET",
        data: {
            id: id
        },
        beforeSend: function (request) {
            
            request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
        },
        success: function(r) {
            if (r.code === 0 && r.data != null) {
                
                $('#modal-id').val(r.data.id);
                $('#modal-email').val(r.data.email);
                $('#modal-name').val(r.data.name);
                $('#modal-registerTime').val(utcToLocalFormatter(r.data.registerTime));
                $('#modal-isFrozen').val(isFrozenFormatter(r.data.isFrozen));
            }
        }
    });

    
    $('#userModal').modal('show');
}
function lockSwitch() {
    let id = getSelectedRow();
    if (id == null) {
        return;
    }

    swal({
        title: "Confirmation Box",
        text: "Confirm to lock/unlock the selected user?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "PUT",
                url: "admin/user/lockswitch",
                contentType: "application/json",
                beforeSend: function (request) {
                    
                    request.setRequestHeader("jwt", window.localStorage.getItem("jwt"));
                },
                data: JSON.stringify({"id": id, "isFrozen": $("#jqGrid").jqGrid('getRowData', id).isFrozen === "Normal" ? 0 : 1}),
                success: function (r) {
                    if (r.code === 0) {
                        swal("Update Successfully", {
                            icon: "success",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    }else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    });

    reload();
}


$('#saveButton').click(async function () {
    
    if (validObject()) {

        

        
        let id = $("#modal-id").val();
        let name = $("#modal-name").val();
        let email = $("#modal-email").val();
        let password = await sha256($("#modal-password").val());

        
        let data = {
            "id": id,
            "name": name,
            "email": email,
            "password": password,
        };
        let url;
        let method;

        
        if (id === null || id === "" || id === undefined || id < 0) {
            url = '、admin/user/add';
            method = 'POST';
        }else {
            
            url = '/admin/user/edit';
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
                    $('#userModal').modal('hide');
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
    $('#modal-email').val('');
    $('#modal-name').val('');
    $('#modal-password').val('');
    $('#modal-registerTime').val('');
    $('#modal-isFrozen').val('0');
}

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
function isFrozenFormatter(cellValue) {
    if (cellValue === 1) {
        return "Locked";
    } else {
        return "Normal";
    }
}

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}