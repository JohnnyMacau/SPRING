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

    $('#imgXL').on(
        {
            click: function () {
                add_eduction();
            }
        }
    );

    $('#imgZS').on(
        {
            click: function () {
                add_certification();
            }
        }
    );

    $('[name="Submit2"]').on(
        {
            click: function () {
                let isLogIn = $('#tab').data('islogin');
                let reLogin = $('#tab').data('reLogin');
                checkSessionTimeoutAndJump2(checkTagAndAddTagData(isLogIn, reLogin),ROOT + '/hr_top1.html')
                // window.location = ROOT + '/hr_top1.html';
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
    add_eduction();
    add_certification();
    getData2();getData1();

});

function getData1() {
    $.get(ROOT + "/applicant/t3/data1", function (json) {
        if (json.state !== 200) {
            alertMessage();
            location.href = ROOT + "/hr_top_m1.html";
            runDialogJS();

            return;
        }
        var info = json.data;
        if (info.length == 0) {
            runDialogJS();

            return;
        }
        var arr = getSelectElements2("degree2");
        for (var i = 0; i < info.length; i++) {
            if (i > 0) add_eduction();
            for (var j = 0; j < arr.length; j++) {
                if (info[i].degree === arr[j]) {
                    $("#degree" + (i + 2)).get(0).selectedIndex = j;
                }
            }
            var arr1 = info[i].fromDate.split("-");
            $("#from_month" + (i + 2)).val(arr1[0]);
            $("#from_year" + (i + 2)).val(arr1[1]);
            var arr2 = info[i].toDate.split("-");
            $("#to_month" + (i + 2)).val(arr2[0]);
            $("#to_year" + (i + 2)).val(arr2[1]);
            $("#organization_name" + (i + 2)).val(info[i].organizationName);
            $("#major" + (i + 2)).val(info[i].major);
        }

        runDialogJS();
    });
}


function getData2() {
    $.get(ROOT + "/applicant/t3/data2", function (json) {
        if (json.state !== 200) {
            // alertMessage();
            // location.href = ROOT + "/hr_top_m1.html";
            runDialogJS();

            return;
        }
        var info = json.data;
        if (info.length == 0) {
            runDialogJS();

            return;
        }
        for (var i = 0; i < info.length; i++) {
            if (i > 0) add_certification();
            $("#cert_name" + (i + 2)).val(info[i].certName);
            var arr1 = info[i].issueDate.split("-");
            $("#cert_month" + (i + 2)).val(arr1[0]);
            $("#cert_year" + (i + 2)).val(arr1[1]);
            $("#cert_organization_name" + (i + 2)).val(info[i].organizationName);
        }

        runDialogJS();
    });
}

/*function getSelectElements(selectName) {
    var array = []; //定義數組
    $("#" + selectName + " option").each(function () { //遍曆全部option
        var txt = $(this).text(); //獲取option的內容
        // if(txt != "全部") //如果不是“全部”
        array.push(txt); //添加到數組中
    });
    return array;
}*/
function getSelectElements2(selectName) {
    var array = []; //定义数组
    $("#" + selectName + " option").each(function () { //遍历全部option
        var txt = $(this).val(); //获取option的值
        // if(txt != "全部") //如果不是“全部”
        array.push(txt); //添加到数组中
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

function set_year2() {
    var date = new Date;
    var year = date.getFullYear() + 5;
    var html = "";
    for (var i = year; i >= year - 50; i--) {
        html += "<option value='" + i + "'>" + i + "</option>";
    }
    return html;
}


function add_eduction() {
    var abc = document.getElementById('add_eduction_table');
    var rowCount = abc.rows.length;
    var newRow = abc.insertRow(rowCount++);
    newRow.insertCell(0).innerHTML = "&nbsp;";
    newRow.cells[0].style.height = '50';
    var num = document.getElementById('num').value;
    if (num == "")
        num = 2;
    /*  if (num === 2) {
          newRow.insertCell(1).innerHTML = "<input type='hidden' id='education_count[]' name='education_count[]' value='" + num + "'/>" +
              "<input type='hidden' id='row_id" + num + "' name='row_id" + num + "' value='-1' />" +
              "<table width='630' border='0' cellpadding='0' cellspacing='0' class='word'>" +
              "<tr>" +
              "<td width='100'><strong><font id='degree_font" + num + "'>＊學歷</font></strong><br>" +
              "<select  name='degree" + num + "' id ='degree" + num + "'>" +
              "<option value='-1' selected>請選擇</option>" +
              "<option value='小學'>小學</option>" +
              "<option value='中學'>中學</option>" +
              "<option value='大專'>大專</option>" +
              "<option value='本科'>本科</option>" +
              "<option value='本科以上'>本科以上</option>" +
              "<option value='其他'>其他</option>" +
              "</select>" +
              "</td>" +
              "<td width='120'><strong><font id='from_font" + num + "'>＊由</font></strong><br>" +
              "<strong>" +
              "<select name='from_month" + num + "' id='from_month" + num + "'><option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
              "<select name='from_year" + num + "' id='from_year" + num + "'><option value='-1'>年</option> " + set_year() + "</select>" +
              "</strong>" +
              "</td>" +
              "<td width='120'>" +
              "<strong>" +
              "<font id='to_font" + num + "'>＊到</font></strong><br><strong>" +
              "<select name='to_month" + num + "' id='to_month" + num + "'> <option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
              "<select name='to_year" + num + "' id='to_year" + num + "'><option value='-1'>年</option> " + set_year2() + "</select>" +
              "</strong>" +
              "</td>" +
              "<td width='140'>" +
              "<strong>" +
              "<font id='organization_name_font" + num + "'>＊學校</font>" +
              "</strong><br>" +
              "<input name='organizationName" + num + "' id='organization_name" + num + "' type='text' size='18'>" +
              "</td>" +
              "<td width='140'>" +
              "<strong><font id='major_font" + num + "'>＊專科</font></strong><br>" +
              "<input name='major" + num + "' id='major" + num + "' type='text' size='18'>" +
              "</td>" +
              "<td width='40' valign='bottom'>&nbsp;</td></tr></table>";
      } else {
          newRow.insertCell(1).innerHTML = "<input type='hidden' id='education_count[]' name='education_count[]' value='" + num + "'/>" +
              "<input type='hidden' id='row_id" + num + "' name='row_id" + num + "' value='-1' />" +
              "<table width='630' border='0' cellpadding='0' cellspacing='0' class='word'>" +
              "<tr>" +
              "<td width='100'><strong><font id='degree_font" + num + "'>&nbsp;&nbsp;&nbsp;學歷</font></strong><br>" +
              "<select  name='degree" + num + "' id ='degree" + num + "'>" +
              "<option value='-1' selected>請選擇</option>" +
              "<option value='小學'>小學</option>" +
              "<option value='中學'>中學</option>" +
              "<option value='大專'>大專</option>" +
              "<option value='本科'>本科</option>" +
              "<option value='本科以上'>本科以上</option>" +
              "<option value='其他'>其他</option>" +
              "</select>" +
              "</td>" +
              "<td width='120'><strong><font id='from_font" + num + "'>&nbsp;&nbsp;&nbsp;由</font></strong><br>" +
              "<strong>" +
              "<select name='from_month" + num + "' id='from_month" + num + "'><option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
              "<select name='from_year" + num + "' id='from_year" + num + "'><option value='-1'>年</option> " + set_year() + "</select>" +
              "</strong>" +
              "</td>" +
              "<td width='120'>" +
              "<strong>" +
              "<font id='to_font" + num + "'>&nbsp;&nbsp;&nbsp;到</font></strong><br><strong>" +
              "<select name='to_month" + num + "' id='to_month" + num + "'> <option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
              "<select name='to_year" + num + "' id='to_year" + num + "'><option value='-1'>年</option> " + set_year2() + "</select>" +
              "</strong>" +
              "</td>" +
              "<td width='140'>" +
              "<strong>" +
              "<font id='organization_name_font" + num + "'>&nbsp;&nbsp;&nbsp;學校</font>" +
              "</strong><br>" +
              "<input name='organizationName" + num + "' id='organization_name" + num + "' type='text' size='18'>" +
              "</td>" +
              "<td width='140'>" +
              "<strong><font id='major_font" + num + "'>&nbsp;&nbsp;&nbsp;專科</font></strong><br>" +
              "<input name='major" + num + "' id='major" + num + "' type='text' size='18'>" +
              "</td>" +
              "<td width='40' valign='bottom'>&nbsp;</td></tr></table>";
      }*/


    newRow.insertCell(1).innerHTML = "<input type='hidden' id='education_count[]' name='education_count[]' value='" + num + "'/>" +
        "<input type='hidden' id='row_id" + num + "' name='row_id" + num + "' value='-1' />" +
        "<table width='800' border='0' cellpadding='0' cellspacing='0' class='word'>" +
        "<tr>" +
        "<td width='90' align='center'>          <font  class='info_title'  id='degree_font" + num + "'>程度</font></strong><br>" +
        "<select  class='info_select_small'  name='degree" + num + "' id ='degree" + num + "'>" +
        "<option value='-1' selected>請選擇</option>"+
        // "<option value='小學'>小學</option>"+
        // "<option value='初中'>初中</option>"+
        // "<option value='高中'>高中</option>"+
        // "<option value='學士'>學士</option>"+
        // "<option value='碩士'>碩士</option>"+
        // "<option value='博士'>博士</option>"+
        "<option value='小學畢業或以下'>小學畢業或以下</option>" +
        "<option value='初中'>初中</option>" +
        "<option value='初中畢業'>初中畢業</option>" +
        "<option value='高中'>高中</option>" +
        "<option value='高中畢業'>高中畢業</option>" +
        "<option value='大專/高級文憑'>大專/高級文憑</option>" +
        "<option value='學士'>學士</option>" +
        "<option value='學士以上'>學士以上</option>" +
        "</select>" +
        "</td>" +
        "<td width='170' align='center'>          <font class='info_title' id='from_font" + num + "'>開始時間</font></strong><br>" +
        "          " +
        "<select  class='info_select_small'  name='from_month" + num + "' id='from_month" + num + "'><option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
        "<select class='info_select_small'  name='from_year" + num + "' id='from_year" + num + "'><option value='-1'>年</option> "+set_year()+"</select>" +
        "</strong>" +
        "</td>" +
        "<td width='170' align='center'>" +
        "          " +
        "<font class='info_title' id='to_font" + num + "'>結束時間</font></strong><br>          " +
        "<select class='info_select_small'  name='to_month" + num + "' id='to_month" + num + "'> <option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
        "<select  class='info_select_small' name='to_year" + num + "' id='to_year" + num + "'><option value='-1'>年</option> "+set_year2()+"</select>" +
        "</strong>" +
        "</td>" +
        "<td width='140' align='center'>" +
        "          " +
        "<font class='info_title'  id='organization_name_font" + num + "'>就讀學校</font>" +
        "</strong><br>" +
        "<input name='organizationName" + num + "' id='organization_name" + num + "' type='text' size='18'>" +
        "</td>" +
        "<td width='140' align='center'>" +
        "          <font class='info_title'  id='major_font" + num + "'>就讀專業</font></strong><br>" +
        "<input name='major" + num + "' id='major" + num + "' type='text' size='18'>" +
        "</td>" +
        "<td width='20' valign='bottom'>&nbsp;</td></tr></table>";

    newRow.cells[1].style.height = '40';
    newRow.cells[1].style.colspan = '2';


    $('#degree' + num).on(
        {
            change: function () {
                hidden1($(this).attr('id').replace(/\D/g, ""));
            }
        }
    );


    var name = 'add_eduction_table';
    var row_type = 'row_id'
    var delete_text = 'delete_id';
    if (num != 2) {
        // var string = "<input type='button' value='刪除' id ='delete-A" + num + "' />";
        var string =     '<div id="delete-A' + num +
            '" class="page_title_small" style="cursor: pointer;width: 50px;margin-top: 20px">－刪除</div> ';
        newRow.insertCell(2).innerHTML = string;
        // onclick='MydeleteRow(this.parentNode.parentNode.rowIndex," + name + "," + num + "," + delete_text + "," + row_type + ");'

        $('#delete-A' + num).on(
            {
                click: function () {
                    MydeleteRow(this.parentNode.parentNode.rowIndex, name, $(this).attr('id').replace(/\D/g, ""), delete_text, row_type);
                }
            }
        );

        newRow.cells[2].style.height = '40';
    }


    num++;
    document.getElementById('num').value = num;
}

function add_certification() {
    var abc = document.getElementById('add_certification_table');
    var rowCount = abc.rows.length;
    var newRow = abc.insertRow(rowCount++);
    newRow.insertCell(0).innerHTML = "&nbsp;";
    newRow.cells[0].style.height = '50';
    var num = document.getElementById('num1').value;
    if (num == "")
        num = 2;
    newRow.insertCell(1).innerHTML = "<input type='hidden' id='certification_count[]' name='certification_count[]' value='" + num + "'/>" +
        "<input type='hidden' id='certification_row_id" + num + "' name='certification_row_id" + num + "' value='-1' />" +
        "<table width='100%' border='0' cellpadding='0' cellspacing='0' class='word'>" +
        "<tr>" +


        "<td width='188' align='center'>" +
        "          <font class='info_title'  id='cert_organization_name_font" + num + "'>頒發機構</font></strong><br>" +
        "<input id='cert_organization_name" + num + "' name='cert_organization_name" + num + "' type='text' size='25'>" +
        "</td>" +


        "<td width='199' align='center'>" +
        "<div align='center'>" +
        "          <font class='info_title'  id='cert_date_font" + num + "'>頒發時間</font></strong><br>" +
        "<select class='info_select_small'  name='cert_month" + num + "' id='cert_month" + num + "'><option value='-1'>月</option><option value='1'>1</option><option value='2'>2</option><option value='3'>3</option><option value='4'>4</option><option value='5'>5</option><option value='6'>6</option><option value='7'>7</option><option value='8'>8</option><option value='9'>9</option><option value='10'>10</option><option value='11'>11</option><option value='12'>12</option></select>" +
        "<select  class='info_select_small' name='cert_year" + num + "' id='cert_year" + num + "'><option value='-1'>年</option> "+set_year()+" </select>" +
        "</div>" +
        "</td>" +


        "<td width='203' align='center'>" +
        "          <font  class='info_title' id='cert_name_font" + num + "'>證書/認可資格</font></strong><br>" +
        "<input name='cert_name" + num + "' id='cert_name" + num + "' type='text' size='25'>" +
        "</td>" +


        "<td width='20' valign='bottom'>&nbsp;</td></tr></table>";
    newRow.cells[1].style.height = '40';
    newRow.cells[1].style.colspan = '2';
    var name = 'add_certification_table';
    var delete_text = 'cert_delete_id';
    var row_type = 'certification_row_id';
    if (num != 2) {
        // var string = "<input type='button' value='刪除' id ='delete-B" + num + "' />";
        var string =     '<div id="delete-B' + num +
            '" class="page_title_small" style="cursor: pointer;width: 50px;margin-top: 20px">－刪除</div> ';
        newRow.insertCell(2).innerHTML = string;
        // onclick='MydeleteRow(this.parentNode.parentNode.rowIndex," + name + "," + num + "," + delete_text + "," + row_type + ");'

        $('#delete-B' + num).on(
            {
                click: function () {
                    MydeleteRow(this.parentNode.parentNode.rowIndex, name, $(this).attr('id').replace(/\D/g, ""), delete_text, row_type);
                }
            }
        );

        newRow.cells[2].style.height = '40';
    }


    num++;
    document.getElementById('num1').value = num;
}

function MydeleteRow(num, name, id, text_name, row_type) {
    var r = confirm("確定要刪除?");
    if (r == true) {
        row_name = row_type + id;
        abc = document.getElementById(row_name).value;
        if (abc > 0) {
            if (document.getElementById(text_name).value == '') {
                document.getElementById(text_name).value = abc;
            } else document.getElementById(text_name).value = document.getElementById(text_name).value + ',' + abc;
        }
        document.getElementById(name).deleteRow(num);
    }
    //var eduction=document.getElementById('num').value;
}

function hidden1(num) {
    var degree_name = 'degree' + num;
    var major_name = 'major' + num;
    if (document.getElementById(degree_name).value == '小學畢業或以下' ||
        document.getElementById(degree_name).value == '初中' ||
        document.getElementById(degree_name).value == '初中畢業'
    ) {
        document.getElementById(major_name).value = 'N/A';
        document.getElementById(major_name).readOnly = true;
    } else {
        document.getElementById(major_name).value = '';
        document.getElementById(major_name).readOnly = false;
    }
}

function send(form,send_type) {
    var input = document.getElementById('num').value;
    var is_send = true;

    document.getElementById("error").innerHTML = "";

    var font = document.getElementsByTagName('font');
    for (i = 0; i < font.length; i++) {
        font[i].color = 'black';
    }

    for (i = 2; i <= document.getElementById('num').value; i++) {
        degree_name = 'degree' + i;
        degree_font_name = 'degree_font' + i;
        if (document.getElementById(degree_name)) {
            if (document.getElementById(degree_name).value == -1) {
                document.getElementById(degree_font_name).color = 'red';
                is_send = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }

            from_month_name = 'from_month' + i;
            from_year_name = 'from_year' + i;
            from_font_name = 'from_font' + i;

            if (document.getElementById(from_month_name).value == -1 || document.getElementById(from_year_name).value == -1) {
                document.getElementById(from_font_name).color = 'red';
                is_send = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }
            to_month_name = 'to_month' + i;
            to_year_name = 'to_year' + i;
            to_font_name = 'to_font' + i;
            if (document.getElementById(to_month_name).value == -1 || document.getElementById(to_year_name).value == -1) {
                document.getElementById(to_font_name).color = 'red';
                is_send = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }

            organization_name = 'organization_name' + i;
            organization_font_name = 'organization_name_font' + i;
            if (document.getElementById(organization_name).value == '') {
                document.getElementById(organization_font_name).color = 'red';
                is_send = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }

            major_name = 'major' + i;
            major_font_name = 'major_font' + i;
            if (document.getElementById(major_name).value == '') {
                document.getElementById(major_font_name).color = 'red';
                is_send = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }
        }
    }
    if (is_send == true)
        for (var i = 2; i <= input; i++) {
            from_font = "from_font" + i;
            from_month = 'from_month' + i;
            from_year = 'from_year' + i;
            to_month = 'to_month' + i;
            to_year = 'to_year' + i;
            if (document.getElementById(from_month)) {
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

            }
        }

    for (i = 2; i <= document.getElementById('num1').value; i++) {

        cert_name = 'cert_name' + i;
        cert_font_name = 'cert_name_font' + i;
        cert_month_name = 'cert_month' + i;
        cert_year_name = 'cert_year' + i
        cert_date_font_name = 'cert_date_font' + i;
        cert_organization_name = 'cert_organization_name' + i;
        cert_organization_font_name = 'cert_organization_name_font' + i;
        if (document.getElementById(cert_name)) {
            if (document.getElementById(cert_name).value != '' || document.getElementById(cert_month_name).value != -1 || document.getElementById(cert_year_name).value != -1 || document.getElementById(cert_organization_name).value != '') {
                if (document.getElementById(cert_name).value == '') {
                    document.getElementById(cert_font_name).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                if (document.getElementById(cert_month_name).value == -1) {
                    document.getElementById(cert_date_font_name).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                if (document.getElementById(cert_year_name).value == -1) {
                    document.getElementById(cert_date_font_name).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }

                if (document.getElementById(cert_organization_name).value == '') {
                    document.getElementById(cert_organization_font_name).color = 'red';
                    is_send = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }
            }
        }
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
    document.getElementById('type').value = 'education';

    var url = ROOT + "/applicant/top3";
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
                    location = ROOT + "/hr_top4.html";
                }
            } else {
                if (json.state !== 999) alert(json.message);
            }
        }
        ,
        "error": function (xhr) {
            // if (xhr.status === 200) {
            // alert("登錄過期,請重新登錄")
            // alert("操作超時")
            alertMessage();
            location.href = ROOT + "/hr_top_m1.html";
            // }
        }


    });
}
