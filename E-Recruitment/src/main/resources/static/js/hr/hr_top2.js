// var responseRecord=new Array();

function sendRequestForJobTitle(department_code, select_object) {

    var len = job_list.length;
    if (len == 0) {
        return;
    }
    for (var i = 0; i < len; i++) {
        if (department_code == job_list[i].key) {
            $("#" + select_object).html("<option value='-1' selected>請選擇</option>");
            $("#" + select_object).append(job_list[i].value);
        }
        if (department_code == -1) {
            $("#" + select_object).html("<option value='-1' selected>請選擇</option>");
        }
    }

}

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

function check(form,send_type) {
    var complete = true;
    document.getElementById("error").innerHTML = "";
    document.getElementById("c_error").innerHTML = "";
    var font = document.getElementsByTagName('font');
    for (i = 0; i < font.length; i++) {
        font[i].color = 'black';
    }

    /* if (document.getElementById('title1').value < 0 || document.getElementById('department_select1').value < 0) {
         if (document.getElementById('title1').value < 0)
             document.getElementById("title_font").color = 'red';
         if (document.getElementById('department_select1').value < 0)
             document.getElementById("department_select_font").color = 'red';
         complete = false;
         document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
     }

     if (document.getElementById('title2').value < 0 && document.getElementById('department_select2').value > 0) {
         document.getElementById("title2_font").color = 'red';
         complete = false;
         document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
     }
     if (document.getElementById('title3').value < 0 && document.getElementById('department_select3').value > 0) {
         document.getElementById("title3_font").color = 'red';
         complete = false;
         document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
     }*/

    /*if (document.getElementById('title1').value < 0) {
        document.getElementById("department_select_font1").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }*/
    for (i = 2; i < JOB_ID_NUM; i++) {
        // console.log(i)
        if (document.getElementById('title' + i) && document.getElementById('title' + i).value < 0) {
            document.getElementById("department_select_font" + i).color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
    }
    expected_salary = document.getElementById('expected_salary').value;
    expected_salary = expected_salary.replace(/,/g, "");

    if (expected_salary == "" || isNaN(expected_salary)) {
        document.getElementById("expected_salary_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else document.getElementById('expected_salary').value = expected_salary;

   /* if (document.getElementById("day").value < 0 || document.getElementById("month").value < 0 || document.getElementById("year").value < 0) {
        document.getElementById("day_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else {
        var myDate = new Date();
        var newDay = myDate.getDate();
        var newMonth = 1 + myDate.getMonth();
        var newYear = myDate.getFullYear();
        if (document.getElementById("year").value <= newYear) {
            if (document.getElementById("month").value < newMonth) {
                document.getElementById("day_font").color = 'red';
                complete = false;
                document.getElementById("c_error").innerHTML = "<font color='red'>不能為過去時間</font><br>";
            } else if (document.getElementById("month").value == newMonth && document.getElementById("day").value < newDay) {
                document.getElementById("day_font").color = 'red';
                complete = false;
                document.getElementById("c_error").innerHTML = "<font color='red'>不能為過去時間</font><br>";
            }
        }
    }

    if (document.getElementById("notice_day").value == "" || isNaN(document.getElementById("notice_day").value)) {
        document.getElementById("notice_day_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }*/

    let noticeDayNum = document.getElementById("notice_day").value;
    if (!!noticeDayNum && !/^\d+$/.test(noticeDayNum)) {
        document.getElementById("notice_day_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    if (!document.getElementById("source_id").value||document.getElementById("source_id").value < 0) {
        document.getElementById("source_id_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }
  /*  if (document.getElementById("notice_day_type").value == "") {
        document.getElementById("notice_day_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }*/
    if (document.getElementById("expected_salary_type").value == "") {
        document.getElementById("expected_salary_font").color = 'red';
        complete = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }

    let v = getSource_id_select();
    if (v === "其他" ||
        v === "其它") {

        if (!document.getElementById("source_ref").value) {
            document.getElementById("source_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }


    }
    if (v === "招聘網站" ||
        v === "招聘展" ||
        v === "報章") {
        if (!document.getElementById("applicantSourceDetailList").value) {
            document.getElementById("source_font").color = 'red';
            complete = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
    }

    if (document.getElementById('needShift').value == 1) {
        let font = 'onShift_font';
        let $select = $("select[name='dayofweek']");
        document.getElementById('onShift_total').value = $select.length;

        for (i = 1; i <= $select.length; i++) {

            // console.log(i)


            let from_hour = parseInt(document.getElementById('from_hour' +ONSHFIT_ID_NUM_arr [i-1]).value)
            let from_min = parseInt(document.getElementById('from_min' + ONSHFIT_ID_NUM_arr [i-1]).value)
            let to_hour = parseInt(document.getElementById('to_hour' + ONSHFIT_ID_NUM_arr [i-1]).value)
            let to_min = parseInt(document.getElementById('to_min' + ONSHFIT_ID_NUM_arr [i-1]).value)


            if (isNaN(from_hour) ||
                isNaN(from_min) ||
                isNaN(to_hour) ||
                isNaN(to_min) ||
                from_hour < 0 ||
                from_min < 0 ||
                to_hour < 0 ||
                to_min < 0 ||
                !document.getElementById('dayofweek' +  ONSHFIT_ID_NUM_arr [i-1]).value
            ) {
                complete = false;
                document.getElementById(font).color = 'red';
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                break;
            }

            if (from_hour > to_hour) {
                complete = false;
                document.getElementById(font).color = 'red';
                document.getElementById("error").innerHTML = "<font color='red'>開始時間須小於結束時間</font><br>";
                break;
            } else if (from_hour == to_hour && from_min >= to_min) {
                complete = false;
                document.getElementById(font).color = 'red';
                document.getElementById("error").innerHTML = "<font color='red'>開始時間須小於結束時間</font><br>";
                break;
            }
        }
    }
    if (!complete){
        $('.t1').css('height','420px')
    }
    if (complete == true) {
        document.getElementById('type').value = 'position';

        // $("#department_select1").attr("disabled", false);
        // $("#title1").attr("disabled", false);
        // $("#department_select2").attr("disabled", false);
        // $("#title2").attr("disabled", false);
        // $("#department_select3").attr("disabled", false);
        // $("#title3").attr("disabled", false);

        var url = ROOT + "/applicant/top2";
        var data = $("#form-1").serialize();

        // alert(data)
        // return;

        $.ajax({
            "url": url,
            "type": "post",
            "data": data,
            "dataType": "json",
            "success": function (json) {
                if (json.state === 200) {
                    if (send_type=='onlySave'){
                        alert("申請成功")
                    }else {
                        alert("申請成功")
                        location = ROOT + "/hr_post_list.html";
                    }
                } else if (json.state === 406) {
                    alert(json.message);
                    location = ROOT + "/hr_post_list.html";
                } else if (json.state !== 999 && json.state !== 622) {
                    alert(json.message);
                }
            }
            ,
            "error": function (xhr) {
                // if (xhr.status === 200) {
                alert("登錄過期,請重新登錄")
                location = ROOT + "/hr_top_m1.html";
                // }
            }
        });

    }


}


function getSource_id_select() {
    let myselect = document.getElementById("source_id");

    let selectedOption = myselect.options[myselect.selectedIndex];
    if (!selectedOption)return null;
    return String(selectedOption.text).trim();
}

function showExSourceInput(dbValue1, dbValue2, dbValue3) {

    if (!dbValue1) dbValue1 = "";
    if (!dbValue2) dbValue2 = "";
    if (!dbValue3) dbValue3 = "";


    let v = getSource_id_select();
    $('#source_ref_div').empty();
    if (v === "招聘網站" ||
        v === "招聘展" ||
        v === "報章") {
        /*  $('#source_ref_div').append('<strong>\n' +
              '<font id="source_font">＊' + '該 ' + v + ' 是' +
              '</font>\n' +
              '</strong><br><input width="50px" type="text" id="source_ref"  name="sourceRef" value="' + dbValue1 +
              '"><br>')*/
       /* $('#source_ref_div').append(
            '<font class="info_title" id="source_font">＊' + '該 ' + v + ' 是' +
            '</font>\n' +
            '<input width="50px" type="text" id="source_ref"  name="sourceRef" value="' + dbValue1 +
            '"><br>')*/

        let html = '<font class="info_title" id="source_font">＊' + '該 ' + v + ' 是' +
            '</font>\n' +
            '<select id="applicantSourceDetailList" name="applicantSourceDetailId" class="self_select_top2_middle"></select>'

        /*     '<input width="50px" type="text" id="source_ref"  name="sourceRef" value="' + dbValue1 +
             '"><br>';*/
        $('#source_ref_div').append(html);

        let myselect = document.getElementById("source_id").value;

        var url =ROOT+ "/applicantSourceDetailList";
        $.ajax({
            "url": url,
            "type": "get",
            "async": false,
            "dataType": "json",
            "data": {'messageId': myselect},
            "success": function (json) {
                if (json.state !== 200) {
                    // alert(json.message);
                    return;
                }
                var list = json.data;
                var html = "<option value=''>請選擇</option>";
                for (var i = 0; i < list.length; i++) {
                    html += "<option value='#{id}'>#{chnDesc}</option>";
                    html = html.replace(/#{id}/g, list[i].id);
                    html = html.replace(/#{chnDesc}/g, list[i].chnDesc);
                }
                $("#applicantSourceDetailList").append(html);
            }
        });
    } else if (
        v === "其他" ||
        v === "其它"
    ) {
        /*   $('#source_ref_div').append('<strong>\n' +
               '<font id="source_font">＊' + v + '出處' +
               '</font>\n' +
               '</strong><br><input width="50px" type="text" id="source_ref"  name="sourceRef" value="' + dbValue1 +
               '"><br>')*/
        $('#source_ref_div').append(
            '<font class="info_title" id="source_font">＊' + v + '出處' +
            '</font>\n' +
            '<input width="50px" type="text" id="source_ref"  name="sourceRef" value="' + dbValue1 +
            '"><br>')
    } else if (
        v === "員工介紹"
    ) {
        /*   $('#source_ref_div').append('<strong>\n' +
               '<font id="source_font1">&nbsp;&nbsp;&nbsp;該員工的姓名</font>\n' +
               '</strong><br><input type="text" name="sourceRef1" value="' + dbValue1 +
               '"><br>\n' +
               '<strong>\n' +
               '<font id="source_font2">&nbsp;&nbsp;&nbsp;該員工的職位</font>\n' +
               '</strong><br><input type="text" name="sourceRef2" value="' + dbValue2 +
               '"><br>\n' +
               '<strong>\n' +
               '<font id="source_font3">&nbsp;&nbsp;&nbsp;該員工的部門</font>\n' +
               '</strong><br><input type="text" name="sourceRef3" value="' + dbValue3 +
               '">')*/
        $('#source_ref_div').append(
            '<font class="info_title" id="source_font1">該員工的姓名</font>\n' +
            '<input type="text" name="sourceRef1" value="' + dbValue1 +
            '"><br>\n' +

            '<font class="info_title" id="source_font2">該員工的職位</font>\n' +
            '<input type="text" name="sourceRef2" value="' + dbValue2 +
            '"><br>\n' +

            '<font class="info_title" id="source_font3">該員工的部門</font>\n' +
            '<input type="text" name="sourceRef3" value="' + dbValue3 +
            '">')
    }
}

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


    /*if (window.localStorage.getItem("status") === "Y") {
        $("#HR_bar").attr("src", "images/HR_bar_a8.jpg");
    } else {
        $.get(ROOT + "/applicant/pageNo", function (json) {
            if (json != null && json != "null") {
                $("#HR_bar").attr("src", "images/HR_bar_a" + json + ".jpg");
            }
        });
    }*/


    $('#department_select1').on(
        {
            change: function () {
                sendRequestForJobTitle(this.value, 'title1');
            }
        }
    );

    $('#department_select2').on(
        {
            change: function () {
                sendRequestForJobTitle(this.value, 'title2');
            }
        }
    );
    $('#department_select3').on(
        {
            change: function () {
                sendRequestForJobTitle(this.value, 'title3');
            }
        }
    );

    $('#source_id').on(
        {
            change: function () {
                showExSourceInput();
            }
        }
    );


    $('#month').on(
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
    );
    $('#onlySave').on(
        {
            click: function () {
                check(this.form,'onlySave');
            }
        }
    );
    $('[name="Submit"]').on(
        {
            click: function () {
                check(this.form,'saveAndNext');
            }
        }
    );
    $('[name="Submit2"]').on(
        {
            click: function () {
                window.location = ROOT + '/hr_top1.html';
            }
        }
    );
    $('#btn_add_applicant_job').on(
        {
            click: function () {
                addSelectJob();
            }
        }
    );
    $('#btn_add_onShift').on(
        {
            click: function () {
                let count = $("select[name='dayofweek']").length;
                if (count >= 10) return;
                addSelectOnShift();
                count++;
                document.getElementById('onShift_total').value = count;

            }
        }
    );
    $('#title1').on(
        {
            change: function () {
                // alert('ch!')
                let jQuery_val = $(this).find('option:selected').data('needshift');

                if (jQuery_val > 0) {
                    // console.log('needShift')
                    $(this).parent().append('<span style="font-size: 2px;color: red">此職位需輪班,請填寫下方可輪班時間</span>')
                    document.getElementById('needShift').value = 1;
                    document.getElementById('part_onShift_table').style.display = 'block';
                    document.getElementById('btn_onShift_table').style.display = 'block';
                } else {
                    $(this).parent().find('span').remove()

                    check_all_selected_needShift();
                }
            }
        }
    );
    // dayofweek
    $('#dayofweek1').on(
        {
            change: function () {
                // alert()

            }
        }
    );

    set_year();
    // showJobList();
    // setOnChooseJobList(1)
    set_source_id_font();
    // getData1();


    $("#to_hour1").change(function(){
    	if($(this).val()=='24'){
    		$("#to_min1").val('0');
    		$("#to_min1").prop('disabled',true);
    	}
    	else{
    		$("#to_min1").prop('disabled',false);
    	}
    })
});
let ARR_DAYOFWEEK = [
    '<option value="every day">每日</option>',
    '<option value="f1t5">週一至五</option>',
    '<option value="6 and 7">週六日</option>',
    '<option value="1">週一</option>',
    '<option value="2">週二</option>',
    '<option value="3">週三</option>',
    '<option value="4">週四</option>',
    '<option value="5">週五</option>',
    '<option value="6">週六</option>',
    '<option value="7">周日</option>',
]

var ONSHFIT_ID_NUM = 2;
// 查找指定的元素在数组中的位置
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) {
            return i;
        }
    }
    return -1;
};
// 通过索引删除数组元素
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
var ONSHFIT_ID_NUM_arr = [];
ONSHFIT_ID_NUM_arr.push('1');

function addSelectOnShift() {
    // let num = document.getElementById('num1').value;
    // if (num == "")
    //     num = 2;

// alert(ARR_DAYOFWEEK.join())
    $("#add_part_Select_onShift").append(' <tr class="word" id="show1">\n' +
        '                                        <td height="30">&nbsp;</td>\n' +
        '                                        <td width="142">\n' +
        // '                                            <font class="info_title">＊請提供可輪班之時間<font></td>\n' +
        '\n' +
        '\n' +
        '                                        <td>\n' +
        '                                            <select class="info_select_small" name="dayofweek" id="dayofweek' + ONSHFIT_ID_NUM +
        '">\n' +
        '                                                <option value="">請選擇</option>\n' +
        ARR_DAYOFWEEK.join() +

        '                                            </select>\n' +
        '                                        </td>\n' +
        '\n' +
        '                                        <td>\n' +
        '                                            &emsp;&emsp;由\n' +
        '                                        </td>\n' +
        '                                        <td>\n' +
        '                                            <select class="info_select_small" id="from_hour' + ONSHFIT_ID_NUM +
        '"\n' +
        '                                                    name="from_hour[]">\n' +
        '                                                <option value="-1" selected disabled style="display: none">時</option>\n' +
        '                                                <option value="0">00</option>\n' +
        '                                                <option value="1">01</option>\n' +
        '                                                <option value="2">02</option>\n' +
        '                                                <option value="3">03</option>\n' +
        '                                                <option value="4">04</option>\n' +
        '                                                <option value="5">05</option>\n' +
        '                                                <option value="6">06</option>\n' +
        '                                                <option value="7">07</option>\n' +
        '                                                <option value="8">08</option>\n' +
        '                                                <option value="9">09</option>\n' +
        '                                                <option value="10">10</option>\n' +
        '                                                <option value="11">11</option>\n' +
        '                                                <option value="12">12</option>\n' +
        '                                                <option value="13">13</option>\n' +
        '                                                <option value="14">14</option>\n' +
        '                                                <option value="15">15</option>\n' +
        '                                                <option value="16">16</option>\n' +
        '                                                <option value="17">17</option>\n' +
        '                                                <option value="18">18</option>\n' +
        '                                                <option value="19">19</option>\n' +
        '                                                <option value="20">20</option>\n' +
        '                                                <option value="21">21</option>\n' +
        '                                                <option value="22">22</option>\n' +
        '                                                <option value="23">23</option>\n' +
        '                                            </select>\n' +
        '                                        </td>\n' +
        '                                        <td>\n' +
        '                                            <select class="info_select_small" id="from_min' + ONSHFIT_ID_NUM +
        '"\n' +
        '                                                    name="from_min[]">\n' +
        '                                                <option value="-1" selected disabled style="display: none">分</option>\n' +
        '\n' +
        '                                                <option value="0">00</option>\n' +
        '                                                <option value="30">30</option>\n' +
        '                                            </select>\n' +
        '                                        </td>\n' +
        '                                        <td>\n' +
        '                                            &emsp;&emsp;至\n' +
        '                                        </td>\n' +
        '                                        <td>\n' +
        '                                            <select class="info_select_small" id="to_hour' + ONSHFIT_ID_NUM +
        '"\n' +
        '                                                    name="to_hour[]">\n' +
        '                                                <option value="-1" selected disabled style="display: none">時</option>\n' +
        '\n' +
        '                                                <option value="0">00</option>\n' +
        '                                                <option value="1">01</option>\n' +
        '                                                <option value="2">02</option>\n' +
        '                                                <option value="3">03</option>\n' +
        '                                                <option value="4">04</option>\n' +
        '                                                <option value="5">05</option>\n' +
        '                                                <option value="6">06</option>\n' +
        '                                                <option value="7">07</option>\n' +
        '                                                <option value="8">08</option>\n' +
        '                                                <option value="9">09</option>\n' +
        '                                                <option value="10">10</option>\n' +
        '                                                <option value="11">11</option>\n' +
        '                                                <option value="12">12</option>\n' +
        '                                                <option value="13">13</option>\n' +
        '                                                <option value="14">14</option>\n' +
        '                                                <option value="15">15</option>\n' +
        '                                                <option value="16">16</option>\n' +
        '                                                <option value="17">17</option>\n' +
        '                                                <option value="18">18</option>\n' +
        '                                                <option value="19">19</option>\n' +
        '                                                <option value="20">20</option>\n' +
        '                                                <option value="21">21</option>\n' +
        '                                                <option value="22">22</option>\n' +
        '                                                <option value="23">23</option>\n' +
        '                                            </select></td>\n' +
        '                                        <td>\n' +
        '                                            <select class="info_select_small" id="to_min' + ONSHFIT_ID_NUM +
        '"\n' +
        '                                                    name="to_min[]">\n' +
        '                                                <option value="-1" selected disabled style="display: none">分</option>\n' +
        '\n' +
        '                                                <option value="0">00</option>\n' +
        '                                                <option value="30">30</option>\n' +
        '                                            </select>\n' +
        '                                        </td>\n' +
        '<td><div id="btn_delete_onShift_' + ONSHFIT_ID_NUM +
        '" class="page_title_small"\n' +
        '                                               style="cursor: pointer;width: 50px">－刪除</div> </td>' +
        '                                    </tr>');
    $('#btn_delete_onShift_' + ONSHFIT_ID_NUM).on(
        {
            click: function () {

                $(this).parent().parent().remove();
                document.getElementById('onShift_total').value = $("select[name='dayofweek']").length;
                ONSHFIT_ID_NUM_arr.remove(String($(this).attr('id').replace(/\D/g, "")))

            }
        }
    );
    // dayofweek
    $('#dayofweek' + ONSHFIT_ID_NUM).on(
        {
            change: function () {
                // alert()

            }
        }
    );


    // setOnChooseJobList(ONSHFIT_ID_NUM);

    ONSHFIT_ID_NUM_arr.push(String(ONSHFIT_ID_NUM));

    ONSHFIT_ID_NUM++;
    // COUNT_NAME++;
    return ONSHFIT_ID_NUM;
}

function setOnChooseJobList(num) {
    let $1 = $("#title" + num);
    $.each(job_list, function (index, val) {
        $1.append('<option data-needshift="' + val.needShift +
            '" value="' + val.deptPosDetailId +
            '">' + val.jobCode + ' ' + val.description + ' ' + val.jobTitle +
            '</option>');

    });
}

function setHistoryChooseJobList(num, val) {
    let count = 0;
    let $1 = $("#title" + num);
    $.each(job_list, function (index, val0) {
        if (val0.deptPosDetailId == val.deptPosDetailId && val0.departmentCode == val.departmentCode) {
            count++;
            $1.find("option[value='" + val.deptPosDetailId +
                "']").attr("selected", true);
            return;
        }
    });

    if (count > 0) {
        return;
    }


    $1.append('<option  data-needshift="' + val.needShift +
        '"  value="' + val.deptPosDetailId +
        '"' +
        ' selected>' + val.jobCode + ' ' + val.description + ' ' + val.jobTitle +
        '</option>');
    // $1.parent().append('<span style="font-size: 2px;color: red">此職位需輪班,請填寫下方可輪班時間</span>')

}

var JOB_ID_NUM = 2;

function check_all_selected_needShift() {
    let count = 0;
    $("select[id^='title']").each(function () {
        if ($(this).find('option:selected').data('needshift') > 0) {
            count++;
        }
    })
    if (count === 0) {
        document.getElementById('needShift').value = 0;
        document.getElementById('part_onShift_table').style.display = 'none';
        document.getElementById('btn_onShift_table').style.display = 'none';
    }
}

function set_all_selected_needShift_red() {
    // console.log('111')
    $("select[id^='title']").each(function () {
        if ($(this).find('option:selected').data('needshift') > 0) {
            $(this).parent().append('<span style="font-size: 2px;color: red">此職位需輪班,請填寫下方可輪班時間</span>')
        }
    })

}

function addSelectJob() {
    // let num = document.getElementById('num1').value;
    // if (num == "")
    //     num = 2;
    $("#add_part_SelectJob").append('<tr id="add_tr_' + JOB_ID_NUM +
        '">\n' +
        '                                            <td>&nbsp;</td>\n' +
        '                                            <td height="50">\n' +
        '                                                <font class="info_title count_name" id="department_select_font' + JOB_ID_NUM +
        '">' +
        '</font>\n' +
        '                                                <select class="info_select_long_1" id="title' + JOB_ID_NUM +
        '" name="title">\n' +
        '                                                    <option value="-1">請選擇</option>\n' +
        '                                                </select></td>\n' +
        '<td><div id="btn_delete_applicant_job_' + JOB_ID_NUM +
        '" class="page_title_small"\n' +
        '                                               style="cursor: pointer;width: 50px">－刪除</div> </td>' +
        '                                        </tr>');
    $('#btn_delete_applicant_job_' + JOB_ID_NUM).on(
        {
            click: function () {

                $(this).parent().parent().remove();
                // console.log(COUNT_NAME)
                $(".count_name").each(function (index, element) {
                    $(this).text('第 ' + (index + 2) +
                        ' 選擇')
                });
                // COUNT_NAME--;
                // num--;
                check_all_selected_needShift();
            }
        }
    );
    $('#title' + JOB_ID_NUM).on(
        {
            change: function () {
                // alert('ch!')
                let jQuery_val = $(this).find('option:selected').data('needshift');

                if (jQuery_val > 0) {
                    // console.log('needShift')
                    $(this).parent().append('<span style="font-size: 2px;color: red">此職位需輪班,請填寫下方可輪班時間</span>')
                    document.getElementById('needShift').value = 1;
                    document.getElementById('part_onShift_table').style.display = 'block';
                    document.getElementById('btn_onShift_table').style.display = 'block';
                } else {
                    $(this).parent().find('span').remove()

                    check_all_selected_needShift();
                }
            }
        }
    );


    $(".count_name").each(function (index, element) {
        $(this).text('第 ' + (index + 2) +
            ' 選擇')
    });
    setOnChooseJobList(JOB_ID_NUM);


    JOB_ID_NUM++;
    // document.getElementById('num1').value = num;
    // COUNT_NAME++;
    return JOB_ID_NUM;
}

function set_year() {
    var date = new Date;
    var year = date.getFullYear();
    for (var i = 0; i < 2; i++) {
        var tmp = year + i;
        $("#year").append("<option value='" + tmp + "'>" + tmp + "</option>")
    }
}


function getData1() {
    var url = ROOT + "/applicant/t1/data1";
    $.ajax({
        "url": url,
        "type": "get",
        "dataType": "json",
        "success": function (json) {
            if (json.state !== 200) {
                return;
            }


            var info = json.data;
            if (info.incompletePageNo <= 2) {
                return;
            }


            Object.keys(info).map(function (key) {
                $('#form-1 :input').filter(function () {
                    return key == this.name;
                }).val(info[key]);
            });


            var time_arr = info.availableWorkDate.split("-");
            $("#year").val(parseInt(time_arr[0]));
            $("#month").val(parseInt(time_arr[1]));
            $("#day").val(parseInt(time_arr[2]));

            let sourceRef = info.sourceRef;
            if (sourceRef) {
                var sr = sourceRef.split("||");
                if (sr.length === 3) {
                    showExSourceInput(sr[0], sr[1], sr[2]);
                } else {
                    showExSourceInput(sourceRef);
                }
            } else {
                showExSourceInput();
            }


            $.get(ROOT + "/applicant/t2/data", function (json) {
                if (json.state !== 200) {
                    return;
                }
                let info2 = json.data;
                if (info2.length == 0) {
                    return;
                }

                for (let i = 0; i < info2.length; i++) {
                    // console.log(JSON.stringify(info2[i]))
                    if (info2[i].needShift > 0) {
                        // console.log('needShift')
                        document.getElementById('needShift').value = 1;
                        document.getElementById('part_onShift_table').style.display = 'block';
                        document.getElementById('btn_onShift_table').style.display = 'block';

                        break;
                    }
                }

                for (let i = 0; i < info2.length; i++) {

                    if (i === 0) {
                        setHistoryChooseJobList(1, info2[i]);
                    } else {
                        setHistoryChooseJobList(addSelectJob() - 1, info2[i]);
                    }
                }

                set_all_selected_needShift_red();
            });

            $.get(ROOT + "/applicant/t6/data", function (json) {
                if (json.state !== 200) {
                    return;
                }
                let info3 = json.data;
                if (info3.length == 0) {
                    return;
                }


                // for (var i = 1; i <= 7; i++) {
                for (let i = 1; i <= info3.length; i++) {
                    if (i > 1) {
                        addSelectOnShift();
                    }

                    let arr = info3[i - 1].fromDate.split(":");
                    $("#from_hour" + i).val(arr[0]);
                    $("#from_min" + i).val(arr[1]);
                    let arr2 = info3[i - 1].toDate.split(":");
                    $("#to_hour" + i).val(arr2[0]);
                    $("#to_min" + i).val(arr2[1]);

                    $("#dayofweek" + i).val(info3[i - 1].dayofweek);


                }

            });


            showdate($("#year").val());
            showdate($("#month").val());

        }
    });
}


function set_source_id_font() {
    var url = ROOT + "/msgDescSource";
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
            $("#source_id").append(html);
        }
    });
}

var job_list = [];


function showJobList() {
    var url = ROOT + "/positionDetail";
    $.ajax({
        "url": url,
        "async": false,
        "type": "get",
        "dataType": "json",
        "success": function (json) {

            if (json.state !== 200) {
                return;
            }
            job_list = json.data;
            // console.log('job_list--' + JSON.stringify(job_list))
        }
    });
}


$('.self_date_picker').click(function(){
    $('.self_date_picker').data('daterangepicker').autoUpdateInput=true
})
let currentYear = parseInt(moment().format('YYYY'), 10)+1;
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
    minDate: moment(),
    maxDate: moment(currentYear+"-12-31"),
    startDate: $('#dob').val() || moment(),

})
