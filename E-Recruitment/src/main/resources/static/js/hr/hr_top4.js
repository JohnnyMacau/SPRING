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

    /*  $('#have_experience_yes').on(
          {
              click: function () {
                  add();
              }
          }
      );


      $('#have_experience_no').on(
          {
              click: function () {
                  delete_all();
              }
          }
      );*/


    $('#imgGZJY').on(
        {
            click: function () {
                add_working_experience();
                // document.getElementById('have_experience_yes').checked = true;
            }
        }
    );


    /* $('#employed_before_yes').on(
         {
             click: function () {
                 add_html(true, 'eb');
             }
         }
     );

     $('#employed_before_no').on(
         {
             click: function () {
                 add_html(false, 'eb');
             }
         }
     );*/


    /*    $('#terminated_before_yes').on(
            {
                click: function () {
                    add_html(true, 'tbc');
                }
            }
        );

        $('#terminated_before_no').on(
            {
                click: function () {
                    add_html(false, 'tbc');
                }
            }
        );

        $('#criminal_record_yes').on(
            {
                click: function () {
                    add_html(true, 'crc');
                }
            }
        );

        $('#criminal_record_no').on(
            {
                click: function () {
                    add_html(false, 'crc');
                }
            }
        );*/


    $('#employed_before').on(
        {
            change: function () {
                // alert(this.value)
                if (this.value == 'Y') {
                    add_html(true, 'eb');
                } else {
                    add_html(false, 'eb');
                }
            }
        }
    );
    $('#terminated_before').on(
        {
            change: function () {
                if (this.value == 'Y') {
                    add_html(true, 'tbc');
                } else {
                    add_html(false, 'tbc');
                }
            }
        }
    );
    $('#criminal_record').on(
        {
            change: function () {
                if (this.value == 'Y') {
                    add_html(true, 'crc');
                } else {
                    add_html(false, 'crc');
                }
            }
        }
    );

    $('[name="Submit2"]').on(
        {
            click: function () {
                let isLogIn = $('#tab').data('islogin');
                let reLogin = $('#tab').data('reLogin');
                checkSessionTimeoutAndJump2(checkTagAndAddTagData(isLogIn, reLogin),ROOT + '/hr_top3.html')
                // window.location = ROOT + '/hr_top3.html';
            }
        }
    );
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

    getData1();
    getData2();
});
var ARR_LEAVE_REASON = [
    '自願離職',
    '解雇',
    '合約期滿',
    '遣散',
    '其它',
];

function getData1() {
    $.get(ROOT + "/applicant/t4/data", function (json) {
        if (json.state !== 200) {
            alertMessage();
            location.href = ROOT + "/hr_top_m1.html";
            runDialogJS();

            return;
        }
        var info = json.data;
        if (info.length == 0) {
            add_working_experience();
            runDialogJS();


            return;
        }


        for (var i = 0; i < info.length; i++) {

            var num = i + 2;
            add_working_experience();
            Object.keys(info[i]).map(function (key) {
                $('#form-1 :input:not(:radio)').filter(function () {
                    var new_name = this.name.replace(String(num), "");
                    return key == new_name;
                }).val(info[i][key]);
            });
            // $("#have_experience_yes").prop("checked", true);
            var arr1 = info[i].fromDate.split("-");
            $("#from_month" + (num)).val(arr1[0]);
            $("#from_year" + (num)).val(arr1[1]);
            var arr2 = info[i].toDate.split("-");
            $("#to_month" + (num)).val(arr2[0]);
            $("#to_year" + (num)).val(arr2[1]);

            if (info[i].stillEmployed === "Y") {
                $("#still_employed" + num + "_yes").prop("checked", true);

            } else if (info[i].stillEmployed === "N") {
                $("#still_employed" + num + "_no").prop("checked", true);
                document.getElementById('tr_leave_time' + num).style.display = '';
                document.getElementById('tr_leave_reason' + num).style.display = '';
            }
            var arr = getSelectElements("currency" + num);
            for (var j = 0; j < arr.length; j++) {
                if (info[i].currency === arr[j]) {
                    $("#currency" + num).get(0).selectedIndex = j;
                }
            }
            if (info[i].leaveReason && !ARR_LEAVE_REASON.includes(info[i].leaveReason)) {
                // console.log(info[i].leaveReason)
                $("#leave_reason" + num).find("option[value='其它']").attr("selected", true);

                $("#leaveOtherReason" + num).val(info[i].leaveReason)
                $("#span_leaveOtherReason" + num).css('display', '');
            }
        }

        runDialogJS();
    });
}

