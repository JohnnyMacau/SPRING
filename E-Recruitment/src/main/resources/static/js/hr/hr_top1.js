/* function document.onkeydown()
{
if ((event.keyCode==8) )
{
if (window.event.srcElement.tagName.toUpperCase()!="INPUT" && window.event.srcElement.tagName.toUpperCase()!="TEXTAREA" && window.event.srcElement.tagName.toUpperCase()!="TEXT")
{
var r=confirm("確定要回上一步?");
if (r==false)
{
    event.keyCode=0;
    event.returnValue=false;
}
}
}
}
if (typeof window.event != ‘undefined’) {
document.onkeydown = function() {
var type = event.srcElement.type;
var code = event.keyCode;
return ((code != 8 && code != 13) ||
 (type == ‘text’ && code != 13 ) ||
 (type == ‘textarea’) ||
 (type == ‘submit’ && code == 13))
}
} else { // FireFox/Others
document.onkeypress = function(e) {
var type = e.target.type;
var code = e.keyCode;
if ((code != 8 && code != 13) ||
(type == ‘text’ && code != 13 ) ||
(type == ‘textarea’) ||
(type == ‘submit’ && code == 13)) {
return true
} else {
alert(‘您真的想放棄現在正在編輯的內容嗎？再想想！’);
return false
}
}
}
*/

function showdate(value) {
    var daycount;
    var year;
    var showday;
    if (value > 0 & value < 13) {
        if (value == 1 || value == 3 || value == 5 || value == 7 || value == 8 || value == 10 || value == 12) {
            daycount = 31;
        } else if (value == 4 || value == 6 || value == 9 || value == 11) {
            daycount = 30;
        } else {
            year = document.getElementById('year').value;
            daycount = 28;
            if (year > 0) {
                if (year % 4 == 0) {
                    if (year % 100 != 0) {
                        daycount = 29;
                    } else if (year % 400 == 0) {
                        daycount = 29;
                    } else daycount = 28;
                } else daycount = 28;
            }

        }
        showday = document.getElementById('day');
        selectday = showday.selectedIndex;
        showday.options.length = 0;
        showday.options.add(new Option('日', -1));
        for (var i = 1; i <= daycount; i++) {
            showday.options.add(new Option(i, i));
        }
        if (selectday > 0 & selectday <= daycount) {
            showday.value = selectday;
        }
    } else if (value > 0 & value > 13) {
        showmonth = document.getElementById('month');
        if (showmonth.selectedIndex == 2) {
            if (value % 4 == 0) {
                if (value % 100 != 0) {
                    daycount = 29;
                } else if (value % 400 == 0) {
                    daycount = 29;
                } else daycount = 28;
            } else daycount = 28;
            showday = document.getElementById('day');
            selectday = showday.selectedIndex;
            showday.options.length = 0;
            showday.options.add(new Option('日', -1));
            for (var i = 1; i <= daycount; i++) {
                showday.options.add(new Option(i, i));
            }
            if (selectday > 0 & selectday <= daycount) {
                showday.value = selectday;
            }
        }
    }
}

function english(englishCheck) {
    var reg = /^[a-zA-Z ]+$/;
    if (englishCheck.match(reg)) {
        return true;
    } else {
        return false;
    }
}

function english_chinese(english_chinese) {
    var reg = /^[\u4e00-\u9fa5a-zA-Z ]+$/;
    if (english_chinese.match(reg)) {
        return true;
    } else {
        return false;
    }
}

function emailcheck(emailtoCheck) {
    var regExp = /^[^@^\s]+@[^\.@^\s]+(\.[^\.@^\s]+)+$/;
    if (emailtoCheck.match(regExp))
        return true;
    else
        return false;
}

function isChinese(temp) {
    var re = /[^\u4e00-\u9fa5]/;
    if (re.test(temp)) return false;
    return true;
}

function checkidcard(number) {
    var re = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
    if (re.test(number)) return false;
    return true;
}

function checkidcard_w(number) {
    var re = /[A-Za-z0-9]/;
    if (re.test(number)) return false;
    return true;
}


