$(function () {
    if (window.$ && window.$.ajaxSetup) {
        $.ajaxSetup({
            "contentType": "application/x-www-form-urlencoded;charset=utf-8",
            "dataType": "json"
        });
    }

    $('[name="Submit"]').on(
        {
            click: function () {
                submit();
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
    set_id_type_id_font();


});


function keyLogin() {
    if (event.keyCode === 13)  //回車鍵的鍵值為13
    {
        event.preventDefault();
        $('[name="Submit"]').click();
    }

}

function submit() {

    var email_address = $("#email_address").val();
    var id_type_id = $("#id_type_id").val();

    if (Number(id_type_id) < 0) {
        alert("請選擇證件類別");
        return;
    }


    var pattern
        // = /^[^@^\s]+@[^\.@^\s]+(\.[^\.@^\s]+)+$/;
        = /[A-Za-z0-9]/;
    if (!pattern.test(email_address)) {
        alert("證件格式不正確");
        return;
    }


    var url = ROOT + "/applicant/forgetPassword";
    $.ajax({
        "url": url,
        "type": "post",
        "data": {"idCardNumber": email_address, "idTypeId": id_type_id},
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {
                alert("郵件已發送!");
                location = ROOT + "/hr_top_m1.html";
            } else {
                alert(json.message);
            }
        }
    });
}

function set_id_type_id_font() {
    var url = ROOT + "/msgDescCard";
    $.ajax({
        "url": url,
        "type": "get",
        "async": false,
        "dataType": "json",
        "success": function (json) {
            if (json.state !== 200) {
                alert(json.message);
                return;
            }
            var list = json.data;
            var html = "";
            for (var i = 0; i < list.length; i++) {
                html += "<option value='#{id}'>#{chnDesc}</option>";
                html = html.replace(/#{id}/g, list[i].id);
                html = html.replace(/#{chnDesc}/g, list[i].chnDesc);
            }
            $("#id_type_id").append(html);
        }
    });
}