function getData2() {
    $.get(ROOT + "/applicant/t1/data1", function (json) {
        if (json.state !== 200) {
            // alertMessage();
            // location.href = ROOT + "/hr_top_m1.html";
            runDialogJS();

            return;
        }
        var info = json.data;
        if (info.incompletePageNo <= 4) {
            runDialogJS();

            return;
        }


        if (info.employedBefore === "Y") {
            // $("#employed_before_yes").prop("checked", true);
            $("#employed_before").find("option[value='Y']").attr("selected", true);
            add_html(true, 'eb');
        } else {
            // $("#employed_before_no").prop("checked", true);
            $("#employed_before").find("option[value='N']").attr("selected", true);
        }

        if (info.appliedBaccountBefore === "Y") {
            $("#applied_baccount_before").find("option[value='Y']").attr("selected", true);
        } else {
            $("#applied_baccount_before").find("option[value='N']").attr("selected", true);
        }

        if (info.terminatedBefore === "Y") {
            // $("#terminated_before_yes").prop("checked", true);
            $("#terminated_before").find("option[value='Y']").attr("selected", true);

            add_html(true, 'tbc');
        } else {
            // $("#terminated_before_no").prop("checked", true);
            $("#terminated_before").find("option[value='N']").attr("selected", true);

        }


        if (info.criminalRecord === "Y") {
            // $("#criminal_record_yes").prop("checked", true);
            $("#criminal_record").find("option[value='Y']").attr("selected", true);

            add_html(true, 'crc');
        } else {
            // $("#criminal_record_no").prop("checked", true);
            $("#criminal_record").find("option[value='N']").attr("selected", true);

        }

        Object.keys(info).map(function (key) {
            $('#form-1 :input').filter(function () {
                return key == this.name;
            }).val(info[key]);
        });
        if (info.ebUserName === 0) {
            $('#eb_user_name').val('');
        }

        runDialogJS();
    });
}


function getSelectElements(selectName) {
    var array = []; //定義數組
    $("#" + selectName + " option").each(function () { //遍曆全部option
        var txt = $(this).text(); //獲取option的內容
        // if(txt != "全部") //如果不是“全部”
        array.push(txt); //添加到數組中
    });
    return array;
}


function set_year() {
    var date = new Date;
    var year = date.getFullYear();
    var html = "";
    for (var i = year; i >= year - 50; i--) {
        html += "<option value='" + i + "'>" + i + "</option>";
    }
    return html;
}