function send(form,send_type) {
    document.getElementById("error").innerHTML = "";
    document.getElementById("ch_error").innerHTML = "";

    // $("#email_address_font").css("color", "black");
    // $("#email_address_font").text("電郵地址");
    // $("#id_card_number_font").css("color", "black");
    // $("#id_card_number_font").text("身份證號碼");

    document.getElementById("id_card_number_font").color = 'black';
    document.getElementById("email_address_font").color = 'black';


    var font = document.getElementsByTagName('font');
    for (i = 0; i < font.length; i++) {
        font[i].color = 'black';
    }
    var complete = true;
    if ((document.getElementById("cn_f_name").value != "" && document.getElementById("cn_l_name").value == "") || (document.getElementById("cn_f_name").value == "" && document.getElementById("cn_l_name").value != "")) {
        document.getElementById('check_ch_time').value = 1;
        complete = false;
        if (document.getElementById("cn_f_name").value == "")
            document.getElementById("cn_f_name_font").color = 'red';
        if (document.getElementById("cn_l_name").value == "")
            document.getElementById("cn_l_name_font").color = 'red';
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (!$("#cn_f_name").attr("disabled")) {


        if ((document.getElementById("cn_f_name").value == "" && document.getElementById('check_ch_time').value == 0) || (document.getElementById("cn_l_name").value == "" && document.getElementById('check_ch_time').value == 0)) {
            document.getElementById('check_ch_time').value = 1;
            complete = false;
            if (document.getElementById("cn_f_name").value == "")
                document.getElementById("cn_f_name_font").color = 'red';
            if (document.getElementById("cn_l_name").value == "")
                document.getElementById("cn_l_name_font").color = 'red';
            document.getElementById("ch_error").innerHTML = "<font color='red'>您還沒填寫中文名字,如沒中文名字可再按下一步跳過</font><br>";
        }

    }


    if (!isChinese(document.getElementById("cn_f_name").value) || !isChinese(document.getElementById("cn_l_name").value)) {
        if (!isChinese(document.getElementById("cn_f_name").value)) {
            document.getElementById("cn_f_name_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
        if (!isChinese(document.getElementById("cn_l_name").value)) {
            document.getElementById("cn_l_name_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
    }

    if (document.getElementById("en_l_name").value == "" || !english(document.getElementById("en_l_name").value)) {
        document.getElementById("en_l_name_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTM += "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }
    if (document.getElementById("en_f_name").value == "" || !english(document.getElementById("en_f_name").value)) {
        document.getElementById("en_f_name_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }
    if (document.getElementById("id_type_id").value < 0) {
        document.getElementById("id_type_id_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }
    /*
       if (document.getElementById("id_type_id").value < 0) {
           document.getElementById("id_type_id_font").color = 'red';
           complete = false;
           document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
       } else if (document.getElementById("id_type_id").value == 39) {
           if (document.getElementById("country").value == "") {
               document.getElementById("country_font").color = 'red';
               complete = false;
               document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
           }
           if (document.getElementById("district").value == "") {
               document.getElementById("district_font").color = 'red';
               complete = false;
               document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
           }
       }

       if (checkidcard(document.getElementById('id_card_number').value) && document.getElementById('id_type_id').value != '39') {
           document.getElementById("id_card_number_font").color = 'red';
           complete = false;
           document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
       } else if (document.getElementById('id_type_id').value == '39') {
           if (isNaN(document.getElementById('id_card_number').value) || checkidcard_w(document.getElementById('id_card_number').value)) {
               document.getElementById("id_card_number_font").color = 'red';
               complete = false;
               document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
           }
       } else if (document.getElementById('id_card_number').value == "") {
           document.getElementById("id_card_number_font").color = 'red';
           complete = false;
           document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
       }
   */
    if (document.getElementById("id_type_id").value < 0) {
        document.getElementById("id_type_id_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為未填寫或格式錯誤</font><br>";
    }

    if (document.getElementById('id_type_id').value == '17') {
        if (!(document.getElementById('id_card_number').value) ||
            document.getElementById('id_card_number').value.length != 8||
            isNaN( document.getElementById('id_card_number').value) ) {
            document.getElementById("id_card_number_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為未填寫或格式錯誤</font><br>";
        }
    } else if (document.getElementById('id_card_number').value == "") {
        document.getElementById("id_card_number_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為未填寫或格式錯誤</font><br>";
    }

    if (document.getElementById("gender").value < 0) {
        document.getElementById("gender_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (document.getElementById("martial_status").value=="") {
        document.getElementById("martial_status_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    // if (document.getElementById("nationality").value == "") {
    //     document.getElementById("address_font").color = 'red';
    //     complete = false;
    //     document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    // }

    if (document.getElementById("address_1").value == "") {
        document.getElementById("address_font_1").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (document.getElementById("address_2").value == "") {
        document.getElementById("address_font_2").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (document.getElementById("address_3").value == "") {
        document.getElementById("address_font_3").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }



    // if (document.getElementById("account_0").value == -1) {
    //     document.getElementById("account_font").color = 'red';
    //     complete = false;
    //     document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    // }
    // if (document.getElementById("account_1").value == -1) {
    //     document.getElementById("account_font").color = 'red';
    //     complete = false;
    //     document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    // }
    // if (document.getElementById("account_2").value == -1) {
    //     document.getElementById("account_font").color = 'red';
    //     complete = false;
    //     document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    // }
    // if (document.getElementById("social").value < 0) {
    //     document.getElementById("social_font").color = 'red';
    //     complete = false;
    //     document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    // }

 /*   if (document.getElementById("day").value < '0') {
        document.getElementById("bir_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else if (document.getElementById("month").value < 0) {
        document.getElementById("bir_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else if (document.getElementById("year").value < 0) {
        document.getElementById("bir_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }*/
    if (!document.getElementById("dob").value) {
        document.getElementById("bir_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (document.getElementById("area_code").value < 0) {
        document.getElementById("area_code_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (!document.getElementById('mobile').value  || isNaN(document.getElementById('mobile').value) || (document.getElementById('mobile').value.length != 8 && (document.getElementById('area_code').value == '853' || document.getElementById('area_code').value == '852')) || (document.getElementById('mobile').value.length != 11 && document.getElementById('area_code').value == '86')) {
        document.getElementById("area_code_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (!emailcheck(document.getElementById('email_address').value)) {
        document.getElementById("email_address_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }
    if (document.getElementById('email_address').value!==document.getElementById('email_address_confirm').value) {
        document.getElementById("email_address_font").color = 'red';
        document.getElementById("email_address_confirm_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>兩次電郵地址不同</font><br>";
    }

    if (!document.getElementById('have_relative').value){
        document.getElementById("have_relative_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }
    else if (document.getElementById('have_relative').value=="yes"){
        if (document.getElementById("relative_name").value == "") {
            document.getElementById("relative_name_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }

        if (document.getElementById("relationship").value == "") {
            document.getElementById("relationship_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }

        if (!document.getElementById("department_name").value  ) {
            document.getElementById("department_code_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
        if (document.getElementById("inService").value <=0) {
            document.getElementById("in_service_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
    }







    if (send_type === 'onlyCheck') {
        return complete;
    }
    if (!complete){
        $('.t1').css('height','420px')
        if (send_type === 'dialog_saveAndGo') {
            let dialog = $('dialog')[0];
            dialog.close();
            alert("儲存失敗, 紅色部份為沒填寫或者格式錯誤");
        }
    }


    if (complete == true) {
        document.getElementById('type').value = 'profile';
        // alert($("#form-1").serialize())
    /*    $("#en_f_name").attr("disabled", false);
        $("#en_l_name").attr("disabled", false);
        $("#cn_f_name").attr("disabled", false);
        $("#cn_l_name").attr("disabled", false);
        $("#id_type_id").attr("disabled", false);
        $("#id_card_number").attr("disabled", false);
        $("#email_address").attr("disabled", false);*/
        var url = ROOT + "/applicant/top1";
        var data = $("#form-1").serialize();
        // console.log(data)
        $.ajax({
            "url": url,
            "type": "post",
            "data": data,
            "dataType": "json",
            "success": function (json) {
                if (json.state === 200) {
                   /* if (json.data !== null) {
                        setStatusToCookie(json.data.status);
                        setTokenToCookie(json.data.token);
                    }*/
                    if (send_type=='onlySave'){
                      /*  $("#en_f_name").attr("disabled", true);
                        $("#en_l_name").attr("disabled", true);
                        $("#cn_f_name").attr("disabled", true);
                        $("#cn_l_name").attr("disabled", true);
                        $("#id_type_id").attr("disabled", true);
                        $("#id_card_number").attr("disabled", true);
                        $("#email_address").attr("disabled", true);*/
                        ORIGINAL_FORM = $("#form-1").serialize();
                        alert("儲存成功")
                    }
                    else if (send_type=='dialog_saveAndGo'){
                        location.href = CURRENT_GOTO;
                    }
                    else {
                        location = ROOT + "/hr_top3.html";
                    }

                } else if (json.state === 400) {
                    document.getElementById("email_address_font").color = 'red';
                    $("#email_address_font").text("電郵地址"+json.message);
                    document.getElementById("id_card_number_font").color = 'red';
                    $("#id_card_number_font").text("身份證號碼"+json.message);
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為已注冊</font><br>";
                } else if (json.state === 401) {

                    document.getElementById("id_card_number_font").color = 'red';
                    $("#id_card_number_font").text("身份證號碼"+json.message);
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為已注冊</font><br>";
                } else if (json.state === 402) {
                    document.getElementById("email_address_font").color = 'red';
                    $("#email_address_font").text("電郵地址"+json.message);

                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為已注冊</font><br>";
                } else if (json.state === 611||json.state ===612) {
                    alert(json.message);
                    location = ROOT + "/hr_top_m1.html";

                }else {
                    if (json.state !== 999)
                        alert(json.message);
                }
            },
            // error: function (data) {
            //     console.log(data);
            // },
        });


        // form.submit();
    }
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


/*function showText() {
    if (document.getElementById('id_type_id').value == "39") {

        document.getElementById('textValue').style.display = 'block';
    } else {
        document.getElementById('textValue').style.display = 'none';
    }
}*/



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


/*    if (window.localStorage.getItem("status") === "Y") {
        $("#HR_bar").attr("src", "images/HR_bar_a8.jpg");
    } else {
        $.get(ROOT + "/applicant/pageNo", function (json) {
            if (json != null && json != "null") {
                $("#HR_bar").attr("src", "images/HR_bar_a" + json + ".jpg");
            }
        });
    }*/

  /*  $('#id_type_id').on(
        {
            change: function () {
                showText();
            }
        }
    );*/
   /* $('#month').on(
        {
            change: function () {
                showdate(this.value);
            }
        }
    );
    $('#year').on(
        {
            change: function () {
                showdate(this.value);
            }
        }
    );*/
    $('#onlySave').on(
        {
            click: function () {
                send(this.form,'onlySave');
            }
        }
    );
    $('[name="Submit"]').on(
        {
            click: function () {
                send(this.form,'saveAndNext');
            }
        }
    );
    $('#have_relative').on(
        {
            change: function () {
                if (this.value == 'yes') {
                    $('#table_relative').css('display', '');
                    // let content = document.getElementById('div_scroll'); //子级
                    $('#div_scroll').scrollTop(1000)
                } else {
                    $('#table_relative').css('display', 'none');
                }
            }
        }
    );
    $('#modify').on(
        {
            click: function () {
                modify_password();
            }
        }
    )
    document.getElementById('email_address').oncut=function (){
        return false;
    }
    document.getElementById('email_address').oncopy=function (){
        return false;
    }
    document.getElementById('email_address_confirm').oncut=function (){
        return false;
    }
    document.getElementById('email_address_confirm').oncopy=function (){
        return false;
    }
    document.getElementById('email_address_confirm').onpaste=function (){
        return false;
    }

  /*  set_year();
    set_month();
    set_day();*/


    if ($("#have_relative").find("option[value='yes']").attr("selected"))  {
        $('#table_relative').css('display', '');
    }


    let token = $('#token').val();
    if (!!token) {
        setTokenToCookie(token)
    }

});


function set_year() {
    let date = new Date;
    let year = date.getFullYear();
    for (let i = 18; i < 65; i++) {
        let tmp = year - i;
        $("#year").append("<option value='" + tmp + "'>" + tmp + "</option>")
    }

}
function set_month() {
    for (let i = 1; i <= 12; i++) {
        $("#month").append("<option value='" + i + "'>" + i + "</option>")
    }

}

function set_day() {
    for (let i = 1; i <= 31; i++) {
        $("#day").append("<option value='" + i + "'>" + i + "</option>")
    }
}

function getData1() {
    let url = ROOT + "/applicant/t1/data1";
    $.ajax({
        "url": url,
        "type": "get",
        "dataType": "json",
        "success": function (json) {
            if (json.state !== 200) {
                return;
            }

            getData2();


            let info = json.data;


            Object.keys(info).map(function (key) {
                $('#form-1 :input').filter(function () {
                    return key == this.name;
                }).val(info[key]);
            });

            // var arr = info.account.split("-");
            // var temp = ["省內", "市內", "城市"];
            // for (var i = 0; i <= 2; i++) {
            //     $("#account_" + i).get(0).selectedIndex = arr[i] === temp[i] ? 1 : 2;
            // }


       /*     var time_arr = info.dob.split("-");

            $("#year").val(parseInt(time_arr[0]));
            $("#month").val(parseInt(time_arr[1]));
            $("#day").val(parseInt(time_arr[2]));*/


            var html = "<input type=\"button\" name=\"modify\" id=\"modify\"  value=\"修改密碼\">";
            $("#td-1").append(html);
            $('#modify').on(
                {
                    click: function () {
                        modify_password();
                    }
                }
            )


            $("#en_f_name").attr("disabled", true);
            $("#en_l_name").attr("disabled", true);
            $("#cn_f_name").attr("disabled", true);
            $("#cn_l_name").attr("disabled", true);
            $("#id_type_id").attr("disabled", true);
            $("#id_card_number").attr("disabled", true);
            $("#email_address").attr("disabled", true);
           /* showdate($("#year").val());
            showdate($("#month").val());*/


        }
    });
}


function getData2() {
    var url = ROOT + "/applicant/t1/data2";
    $.ajax({
        "url": url,
        "type": "get",
        "dataType": "json",
        "success": function (json) {

            var info = json.data;
            if (info == null || info == "null") {
                // $("#have_relative_no").prop("checked", true);
                $("#have_relative").find("option[value='no']").attr("selected",true);
                return;
            }

            // $("#have_relative_yes").prop("checked", true);
            $("#have_relative").find("option[value='yes']").attr("selected",true);
            $('#table_relative').css('display', '');


            Object.keys(info).map(function (key) {
                $('#form-1 :input').filter(function () {
                    return key == this.name;
                }).val(info[key]);
            });
        }
    });
}


function modify_password() {
    location = ROOT + "/hr_top_m3.html";
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

function set_department_code_font() {
    var url = ROOT + "/deptDesc";
    $.ajax({
        "url": url,
        "type": "get",
        "async": false,
        "dataType": "json",
        "success": function (json) {
            if (json.state !== 200) {
                // alert(json.message);
                return;
            }
            var list = json.data;
            var html = "";
            for (var i = 0; i < list.length; i++) {
                html += "<option value='#{id}'>#{deptDesc}</option>";
                html = html.replace(/#{id}/g, list[i].departmentCode);
                html = html.replace(/#{deptDesc}/g, list[i].description);
            }
            $("#department_code").append(html);
        }
    });
}



$('.self_date_picker').click(function(){
    $('.self_date_picker').data('daterangepicker').autoUpdateInput=true
})
$('.self_date_picker').daterangepicker({
    // timePicker: true,        // 选择时间
    opens: 'right',
    autoApply: false,
    singleDatePicker: true,  // 只选一个
    autoUpdateInput: false,
    // buttonClasses: ['button'],
    applyClass: 'button',
    cancelClass: 'button',
    locale: {
        format: 'YYYY-MM-DD',
        applyLabel: '確定',
        cancelLabel: '取消',
        daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
        monthNames: ['1月', '2月', '3月', '4月', '5月', '6月',
            '7月', '8月', '9月', '10月', '11月', '12月'
        ],
        // firstDay: 1
    },
    showDropdowns: true,
    // minYear: currentYear - 65 + 1,
    // maxYear: currentYear - 18 + 1
    // startDate: moment().hours(0).minutes(0).seconds(0), //设置开始日期
    // maxDate: moment(new Date()), //设置最大日期
    // startDate: moment({year:currentYear -18}), //设置开始日期
    minDate: moment().subtract('years',65),
    maxDate: moment().subtract('years',18),
    startDate: $('#dob').val() || moment().subtract('years',18),

})
