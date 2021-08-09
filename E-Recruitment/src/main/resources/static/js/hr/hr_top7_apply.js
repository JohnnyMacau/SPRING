$('[name="Submit2"]').on(
    {
        click: function () {
            let isLogIn = $('#tab').data('islogin');
            let reLogin = $('#tab').data('reLogin');
            checkSessionTimeoutAndJump2(checkTagAndAddTagData(isLogIn, reLogin),ROOT+'/hr_top2.html/'+$('#dept_pos_detail_id').val())
        }
    }
);
