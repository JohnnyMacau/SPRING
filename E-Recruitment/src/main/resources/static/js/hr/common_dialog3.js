let ORIGINAL_FORM;
let CURRENT_GOTO;

$(function () {

    // ORIGINAL_FORM = $("#form-1").serialize();

   /* let dialog = $('dialog')[0],
        $closeDialog = $('#closeDialog');


    $closeDialog.on('click', function () {
        dialog.close();
    });
    $('#goAndWithoutSave').on('click', function () {
        location.href = CURRENT_GOTO;
    });*/

    let isLogIn = $('#tab').data('islogin');
    let reLogin = $('#tab').data('reLogin');

    $('.a_goTop').on(
        {
            click: function () {

                CURRENT_GOTO = $(this).data('thref');


                let data = checkTagAndAddTagData(isLogIn, reLogin);

               /* if (data['tag'] !== 'inRegister') {
                    if (ORIGINAL_FORM !== $("#form-1").serialize()) {
                        // confirm('轉換分頁前請先儲存，否則內容將會消失')
                        dialog.showModal();
                        return;
                    }
                }*/

                checkSessionTimeoutAndJump2(data,ROOT+CURRENT_GOTO);

            }
        }
    );
});


