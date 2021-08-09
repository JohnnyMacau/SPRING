let ORIGINAL_FORM;
let CURRENT_GOTO;
let ajaxCompleted = false;
let common_dialog_flag = 0;

function runDialogJS() {
    common_dialog_flag += 1;
    // console.log(common_dialog_flag)
    ajaxCompleted = common_dialog_flag === 2;
    if (ajaxCompleted) {
        ORIGINAL_FORM = $("#form-1").serialize();
    }

}

$(function () {

    // ORIGINAL_FORM = $("#form-1").serialize();

    let dialog = $('dialog')[0],
        $closeDialog = $('#closeDialog');


    $closeDialog.on('click', function () {
        // dialog.close();
        send(this.form,'dialog_saveAndGo');
    });
    $('#goAndWithoutSave').on('click', function () {
        location.href = CURRENT_GOTO;
    });

    let isLogIn = $('#tab').data('islogin');
    let reLogin = $('#tab').data('reLogin');

    $('.a_goTop').on(
        {
            click: function () {

                CURRENT_GOTO = $(this).data('thref');


                let data = checkTagAndAddTagData(isLogIn, reLogin);

                if (data['tag'] !== 'inRegister') {
                    if (ajaxCompleted && ORIGINAL_FORM !== $("#form-1").serialize()) {
                        // confirm('轉換分頁前請先儲存，否則內容將會消失')
                        dialog.showModal();
                        return;
                    }
                }


                checkSessionTimeoutAndJump(data);


            }
        }
    );
});


