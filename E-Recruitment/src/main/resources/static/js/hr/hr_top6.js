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
    if (window.localStorage.getItem("status") === "Y") {
        $("#HR_bar").attr("src", "images/HR_bar_a8.jpg");
    } else {
        $.get(ROOT + "/applicant/pageNo", function (json) {
            if (json != null && json != "null") {
                $("#HR_bar").attr("src", "images/HR_bar_a" + json + ".jpg");
            }
        });
    }
    $('#on_shift_yes').on(
        {
            click: function () {
                show(this.value);
            }
        }
    );


    $('#on_shift_no').on(
        {
            click: function () {
                show(this.value);
            }
        }
    );


    $('[name="Submit2"]').on(
        {
            click: function () {
                window.location = ROOT + '/hr_top5.html';
            }
        }
    );

    $('[name="Submit"]').on(
        {
            click: function () {
                send(this.form);
            }
        }
    );


    getData();
});

function getData() {
    $.get(ROOT + "/applicant/t6/data", function (json) {
        if (json.state !== 200) {
            return;
        }
        var info = json.data;
        if (info.length == 0) {
            $("#on_shift_no").prop("checked", true);
            return;
        }

        $('#on_shift_yes').click();
        // for (var i = 1; i <= 7; i++) {
        for (var i = 1; i <= 1; i++) {

            var arr = info[i - 1].fromDate.split(":");
            $("#from_hour" + i).val(arr[0]);
            $("#from_min" + i).val(arr[1]);
            var arr2 = info[i - 1].toDate.split(":");
            $("#to_hour" + i).val(arr2[0]);
            $("#to_min" + i).val(arr2[1]);
        }

    });
}


function show(value) {
    if (value == 'Y') {
        document.getElementById('show1').style.display = "";
        document.getElementById('show2').style.display = "";
    } else {
        document.getElementById('show1').style.display = "none";
        document.getElementById('show2').style.display = "none";
    }
}

function send(form) {

    var is_send = true;
    if (document.getElementById('on_shift_yes').checked == true) {
        // for (i = 1; i < 8; i++) {
        for (i = 1; i < 2; i++) {

            from_hour = 'from_hour' + i;
            from_min = 'from_min' + i;
            to_hour = 'to_hour' + i;
            to_min = 'to_min' + i;
            font = 'font' + i;
            if (parseInt(document.getElementById(from_hour).value) > parseInt(document.getElementById(to_hour).value)) {
                is_send = false;
                document.getElementById(font).color = 'red';
                document.getElementById("error").innerHTML = "<font color='red'>开始时间大于结束时间</font><br>";

            } else if (parseInt(document.getElementById(from_hour).value) == parseInt(document.getElementById(to_hour).value)) {
                if (parseInt(document.getElementById(from_min).value) > parseInt(document.getElementById(to_min).value)) {
                    is_send = false;
                    document.getElementById(font).color = 'red';
                    document.getElementById("error").innerHTML = "<font color='red'>开始时间大于结束时间</font><br>";
                }
            }
        }
    }
    // alert(is_send)
    if (is_send == true) {
        submit();
    }
}

function submit() {

    var url = ROOT + "/applicant/top6";

    var data = $("#form-1").serialize();
    // if ($("#on_shift_no").prop("checked")) data = null;
    if ($("#on_shift_no").prop("checked")) data = '{}';
    // alert(data)
    $.ajax({
        "url": url,
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {
                location = ROOT + "/hr_top7.html";
            } else {
                if (json.state !== 999)
                    alert(json.message);
            }
        }
        ,
        "error": function (xhr) {
            // if (xhr.status === 200) {
                alert("登录过期,请重新登录")
                location = ROOT + "/hr_top_m1.html";
            // }
        }
    });
}