function add_working_experience() {
    var abc = document.getElementById('add_working_experience_table');
    var rowCount = abc.rows.length;
    var newRow = abc.insertRow(rowCount++);
    newRow.insertCell(0).innerHTML = "&nbsp;";
    newRow.cells[0].style.height = '50';
    var num = document.getElementById('num').value;
    if (num == "")
        num = 2;
    newRow.insertCell(1).innerHTML = "<p class='count_name self_margin_top_s'> </p>" +
        "<input type='hidden' id='experience_count[]' name='experience_count[]' value='" + num + "'/>" +
        "<input type='hidden' id='experience_row_id" + num + "' name='experience_row_id" + num + "' value='-1' />" +
        "<table width='650' border='0' cellpadding='0' cellspacing='0' class='word'>" +
        "<tr>" +
        "<td height='45'>          <font class='info_title' id='company_name_font" + num + "'>          公司/機構名稱</font>          <br><input class='self_top4_input_large' id='company_name" + num + "'name='companyName" + num + "' type='text' size='25'></td>" +
        "<td rowspan='4' valign='top'>          <font  class='info_title' id='job_duty_font" + num + "'>          職務</font>          <br><textarea rows='9' cols='42' id='job_duty" + num + "' name='jobDuty" + num + "' cols='32' rows='6'></textarea></td>" +

        "</tr>" +
        "<tr>" +
        "<td height='45'>          <font class='info_title'  id='position_font" + num + "'>          職位名稱</font>          <br>  <input class='self_top4_input_large' id='position" + num + "' name='position" + num + "' type='text' size='25'></td>" +
        "</tr>     </td>" +
        "</tr>" +
        "<tr>" +
        "<td>          <font  class='info_title' id='pay_method_font" + num + "'>          薪酬</font>          <br>" +
        "<select class='info_select_small'  id='currency" + num + "'name='currency" + num + "'><option value='-1'>請選擇</option><option value='HKD'>HKD</option><option value='MOP'>MOP</option><option value='CNY'>CNY</option></select>" +

        "<select class='info_select_small'  id='pay_method" + num + "' name='payMethod" + num + "'><option value='-1'>請選擇</option><option value='monthly'>月薪</option><option value='daily'>日薪</option><option value='hourly'>時薪</option></select>" +
        // "          <font class='info_title'  size=2 id='currency_font" + num + "'></font>          <br>" +
        // "          <font  class='info_title' size=2 id='salary_font" + num + "'>          </font>          <br>" +
        "<input class='self_top4_input_small' id='salary" + num + "' name='salary" + num + "' type='text' size='6'>" +
        "          <font class='info_title cls_allowance' size=2 id='allowance_font" + num + "'>津貼</font>          " +
        // "<br>" +
        "<input class='cls_allowance'  id='allowance" + num + "' name='allowance" + num + "' type='text' size='6'></td>" +

        "</tr>" +
        "<tr>" +
        "<td height='40px'><div align='left'>          <font class='info_title'  id='from_font" + num + "'>          入職時間</font>                <select class='info_select_small'  name='from_month" + num + "' id='from_month" + num + "'><option value=' '>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select><select class='info_select_small'  name='from_year" + num + "' id='from_year" + num + "'><option value=' '>年</option>  " + set_year() + " </select>          </div></td>" +

        "</tr>" +
        "<tr>" +
        "<td height='20px' valign='top'>          <font  class='info_title' id='still_employed_font" + num + "'>          現職&emsp;&emsp;</font>      <input type='radio' id='still_employed" + num + "_yes' name='stillEmployed" + num + "' value='Y'>          是&emsp;          <input type='radio' id='still_employed" + num + "_no' name='stillEmployed" + num + "' value='N'>          否         </td>" +

        "</tr>" +
        "<tr style='display: none' id='tr_leave_time" + num +
        "'>" +
        "<td height='40px' >          <font class='info_title'  id='to_font" + num + "'>          離職時間</font>                    <select class='info_select_small'  name='to_month" + num + "' id='to_month" + num + "'> <option value=' '>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select><select class='info_select_small'  name='to_year" + num + "' id='to_year" + num + "'><option value=' '>年</option> " + set_year() + "</select></td>" +
        "</tr>" +
        "<tr style='display: none' id='tr_leave_reason" + num +
        "'>" +
        "<td colspan='2' height='40px'  valign='top'>          <font  class='info_title' id='leave_reason_font" + num + "'>          離職原因</font>          " +
        "<select class='info_select_middle'  id='leave_reason" + num + "' name='leaveReason" + num + "'  >" +
        "<option value='' style='display: none'></option>" +
        "<option value='自願離職'>自願離職</option>" +
        "<option value='解雇'>解雇</option>" +
        "<option value='合約期滿'>合約期滿</option>" +
        "<option value='遣散'>遣散</option>" +
        "<option value='其它'>其它</option>" +
        "</select>" +

        // "<td>dsdf" +
        // "</td>" +

        // "<td>" +
        "    <span id='span_leaveOtherReason" + num + "' style='display:none;'>      <font  class='info_title' size=2 >其它原因</font>          " +
        "<input   id='leaveOtherReason" + num + "' name='leaveOtherReason" + num + "' type='text' ></span>" +
        // "</td>" +
        "</td>" +

        "</tr>" +
        "</table></table>";
    newRow.cells[1].style.height = '40';
    newRow.cells[1].style.width = '650';

    newRow.cells[1].style.colspan = '2';
    var name = 'add_working_experience_table';

    if (num != 2) {
        // var string = "<input type='button' value='刪除' id ='delete-A" + num + "'  />";
        var string = '<div id="delete-A' + num +
            '" class="page_title_small" style="cursor: pointer;width: 50px;margin-top: 20px">－刪除</div> ';
        newRow.insertCell(2).innerHTML = string;
        newRow.cells[2].style.height = '40';

    }


    //onclick='MydeleteRow(this.parentNode.parentNode.rowIndex," + name + "," + num + ");'
    $('#delete-A' + num).on(
        {
            click: function () {
                MydeleteRow(this.parentNode.parentNode.rowIndex, name, $(this).attr('id').replace(/\D/g, ""));
                $(".count_name").each(function (index, element) {
                    // $(this).text('工作經驗' + NoToChinese(index + 1));
                    $(this).html('<div style="border-top:1px dotted rgba(169,169,169,0.85);width: 650px "></div>')

                });
            }
        }
    );
    let still_employed_name_yes = '#still_employed' + num + '_yes';
    let still_employed_name_no = '#still_employed' + num + '_no';
    let leave_reason = '#leave_reason' + num;

    // document.getElementById('tr_leave_time' + num).style.display = '';
    // document.getElementById('tr_leave_reason' + num).style.display = '';

    $(still_employed_name_yes).on(
        {
            change: function () {
                $(this).parent().parent().nextAll().css('display', 'none');
                $(this).parent().parent().next().find("option[value=' ']").attr("selected", true);
                // $(this).parent().parent().next().next().find("input").val('');
                $(this).parent().parent().next().next().find("option[value='']").attr("selected", true);


            }
        }
    );
    $(still_employed_name_no).on(
        {
            change: function () {
                $(this).parent().parent().nextAll().css('display', '');
            }
        }
    );


    $(leave_reason).on(
        {
            change: function () {
                if (this.value == '其它') {
                    $(this).next().css('display', '');
                } else {
                    $(this).next().css('display', 'none');
                }
            }
        }
    );


    var newRow = abc.insertRow(rowCount++);
    num++;
    document.getElementById('num').value = num;


    $(".count_name").each(function (index, element) {
        // $(this).text('工作經驗' + NoToChinese(index + 1));
        $(this).html('<div style="border-top:1px dotted rgba(169,169,169,0.85);width: 650px "></div>')

    });
}

