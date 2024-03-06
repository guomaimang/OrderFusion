var editorD;

$(function () {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    //富文本编辑器
    const E = window.wangEditor;
    editorD = new E('#wangEditor')
    // 设置编辑区域高度为 400px
    editorD.config.height = 400
    //配置服务端图片上传地址
    editorD.config.uploadImgServer = 'images/upload'
    editorD.config.uploadFileName = 'file'
    //限制图片大小 2M
    editorD.config.uploadImgMaxSize = 2 * 1024 * 1024
    //限制一次最多能传几张图片 一次最多上传 1 个图片
    editorD.config.uploadImgMaxLength = 1
    //隐藏插入网络图片的功能
    editorD.config.showLinkImg = false
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

    $('#articleModal').modal('hide');

    $("#jqGrid").jqGrid({
        url: 'articles/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', index: 'id', width: 50, key: true, hidden: true},
            {label: 'Title', name: 'title', index: 'title', width: 240},
            {label: 'Author', name: 'author', index: 'author', width: 120},
            {label: 'PublishTme', name: 'createTime', index: 'createTime', width: 120},
            {label: 'UpdateTime', name: 'updateTime', index: 'updateTime', width: 120}
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
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
    });
    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });
});

//绑定modal上的保存按钮
$('#saveButton').click(function () {
    //验证数据
    if (validObject()) {
        //一切正常后发送网络请求
        //ajax
        var id = $("#articleId").val();
        var title = $("#articleName").val();
        var author = $("#author").val();
        var content = editorD.txt.html();
        var data = {"title": title, "content": content, "author": getCookie("userName")};
        var url = 'articles/add';
        var method = 'POST';
        //id>0表示编辑操作
        if (id > 0) {
            data = {"id": id, "title": title, "content": content, "author": getCookie("userName")};
            url = 'articles/update';
            method = 'PUT';
        }
        $.ajax({
            type: method,//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: url,//url
            contentType: "application/json; charset=utf-8",
            beforeSend: function (request) {
                //设置header值
                request.setRequestHeader("token", getCookie("token"));
            },
            data: JSON.stringify(data),
            success: function (result) {
                checkResultCode(result.resultCode);
                if (result.resultCode == 200) {
                    $('#articleModal').modal('hide');
                    swal("Save successfully", {
                        icon: "success",
                    });
                    reload();
                }
                else {
                    $('#articleModal').modal('hide');
                    swal("Failed to save", {
                        icon: "error",
                    });
                }
                ;
            },
            error: function () {
                swal("Operation failure", {
                    icon: "error",
                });
            }
        });

    }
});

function articleAdd() {
    reset();
    $('.modal-title').html('Add');
    $('#articleModal').modal('show');
}

function articleEdit() {
    reset();
    $('.modal-title').html('Edit');

    var id = getSelectedRow();
    if (id == null) {
        return;
    }
    //请求数据
    $.get("articles/info/" + id, function (r) {
        if (r.resultCode == 200 && r.data != null) {
            //填充数据至modal
            $('#articleId').val(r.data.id);
            $('#articleName').val(r.data.title);
            $('#articleAuthor').val(r.data.author);
            editorD.txt.html(r.data.content);
        }
    });
    //显示modal
    $('#articleModal').modal('show');
}

/**
 * 数据验证
 */
function validObject() {
    var articleName = $('#articleName').val();
    if (isNull(articleName)) {
        showErrorInfo("The title can not be blank!");
        return false;
    }
    if (!validLength(articleName, 120)) {
        showErrorInfo("Title characters cannot be larger than 120!");
        return false;
    }
    var articleAuthor = $('#articleAuthor').val();
    if (isNull(articleAuthor)) {
        showErrorInfo("Author cannot be empty!");
        return false;
    }
    var ariticleContent = editorD.txt.html();
    if (isNull(ariticleContent) || ariticleContent == 'Please enter...') {
        showErrorInfo("Content cannot be empty!");
        return false;
    }
    if (!validLength(ariticleContent, 3000)) {
        showErrorInfo("Content characters cannot be larger than 3000!");
        return false;
    }
    return true;
}

/**
 * 重置
 */
function reset() {
    //隐藏错误提示框
    $('.alert-danger').css("display", "none");
    //清空数据
    $('#articleId').val(0);
    $('#articleName').val('');
    $('#articleAuthor').val('');
    editorD.txt.html('');
}

function deleteArticle() {
    var ids = getSelectedRows();
    if (ids == null) {
        return;
    }
    swal({
        title: "Confirmation Box",
        text: "Confirm to delete data??",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((flag) => {
        if (flag) {
            $.ajax({
                type: "DELETE",
                url: "articles/delete",
                contentType: "application/json",
                beforeSend: function (request) {
                    //设置header值
                    request.setRequestHeader("token", getCookie("token"));
                },
                data: JSON.stringify(ids),
                success: function (r) {
                    checkResultCode(r.resultCode);
                    if (r.resultCode == 200) {
                        swal("Deleted successfully", {
                            icon: "success",
                        });
                        $("#jqGrid").trigger("reloadGrid");
                    } else {
                        swal(r.message, {
                            icon: "error",
                        });
                    }
                }
            });
        }
    });
}

/**
 * jqGrid重新加载
 */
function reload() {
    reset();
    var page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");
}

function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}