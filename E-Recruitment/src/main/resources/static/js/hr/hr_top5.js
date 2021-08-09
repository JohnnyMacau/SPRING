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
    $('#other_language_btn').on(
        {
            click: function () {
                add_language(document.getElementById('other_language').value, 'Y');
            }
        }
    );

    $('#imgQTYY').on(
        {
            click: function () {
                document.getElementById('show_other_language').style.display = '';
            }
        }
    );


    /*   $('#other_skill_name_btn').on(
           {
               click: function () {
                   add_other_skill(document.getElementById('other_skill_name').value);
               }
           }
       );*/

    $('#imgQTJL').on(
        {
            click: function () {
                document.getElementById('show_other_skill').style.display = '';
            }
        }
    );


    $('[name="Submit2"]').on(
        {
            click: function () {
                let isLogIn = $('#tab').data('islogin');
                let reLogin = $('#tab').data('reLogin');
                checkSessionTimeoutAndJump2(checkTagAndAddTagData(isLogIn, reLogin),ROOT + '/hr_top4.html')
                // window.location = ROOT + '/hr_top4.html';
            }
        }
    );
    $('#onlySave').on(
        {
            click: function () {
                check(this.form, 'onlySave');
            }
        }
    );
    $('[name="Submit"]').on(
        {
            click: function () {
                check(this.form, 'saveAndNext');
            }
        }
    );


    add_language("廣東話", 'N');
    add_language("英語", 'N');
    add_language("國語", 'N');
    getData1();
    getData2();
});


function getData1() {
    $.get(ROOT + "/applicant/t5/data1", function (json) {
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

        var arr = getSelectElements("written2");
        var arr2 = getSelectElements("conversation2");
        var arr3 = getSelectElements("certificate3");


        for (var i = 0; i < info.length; i++) {
            if (i > 2) add_language(info[i].name, "Y");


            var num = i + 2;
            $("#lanauage_name" + num).val(info[i].name);


            for (var k = 0; k < arr.length; k++) {
                if (info[i].written === arr[k]) {
                    $("#written" + num).get(0).selectedIndex = k;
                }
            }
            for (var m = 0; m < arr.length; m++) {
                if (info[i].spoken === arr2[m]) {
                    $("#conversation" + num).get(0).selectedIndex = m;
                }
            }
            // if (i === 1) {
            //     for (var j = 0; j < arr.length; j++) {
            //         if (info[i].spoken === arr2[j]) {
            //             $("#certificate" + num).get(0).selectedIndex = j;
            //         }
            //     }
            // }

        }


        runDialogJS();
    });
}

function getData2() {
    $.get(ROOT + "/applicant/t5/data2", function (json) {
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

        /*  var arr = getSelectElements("other_skill1");

          for (var i = 0; i < info.length; i++) {
              if (i > 3) add_other_skill(info[i].name);


              var num = i + 1;
              $("#other_skill_name" + num).val(info[i].name);


              for (var m = 0; m < arr.length; m++) {
                  if (info[i].degree === arr[m]) {
                      $("#other_skill" + num).get(0).selectedIndex = m;
                  }
              }
          }*/

        let old_computer_skills = [];
        let isOldStyle = false;
        for (let i = 0; i < info.length; i++) {


            let num = i + 1;


            let skill_name = info[i].name;
            if (info[i].skillType == 'c' && skill_name != 'computer skill' && skill_name != 'other skill') {
                // console.log(info[i].skillType)
                isOldStyle = true;
                old_computer_skills.push(skill_name + ',' + info[i].degree + '\n');
            }
            // if (i > 0){
            //     $("#other_skill"  +num ).val(info[i].degree);
            // }
        }
        if (isOldStyle) {
            $("#other_skill1").val(old_computer_skills.join());
        } else {
            for (let i = 0; i < info.length; i++) {
                let num = i + 1;
                if (i < 2) {
                    $("#other_skill" + num).val(info[i].degree);
                }
            }

        }


        runDialogJS();
    });
}


function getSelectElements(selectName) {
    var array = []; //定義數組
    $("#" + selectName + " option").each(function () { //遍曆全部option
        var val = $(this).val();
        array.push(val);
    });
    return array;
}