//阿拉伯數字轉中文數字
function NoToChinese(num) {

    var chnNumChar = ["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"];
    var chnUnitSection = ["", "万", "亿", "万亿", "亿亿"];
    var chnUnitChar = ["", "十", "百", "千"];
    var unitPos = 0;
    var strIns = '', chnStr = '';
    var needZero = false;
    if (num === 0) {
        return chnNumChar[0];
    }

    while (num > 0) {
        var section = num % 10000;
        if (needZero) {
            chnStr = chnNumChar[0] + chnStr;
        }
        strIns = SectionToChinese(section);
        strIns += (section !== 0) ? chnUnitSection[unitPos] : chnUnitSection[0];
        chnStr = strIns + chnStr;
        needZero = (section < 1000) && (section > 0);
        num = Math.floor(num / 10000);
        unitPos++;
    }

    return chnStr;
}

function SectionToChinese(section) {
    var index = 0;
    var chnNumChar = ["零", "一", "二", "三", "四", "五", "六", "七", "八", "九"];
    var chnUnitSection = ["", "万", "亿", "万亿", "亿亿"];
    var chnUnitChar = ["", "十", "百", "千"];
    var strIns = '', chnStr = '';
    var unitPos = 0;
    var zero = true;
    while (section > 0) {
        index++;
        var v = section % 10;
        if (v === 0) {
            if (!zero) {
                zero = true;
                chnStr = chnNumChar[v] + chnStr;
            }
        } else {
            zero = false;
            strIns = chnNumChar[v];
            if (strIns == '一' && chnUnitChar[unitPos] == "十") strIns = "";
            strIns += chnUnitChar[unitPos];
            chnStr = strIns + chnStr;
        }
        unitPos++;
        section = Math.floor(section / 10);
    }
    return chnStr;


};

