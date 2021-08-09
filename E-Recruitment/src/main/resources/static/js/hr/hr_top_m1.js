function forgetUserNameOrPassword() {
    document.location.replace(ROOT + '/hr_top_m2.html');
}

$(function () {
    if (window.$ && window.$.ajaxSetup) {
        $.ajaxSetup({
            "contentType": "application/x-www-form-urlencoded;charset=utf-8",
            "dataType": "json"
        });
    }


    $('#first_button').on(
        {
            click: function () {
                exit_method();
            }
        }
    );
    $('#forget').on(
        {
            click: function () {
                forgetUserNameOrPassword();
            }
        }
    );

    $('#self_a_checkCookie').on(
        {
            click: function () {
                checkCookie();
            }
        }
    );

    $('[name="Login"]').on(
        {
            click: function () {
                login();
            }
        }
    );
    $("body").on(
        {
            keydown: function () {
                keyLogin();
            }
        }
    );

});

function checkCookie() {
    if ($.cookie('GoBackToPostListHtmlStatus')) {
        $.cookie('GoBackToPostListHtmlStatus', 'no', {
            path: "/",
        });
    }
}


function keyLogin() {
    if (event.keyCode === 13)  //回车键的键值为13
    {
        event.preventDefault();
        $('[name="Login"]').click(); //调用登录按钮的登录事件
    }

}


function login() {
    if ($('#user_name').val() === "") {
        alert("用戶名不能為空");
        return false;
    }

    if ($('#password').val() === "") {
        alert("密碼不能為空");
        return false;
    }


    var url = ROOT + "/applicant/session";
    var data = $("#form-1").serialize();
    $.ajax({
        "url": url,
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {
                setStatusToCookie(json.data.status);
                setTokenToCookie(json.data.token);//兼容旧的,新的可删除这行
                if (json.data.status === 'Y') {
                    let GoBackToPostListHtmlStatus = $.cookie('GoBackToPostListHtmlStatus');
                    if (!GoBackToPostListHtmlStatus||GoBackToPostListHtmlStatus==='no') {
                        location = ROOT + "/hr_top1.html";
                    }else {
                        $.cookie('GoBackToPostListHtmlStatus', 'no', {
                            path: "/",
                        });
                        let positionIdInApplicationList = String(json.data.positionIdInApplicationList).split(',');
                        if (positionIdInApplicationList.indexOf(GoBackToPostListHtmlStatus) >= 0) {
                            location = ROOT + "/hr_post_list.html";
                        } else {
                            location = ROOT + "/hr_top2.html/"+GoBackToPostListHtmlStatus;
                        }
                    }
                } else {
                    location = ROOT + "/hr_top" + json.data.incompletePageNo + ".html";//兼容旧的,新的可删除这行
                }
            } else {
                alert(json.message);
            }
        }
    });
}

function exit_method() {

    var url = ROOT + "/applicant/delete/session";

    $.ajax({
        "url": url,
        "type": "post",
        "dataType": "json",
        "success": function () {
            document.location.replace(ROOT + '/hr_top0.html');
        },
        "error": function (xhr) {
        }
    });
}

function setStatusToCookie(status) {
    // var expiresDate = new Date();
    // expiresDate.setTime(expiresDate.getTime() + (60 * 60 * 1000 / 2));
    /* $.cookie('status', status, {path: "/",
         // expires: expiresDate
     });*/

    if(!window.localStorage){
        alert("not support localstorage");
        return false;
    }else{
        var storage=window.localStorage;
        storage.setItem("status",status);
    }
}

function setTokenToCookie(token) {
    // var expiresDate = new Date();
    // expiresDate.setTime(expiresDate.getTime() + (60 * 60 * 1000 / 2));
    /*   $.cookie('XSRF-TOKEN', token, {path: "/",
           // expires: expiresDate
       });*/


    if(!window.localStorage){
        alert("not support localstorage");
        return false;
    }else{
        var storage=window.localStorage;
        storage.setItem("XSRF-TOKEN",token);
    }
}
