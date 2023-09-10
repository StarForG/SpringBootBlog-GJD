/**
 * 教程js
 */
// Tags Input
$('#tags_cs').tagsInput({
    width: '100%',
    height: '35px',
    defaultText: ' '
});

$('.toggle').toggles({
    on: true,
    text: {
        on: '开启',
        off: '关闭'
    }
});

$(".select2").select2({
    width: '100%'
});

var tale = new $.tale();

/**
 * 保存教程
 * @param status
 */
function subArticle2(status) {
    var title = $('#courseForm input[name=title]').val();
    var content = $('#text_cs').val();
    if (title == '') {
        tale.alertWarn('请输入教程标题');
        return;
    }
    if (content == '') {
        tale.alertWarn('请输入教程内容');
        return;
    }
    $('#content-editor_cs').val(content);
    $("#courseForm #status_cs").val(status);
    $("#courseForm #categories_cs").val($('#multiple-sel').val());
    $("#courseForm #type_cs").val($('#multiple-type').val());
    var params = $("#courseForm").serialize();
    console.log(params);
    var url = $('#courseForm #csid').val() != '' ? '/admin/course/modify_cs' : '/admin/course/publish';
    tale.post({
        url:url,
        data:params,
        success: function (result) {
            if (result && result.code == 'success') {
                tale.alertOk({
                    text:'教程保存成功',
                    then: function () {
                        setTimeout(function () {
                            window.location.href = '/admin/course';
                        }, 500);
                    }
                });
            } else {
                tale.alertError(result.msg || '保存教程失败');
            }
        }
    });
}


function allow_comment(obj) {
    var this_ = $(obj);
    var on = this_.find('.toggle-on.active').length;
    var off = this_.find('.toggle-off.active').length;
    if (on == 1) {
        $('#allow_comment').val(false);
    }
    if (off == 1) {
        $('#allow_comment').val(true);
    }
}

function allow_ping(obj) {
    var this_ = $(obj);
    var on = this_.find('.toggle-on.active').length;
    var off = this_.find('.toggle-off.active').length;
    if (on == 1) {
        $('#allow_ping').val(false);
    }
    if (off == 1) {
        $('#allow_ping').val(true);
    }
}


function allow_feed(obj) {
    var this_ = $(obj);
    var on = this_.find('.toggle-on.active').length;
    var off = this_.find('.toggle-off.active').length;
    if (on == 1) {
        $('#allow_feed').val(false);
    }
    if (off == 1) {
        $('#allow_feed').val(true);
    }
}

$('div.allow-false').toggles({
    off: true,
    text: {
        on: '开启',
        off: '关闭'
    }
});

$('#multiple-type').change(function () {
    var postType = $('#multiple-type').val();
    var tags = $('#tags');
    var categories = $('#multiple-sel');
    if(postType == 'post'){
        $('#tags_tagsinput').show(500);
        $('#s2id_multiple-sel').show(500);
        $('#comment-div').attr("style","display:block;");
    }else {
        $('#tags_tagsinput').hide(500);
        $('#s2id_multiple-sel').hide(500);
        $('#comment-div').attr("style","display:none;");

    }
});

$(function () {
    var postType = $('#multiple-type').val();
    var tags = $('#tags');
    var categories = $('#multiple-sel');
    if(postType == 'post'){
        $('#tags_tagsinput').show();
        $('#s2id_multiple-sel').show();
        $('#comment-div').attr("style","display:block;");
    }else {
        $('#tags_tagsinput').hide();
        $('#s2id_multiple-sel').hide();
        $('#comment-div').attr("style","display:none;");

    }
});