function add_html(flag, name) {
    var userOutput = '';
    if (flag && name == 'eb') {
        //建立“曾在本公司工作”填寫模組 20120820 starry
        userOutput = "<table width='350' border='0' cellpadding='0' cellspacing='0' class='word'>" +
            "<tr>" +
            "<td height='30'>     <font class='info_title'  id='eb_posi_font'>＊職位</font>     <input id='eb_posi' name='ebPosi' type='text' size='25'></td>" +

            "</tr>" +
            "<tr>" +
            "<td height='30'>     <font class='info_title' id='eb_dept_font'>＊部門</font>     <input id='eb_dept' name='ebDept' type='text' size='25'></td>" +

            "</tr>" +
            "<tr>" +
            // "<td height='30'>     <font class='info_title'  id='eb_time_font'>＊年資</font>     <input id='eb_time' name='ebTime' type='text' size='6'></td>" +
            "<td height='30'>     <font class='info_title'  id='eb_user_name_font'>&emsp;員工編號&emsp;&emsp;</font>     <input class='self_top4_input_middle' id='eb_user_name' name='ebUserName' type='text' size='18'></td>" +

            "</tr>" +
            "</table>";
    } else if (flag && name == 'tbc') {
        //建立“曾被解僱”填寫模組 20120820 starry
        userOutput = "<table width='300' border='0' cellpadding='0' cellspacing='0' class='word'>" +
            "<tr>" +
            "<td height='45'><font class='info_title'  id='tb_cause_font'>＊原因</font><br>" +
            "<textarea id='tb_cause' name='tbCause' cols='30' rows='3'></textarea></td>" +
            "</tr>" +
            "</table>";
    } else if (flag && name == 'crc') {
        //建立“曾涉刑事罪行”填寫模組 20120820 starry
        userOutput = "<table width='300' border='0' cellpadding='0' cellspacing='0' class='word'>" +
            "<tr>" +
            "<td height='45'><font  class='info_title' id='cr_cause_font'>＊原因</font><br>" +
            "<textarea id='cr_cause' name='crCause' cols='30' rows='3'></textarea>" +
            "</td>" +


            "</tr>" +
            "</table>";
    } else {
        //選擇否時將資料改為空，相當刪除
        if (name == 'eb')
            userOutput = "<input type='hidden' name='ebDept' value=''>" +
                "<input type='hidden' name='ebPosi' value=''>" +
                "<input type='hidden' name='ebTime' value=''>";

        if (name == 'tbc')
            userOutput = "<input type='hidden' name='tbCause' value=''>";

        if (name == 'crc')
            userOutpu = "<input type='hidden' name='crCause' value=''>";
    }

    document.getElementById(name).innerHTML = userOutput;


}


function MydeleteRow(num, name, id) {
    var r = confirm("確定要刪除?");
    if (r == true) {
        row_name = 'experience_row_id' + id;
        abc = document.getElementById(row_name).value;
        if (abc > 0) {
            if (document.getElementById('delete_id').value == '') {
                document.getElementById('delete_id').value = abc;
            } else document.getElementById('delete_id').value = document.getElementById('delete_id').value + ',' + abc;
        }
        document.getElementById(name).deleteRow(num);
        document.getElementById(name).deleteRow(num);

        //var eduction=document.getElementById('num').value;
        is_have = false;
        var length = document.getElementById(name).rows.length;
        for (i = 2; i < length; i++) {
            if (document.getElementById(name).rows[i]) {
                is_have = true;
            }
        }
        if (!is_have) {
            document.getElementById('have_experience_no').checked = true;
        }

    }
}

