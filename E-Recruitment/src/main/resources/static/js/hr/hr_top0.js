function chk(input) {
    if (input == 'yes') {
        document.getElementById('nocheckbox').checked = false;
    }
    else {

        document.getElementById('yescheckbox').checked = false;
    }
    return true;
}

function checkyes(form) {
    if (document.getElementById('yescheckbox').checked == true) {
        // location = "hr_top1.html";
        // form.action="hr_top1.html";
        // form.submit();
        $.ajax({
            "url":ROOT+ '/applicant/top0',
            "type": "post",
            // "data": {},
            "dataType": "json",
            "success": function (json) {
                if (json.state === 200) {
                    location.href = "hr_top1.html";
                }
            }
        });
    }
    else if (document.getElementById('nocheckbox').checked == true) {
        document.getElementById("error").innerHTML = "<font color='red'>閣下選擇了“否”，您的職位申請將不會被儲存及遞交。</font><br>";
    }
    else document.getElementById("error").innerHTML = "<font color='red'>閣下還沒選擇“是“或者“否”,請選擇後再遞交</font><br>";
}
$(function () {
    if (window.$ && window.$.ajaxSetup) {
        $.ajaxSetup({
            "contentType": "application/x-www-form-urlencoded;charset=utf-8",
            "dataType": "json"
        });
    }
    $('#yescheckbox').on(
        {
            click: function () {
                return chk(this.value);
            }
        }
    );
    $('#nocheckbox').on(
        {
            click: function () {
                return chk(this.value);
            }
        }
    );

    $('[name="Submit"]').on(
        {
            click: function () {
                checkyes(this.form);
            }
        }
    );

});
