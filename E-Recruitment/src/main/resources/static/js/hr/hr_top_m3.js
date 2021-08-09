$(function () {

    if (window.$ && window.$.ajaxSetup) {
        $.ajaxSetup({
            "contentType": "application/x-www-form-urlencoded;charset=utf-8",
            "dataType": "json",
            beforeSend: function (xhr) {
                 var storage=window.localStorage;
xhr.setRequestHeader("X-XSRF-TOKEN",storage.getItem("XSRF-TOKEN"));
            }
        });
    }


    $('[name="Submit2"]').on(
        {
            click: function () {
                window.location = ROOT + '/hr_top1.html';
            }
        }
    );
    $('[name="Submit"]').on(
        {
            click: function () {
                check();
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


function keyLogin() {
    if (event.keyCode === 13)  //回车键的键值为13
    {
        event.preventDefault();
        $('[name="Submit"]').click();
    }

}

function check() {

    var old_password = $("#old_password").val();
    var new_password1 = $("#new_password1").val();
    var new_password2 = $("#new_password2").val();

    if (old_password === "") {
        alert("登入密码不能为空");
        return;
    }
    if (new_password1 === "") {
        alert("新密码不能为空");
        return;
    }
    if (new_password2 === "") {
        alert("确认新密码不能为空");
        return;
    }


    if (old_password == new_password1) {
        alert("新密码不能和旧密码相同");
        return;
    }
    if (new_password1 !== new_password2) {
        alert("两次输入的新密码不正确");
        return;
    }

    var pattern1 = /^[a-z0-9]{6,}$/i;
    var pattern2 = /^[a-z]+$/;
    var pattern3 = /^[0-9]+$/;


    if (!pattern1.test(new_password1) ||
        pattern2.test(new_password1) ||
        pattern3.test(new_password1)) {
        alert("密码必须由英文字母及数字组成且长度最少6个字");
        return;

    }
    submit();
}

function submit() {

    document.getElementById('type').value = 'password';

    var url = ROOT + "/applicant/password";


    var data = $("#form-1").serialize();


    console.log(data);


    $.ajax({
        "url": url,
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {
                alert("密码更改成功!");
                location = ROOT + "/hr_top_m1.html";
            } else {
                alert(json.message);
            }
        }
    });
}
