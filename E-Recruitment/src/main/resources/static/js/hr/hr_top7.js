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
                let isLogIn = $('#tab').data('islogin');
                let reLogin = $('#tab').data('reLogin');
                checkSessionTimeoutAndJump2(checkTagAndAddTagData(isLogIn, reLogin),ROOT + '/hr_top5.html')
                // window.location = ROOT + '/hr_top5.html';
            }
        }
    );
   /* if (window.localStorage.getItem("status") !== "Y") {
        $('.ff').css('opacity','1');
    }*/


    $('#suspendAllApplicant').on(
        {
            click: function () {
                alert('目前您已有三個應徵申請處理中,請待工作人員處理完再申請');
            }
        }
    );
});