function add_language(name, button) {
    if (name != "") {
        var num = document.getElementById('num').value;
        if (num == "")
            num = 2;
        var abc = document.getElementById('add_language_table');
        var rowCount = abc.rows.length;
        var newRow = abc.insertRow(rowCount++);
        newRow.style.valign = "bottom";

        newRow.insertCell(0).innerHTML = "<font class='info_title' id='span_title_lanauage_name" + num + "'>＊" + name + "</font>" +
            "<input type='hidden' id='lanauage_name" + num + "' name='lanauage_name" + num + "' value='" + name + "'>" +
            "<input type='hidden' id='language_count[]' name='language_count[]' value='" + num + "'/>" +
            "<input type='hidden' name='language_row_id" + num + "' id='language_row_id" + num + "' value='-1'>";
        // newRow.cells[0].style.height = '25';
        // console.log( newRow.cells[0])
        let lanauage_name = '#lanauage_name' + num;
        $(lanauage_name).parent().css('height', 50)
        // newRow.cells[0].style.colspan = '3';
        // newRow = abc.insertRow(rowCount++);
        newRow.insertCell(1).innerHTML =
            // "<font class='page_title_small_black' id='written_font" + num + "'>＊書寫</font><br>" +
            "<select class='info_select_small' name='written" + num + "' id='written" + num + "'>" +
            "<option value='-1'>請選擇</option>" +
            // "<option value='優'>優</option>" +
            // "<option value='好'>好</option>" +
            "<option value='流利'>流利</option>" +
            "<option value='一般'>一般</option>" +
            // "<option value='差'>差</option>" +
            "<option value='略懂'>略懂</option>" +
            "<option value='不適用'>不適用</option>" +
            "</select>";
        // newRow.cells[0].style.height = '35';
        newRow.cells[0].style.width = '150';
        newRow.insertCell(2).innerHTML =
            // "<font id='conversation_font" + num + "'>＊會話</font><br>" +
            "<select class='info_select_small' name='conversation" + num + "' id='conversation" + num + "'>" +
            "<option value='-1'>請選擇</option>" +
            // "<option value='優'>優</option>" +
            // "<option value='好'>好</option>" +
            "<option value='流利'>流利</option>" +
            "<option value='一般'>一般</option>" +
            // "<option value='差'>差</option>" +
            "<option value='略懂'>略懂</option>" +
            "<option value='不適用'>不適用</option>" +
            "</select>";
        // newRow.cells[1].style.height = '50';
        newRow.cells[1].style.width = '150';
        /* if (name == '英語') {
             newRow.insertCell(2).innerHTML = "<strong><font id='certificate_font" + num + "'>證書</font></strong><br><select name='certificate" + num + "' id='certificate" + num + "'><option value='-1'>請選擇</option><option value='CET4'>CET4</option><option value='CET6'>CET6</option><option value='PETS-1'>PETS-1</option><option value='PETS-1B'>PETS-1B</option><option value='PETS-2'>PETS-2</option><option value='PETS-3'>PETS-3</option><option value='PETS-4'>PETS-4</option><option value='PETS-5'>PETS-5</option><option value='其他'>其他</option></select>";
             newRow.cells[2].style.height = '50';
             newRow.cells[2].style.width = '281';
         }*/
        if (button == 'Y') {
            var name = 'add_language_table';
            var delete_name = 'delete_id';
            var row_id = "language_row_id";
            // var string = "<input type='button' value='刪除' id ='delete-A" + num + "' />";
            var string = '<div id="delete-A' + num +
                '" class="page_title_small" style="cursor: pointer;width: 50px;">－刪除</div> ';

            newRow.insertCell(3).innerHTML = string;
            //onclick='MydeleteRow(this.parentNode.parentNode.rowIndex," + name + "," + num + "," + row_id + "," + delete_name + ");'
            $('#delete-A' + num).on(
                {
                    click: function () {
                        MydeleteRow(this.parentNode.parentNode.rowIndex, name, $(this).attr('id').replace(/\D/g, ""), row_id, delete_name);
                    }
                }
            );


            newRow.cells[3].style.height = '40';
        } else newRow.insertCell(3).innerHTML = '&emsp;&emsp;&emsp;&emsp;&emsp;';
        document.getElementById('other_language').value = "";
        num++;
        document.getElementById('num').value = num;
        document.getElementById('show_other_language').style.display = 'none';
    } else alert("輸入不能為空");
}