function add() {

    var num = document.getElementById('num').value;
    if (num == "") {
        num = 2;
        document.getElementById('num').value = num;
    }
    is_add = true;
    for (i = 2; i <= document.getElementById('num').value; i++) {
        var name = 'company_name' + i;
        if (document.getElementById(name))
            is_add = false;

    }
    if (is_add)
        add_working_experience();
}


function delete_all() {
    var r = confirm("確定要全部刪除?");
    if (r == true) {
        for (id = 2; id <= document.getElementById('num').value; id++) {

            row_name = 'experience_row_id' + id;
            if (document.getElementById(row_name)) {
                abc = document.getElementById(row_name).value;

                if (abc > 0) {
                    if (document.getElementById('delete_id').value == '') {
                        document.getElementById('delete_id').value = abc;
                    } else document.getElementById('delete_id').value = document.getElementById('delete_id').value + ',' + abc;
                }
            }
        }

        var length = document.getElementById('add_working_experience_table').rows.length;
        for (i = 2; i < length; i++) {
            document.getElementById('add_working_experience_table').deleteRow(2);
        }
    } else document.getElementById('have_experience_yes').checked = true;
}

function send(form,send_type) {
    var input = document.getElementById('num').value;
    var is_send = true;

    document.getElementById("error").innerHTML = "";

    var font = document.getElementsByTagName('font');
    for (i = 0; i < font.length; i++) {
        font[i].color = 'black';
    }
    /*
        for (i = 2; i <= document.getElementById('num').value; i++) {

            company_name = 'company_name' + i;
            company_name_font = 'company_name_font' + i;
            if (document.getElementById(company_name)) {
                if (document.getElementById(company_name).value == '') {
                    document.getElementById(company_name_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                from_month = 'from_month' + i;
                from_year = 'from_year' + i;
                from_font = 'from_font' + i;
                if (document.getElementById(from_month).value == '-1' || document.getElementById(from_year).value == '-1') {
                    document.getElementById(from_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                to_month = 'to_month' + i;
                to_year = 'to_year' + i;
                to_font = 'to_font' + i;
                if (document.getElementById(to_month).value == '-1' || document.getElementById(to_year).value == '-1') {
                    document.getElementById(to_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                position_name = 'position' + i;
                position_font = 'position_font' + i;
                if (document.getElementById(position_name).value == '') {
                    document.getElementById(position_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                pay_method_name = 'pay_method' + i;
                pay_method_font = 'pay_method_font' + i;
                if (document.getElementById(pay_method_name).value == '-1') {
                    document.getElementById(pay_method_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                currency_name = 'currency' + i;
                currency_font = 'currency_font' + i;
                if (document.getElementById(currency_name).value == '-1') {
                    document.getElementById(currency_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                salary_name = 'salary' + i;
                salary_font = 'salary_font' + i;
                if (document.getElementById(salary_name).value == '' || isNaN(document.getElementById(salary_name).value)) {
                    document.getElementById(salary_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }
                salary_font
                job_duty_name = 'job_duty' + i;
                job_duty_font = 'job_duty_font' + i;
                if (document.getElementById(job_duty_name).value == '') {
                    document.getElementById(job_duty_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }


                still_employed_name_yes = 'still_employed' + i + '_yes';
                still_employed_name_no = 'still_employed' + i + '_no';
                still_employed_font = 'still_employed_font' + i;
                if (document.getElementById(still_employed_name_yes).checked != true && document.getElementById(still_employed_name_no).checked != true) {
                    document.getElementById(still_employed_font).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                if (document.getElementById(still_employed_name_yes).checked != true) {
                    leave_reason_name = 'leave_reason' + i;
                    leave_reason_font = 'leave_reason_font' + i;
                    if (document.getElementById(leave_reason_name).value == '') {
                        document.getElementById(leave_reason_font).color = 'red';
                        is_send = false;
                        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                    }
                }
            }
        }
    */
    /*   if (document.getElementById("employed_before_yes").checked != true && document.getElementById("employed_before_no").checked != true) {
           document.getElementById("employed_before").color = 'red';
           is_send = false;
           document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
       } else if (document.getElementById("employed_before_yes").checked == true) {
           if (document.getElementById("eb_dept").value == '' || document.getElementById("eb_posi").value == '' || document.getElementById("eb_time").value == '') {
               document.getElementById("eb_dept_font").color = 'red';
               document.getElementById("eb_posi_font").color = 'red';
               document.getElementById("eb_time_font").color = 'red';
               is_send = false;
               document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
           }
       }*/

    if (!document.getElementById('employed_before').value) {
        document.getElementById("employed_before_font").color = 'red';
        is_send = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else if (document.getElementById('employed_before').value == "Y") {
        if (!document.getElementById("eb_dept").value) {
            document.getElementById("eb_dept_font").color = 'red';
            is_send = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
        if (!document.getElementById("eb_posi").value) {
            document.getElementById("eb_posi_font").color = 'red';
            is_send = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }
    }
    if (!document.getElementById('applied_baccount_before').value) {
        document.getElementById("applied_baccount_before_font").color = 'red';
        is_send = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    }


    if (!document.getElementById('terminated_before').value) {
        document.getElementById("terminated_before_font").color = 'red';
        is_send = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else if (document.getElementById('terminated_before').value == "Y") {
        if (!document.getElementById("tb_cause").value) {
            document.getElementById("tb_cause_font").color = 'red';
            is_send = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }

    }
    if (!document.getElementById('criminal_record').value) {
        document.getElementById("criminal_record_font").color = 'red';
        is_send = false;
        document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
    } else if (document.getElementById('criminal_record').value == "Y") {
        if (!document.getElementById("cr_cause").value) {
            document.getElementById("cr_cause_font").color = 'red';
            is_send = false;
            document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
        }

    }
    if (is_send == true)
        for (var i = 2; i <= input; i++) {
            from_font = 'from_font' + i;
            from_month = 'from_month' + i;
            from_year = 'from_year' + i;
            to_month = 'to_month' + i;
            to_year = 'to_year' + i;
            /* if (document.getElementById(from_month)) {
                 if (document.getElementById(from_year).value > document.getElementById(to_year).value) {
                     document.getElementById(from_font).color = 'red';
                     is_send = false;
                     document.getElementById("error").innerHTML = "<font color='red'>開始時間大於結束時間</font><br>";
                     break;
                 } else if (document.getElementById(from_year).value == document.getElementById(to_year).value) {
                     if (document.getElementById(from_month).value > document.getElementById(to_month).value) {
                         document.getElementById(from_font).color = 'red';
                         is_send = false;
                         document.getElementById("error").innerHTML = "<font color='red'>開始時間大於結束時間</font><br>";
                         break;
                     }
                 }

             }*/
        }
    if (!is_send){
        $('.t1').css('height','421px')
        if (send_type === 'dialog_saveAndGo') {
            let dialog = $('dialog')[0];
            dialog.close();
            alert("儲存失敗, 紅色部份為沒填寫或者格式錯誤");
        }
    }
    if (is_send == true) {
        submit(send_type);
    }
}

function submit(send_type) {
    // form.submit();
    document.getElementById('type').value = 'experience';

    var url = ROOT + "/applicant/top4";
    var data = $("#form-1").serialize();

    $.ajax({
        "url": url,
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {
                if (send_type=='onlySave'){
                    alert("儲存成功")
                }
                else if (send_type=='dialog_saveAndGo'){
                    location.href = CURRENT_GOTO;
                }
                else {
                    location = ROOT + "/hr_top5.html";
                }
            } else {
                if (json.state !== 999)
                    alert(json.message);
            }
        }
        ,
        "error": function (xhr) {
            // if (xhr.status === 200) {
            // alert("操作超時")
            alertMessage();
            location.href = ROOT + "/hr_top_m1.html";
            // }
        }
    });
}