/*
function add_other_skill(name) {
    if (name != null && name != "") {
        var num = document.getElementById('other_skill_num').value;
        var abc = document.getElementById('add_other_skill_table');
        var rowCount = abc.rows.length;
        var newRow = abc.insertRow(rowCount++);
        newRow.insertCell(0).innerHTML = "<strong><font id='other_skill_font" + num + "'>＊" + name + "</font></strong><input type='hidden' id='other_skill_name" + num + "' name='other_skill_name" + num + "' value='" + name + "'><input type='hidden' id='other_skill_count[]' name='other_skill_count[]' value='" + num + "'/><input type='hidden' name='other_skill_row_id" + num + "' id='other_skill_row_id" + num + "' value='-1'><br><select id='other_skill" + num + "' name='other_skill" + num + "'><option value='-1'>請選擇</option><option value='優'>優</option><option value='好'>好</option><option value='一般'>一般</option><option value='差'>差</option><option value='不適用'>不適用</option></select>"
        var name = "add_other_skill_table";
        var delete_name = "other_skill_delete_id";
        var row_id = "other_skill_row_id";
        var string = "<input type='button' value='刪除' id ='delete-B" + num + "'  />";
        newRow.insertCell(1).innerHTML = string;
        //onclick='MydeleteRow(this.parentNode.parentNode.rowIndex," + name + "," + num + "," + row_id + "," + delete_name + ");'
        $('#delete-B' + num).on(
            {
                click: function () {
                    MydeleteRow(this.parentNode.parentNode.rowIndex, name, $(this).attr('id').replace(/\D/g, ""), row_id, delete_name);
                }
            }
        );
        newRow.cells[1].style.height = '40';

        document.getElementById('other_skill_name').value = "";
        num++;
        document.getElementById('other_skill_num').value = num;
        document.getElementById('show_other_skill').style.display = 'none';

    } else alert("輸入不能為空");
}
*/


function MydeleteRow(num, name, id, row_id, delete_id) {
    var r = confirm("確定要刪除?");
    if (r == true) {
        row_name = row_id + id;
        abc = document.getElementById(row_name).value;
        if (abc > 0) {
            if (document.getElementById(delete_id).value == '') {
                document.getElementById(delete_id).value = abc;
            } else document.getElementById(delete_id).value = document.getElementById(delete_id).value + ',' + abc;
        }
        /*   if (name != 'add_other_skill_table') {
               document.getElementById(name).deleteRow(num - 1);
               document.getElementById(name).deleteRow(num - 1);
           }*/
        else document.getElementById(name).deleteRow(num);
        //alert(document.getElementById('delete_id').value);
        //var eduction=document.getElementById('num').value;
    }
}

function check(form, send_type) {
    var aa = document.getElementsByTagName('select');
    var bb = document.getElementById('add_language_table');
    // var cc = document.getElementById('add_other_skill_table');
    var is_ok = true;

    var font = document.getElementsByTagName('font');
    for (i = 0; i < font.length; i++) {
        font[i].color = 'black';
    }

    for (i = 0; i <= document.getElementById('num').value; i++) {
        let written_name = 'written' + i;
        // written_font = 'written_font' + i;
        let conversation_name = 'conversation' + i;
        // conversation_font = 'conversation_font' + i;
        let span_title_lanauage_name = 'span_title_lanauage_name' + i;
        /*    if (document.getElementById(written_name)) {
                if (document.getElementById(written_name).value == '-1') {
                    document.getElementById(written_font).color = 'red';
                    is_ok = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }
            }

            if (document.getElementById(conversation_name)) {
                if (document.getElementById(conversation_name).value == '-1') {
                    document.getElementById(conversation_font).color = 'red';
                    is_ok = false;
                    document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
                }
            }*/

        if (document.getElementById(written_name)) {
            if (document.getElementById(written_name).value == '-1') {
                document.getElementById(span_title_lanauage_name).color = 'red';
                is_ok = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }
        }

        if (document.getElementById(conversation_name)) {
            if (document.getElementById(conversation_name).value == '-1') {
                document.getElementById(span_title_lanauage_name).color = 'red';
                is_ok = false;
                document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
            }
        }


    }


    /*   for (i = 0; i <= document.getElementById('other_skill_num').value; i++) {
           other_skill_name = 'other_skill' + i;
           other_skill_font = 'other_skill_font' + i;
           if (document.getElementById(other_skill_name)) {
               if (document.getElementById(other_skill_name).value == '-1') {
                   document.getElementById(other_skill_font).color = 'red';
                   is_ok = false;
                   document.getElementById("error").innerHTML = "<font color='red'>紅色部份為沒填寫或者格式錯誤</font><br>";
               }
           }
       }*/


    if (!is_ok){
        $('.t1').css('height','421px')
        if (send_type === 'dialog_saveAndGo') {
            let dialog = $('dialog')[0];
            dialog.close();
            alert("儲存失敗, 紅色部份為沒填寫或者格式錯誤");
        }
    }

    if (is_ok == true) {

        submit(send_type);
    }
}

function submit(send_type) {
    // form.submit();
    document.getElementById('type').value = 'language';

    var url = ROOT + "/applicant/top5";
    var data = $("#form-1").serialize();


    $.ajax({
        "url": url,
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {
                if (send_type == 'onlySave') {
                    alert("儲存成功")
                }
                else if (send_type=='dialog_saveAndGo'){
                    location.href = CURRENT_GOTO;
                }
                else {
                    location = ROOT + "/hr_top7.html";
                }
            } else {
                if (json.state !== 999 && json.state !== 622)
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
