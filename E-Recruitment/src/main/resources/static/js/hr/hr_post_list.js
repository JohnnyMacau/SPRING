$(function () {
    if (window.$ && window.$.ajaxSetup) {
        $.ajaxSetup({
            "contentType": "application/x-www-form-urlencoded;charset=utf-8",
            "dataType": "json"
        });
    }
    showJobList();
});

function showJobList() {
    var url = ROOT + "/positionDetail";
    $.ajax({
        "url": url,
        "type": "get",
        "dataType": "json",
        "success": function (json) {
            if (json.state !== 200) {
                return;
            }
            $("#table_ajax").empty();
            var list = json.data.deptPositionDetailVOList;
            if (list.length == 0) {
                $("#table_ajax").append('暫沒有職位空缺');
                return;
            }
            var $department_name = "";
            var html = "";


            // for (var j = 0; j < list.length; j++) {
            //     var btn =  $('#btn-a'+j);
            //     $("#table_ajax").on('click', btn, function(){alert()});
            //       // btn.on("click",function () { this.style.cursor='hand'  })
            //
            //     // btn.click( showlist(j));
            // }

            let colorFlag = true;
            let applicantPositionList = json.data.applicantPositionList;

            for (var i = 0; i < list.length; i++) {
                let thisOneLock = false;
                for (let j = 0; j < applicantPositionList.length; j++) {
                    if (list[i].deptPosDetailId === applicantPositionList[j].deptPosDetailId) {
                        thisOneLock = true;
                        break;
                    }
                }
                if ($department_name != list[i].description) {

                    if (i !== 0) {
                        html += "</table></td></tr>";
                    }
                    html += "<tr id='btn-a#{i}' class='#{class}'   style=\"cursor: pointer;\">"
                        + "<td ><table cellpadding='0' cellspacing='0'>"
                        + "<tr valign='middle'>"
                        + "<td width='23'><img name='picture#{i}' id='picture#{i}' src='#{arrow}.gif' width='23' height='22' /></td>"
                        + "<td class='title' valign='bottom'><strong>&nbsp;&nbsp;#{description}</strong></td>"
                        + "</tr>"
                        + "</table></td>"
                        + "</tr>"
                        + "<tr>"
                        + "<td id='position#{i}' style='display:none'><table width='800' cellpadding='0' cellspacing='0'>"
                        + "<tr valign='middle'>"
                        + "<td width='23'>&nbsp;</td>"
                        + "<td width='130' height='25' valign='middle' class='word'>#{job_title}</td>"
                        + "<td width='163' valign='middle' class='word'>"
                        + "<div align='center'>(REF:#{job_code})</div></td>"
                        + "<td width='300' valign='middle' class='word'>"
                        + "<span align='right'>"
                        + "<input class='button' type='submit' name='Submit' value='職位詳情' id='btn-b#{i}' detail-id ='#{dept_pos_detail_id}'>"

                        + "<span>&emsp;</span>"
                        + (thisOneLock ? "<a class=\"self_disabled_btn_style\" title=\"您已申請此職位\" >應徵此職位</a>" :
                                "<a class='self_applicant_a button' href='#' data-thref='/hr_top2.html/#{dept_pos_detail_id}' data-deptposdetailid='#{dept_pos_detail_id}'>應徵此職位</a>"
                        )
                        + "</span></td>"
                        + "</tr>";
                    if (colorFlag) {
                        html = html.replace(/#{class}/g, "brown");
                        html = html.replace(/#{arrow}/g, ROOT + "/images/brown_arrow1");
                    } else {
                        html = html.replace(/#{class}/g, "green");
                        html = html.replace(/#{arrow}/g, ROOT + "/images/brown_arrow2");
                    }
                    colorFlag = !colorFlag;

                    $department_name = list[i].description;
                } else {
                    html += "<tr valign='middle'>"
                        + "<td width='23'>&nbsp;</td>"
                        + "<td width='130' height='25' valign='middle' class='word'>#{job_title}</td>"
                        + "<td width='163' valign='middle' class='word'>"
                        + "<div align='center'>(REF:#{job_code})</div></td>"
                        + "<td width='300' valign='middle' class='word'>"
                        + "<span align='right'>"
                        + "<input class='button' type='submit' name='Submit' value='職位詳情' id='btn-b#{i}'  detail-id ='#{dept_pos_detail_id}'>"

                        + "<span>&emsp;</span>"
                        + (thisOneLock ? "<a class=\"self_disabled_btn_style\" title=\"您已申請此職位\" >應徵此職位</a>" :
                                "<a class='self_applicant_a button' href='#' data-thref='/hr_top2.html/#{dept_pos_detail_id}' data-deptposdetailid='#{dept_pos_detail_id}'>應徵此職位</a>"
                        )
                        + "</span></td>"
                        + "</tr>";

                }

                html = html.replace(/#{i}/g, i);
                html = html.replace(/#{description}/g, list[i].description);
                html = html.replace(/#{job_title}/g, list[i].jobTitle);
                html = html.replace(/#{job_code}/g, list[i].jobCode);
                html = html.replace(/#{dept_pos_detail_id}/g, list[i].deptPosDetailId);


            }
            html += "</table></td></tr>";
            $("#table_ajax").append(html);

            let suspendAllApplicant = json.data.suspendAllApplicant;

            $('.self_applicant_a').on({
                click: function () {
                    if (suspendAllApplicant) {
                        alert('目前您已有三個應徵申請處理中,請待工作人員處理完再申請')
                        return false;
                    }
                    let url = $(this).data('thref');
                    let deptPosDetailId = $(this).data('deptposdetailid');

                    $.ajax({
                        "url": ROOT + '/applicant/checkLogin',
                        "type": "post",
                        "data": {'c': 'c'},
                        "dataType": "json",
                        "success": function (json) {
                            if (json.state === 200) {
                                location.href = ROOT + url;
                            } else if (json.state === 620 || json.state === 611) {
                                alert("請先登錄或註冊個人資料")
                                $.cookie('GoBackToPostListHtmlStatus', deptPosDetailId, {
                                    path: "/",
                                });
                                location.href = ROOT + "/hr_top_m1.html";
                            } else if (json.state === 630) {
                                alert("請先補齊個人資料")
                                location.href = ROOT + "/hr_top1.html";
                            }
                        }
                    });
                }
            })


            for (var j = 0; j < list.length; j++) {
                var btn1 = $('#btn-a' + j);
                btn1.on(
                    {
                        click: function () {
                            showlist($(this).attr('id').replace(/\D/g, ""));
                        }
                    }
                );
                var btn2 = $('#btn-b' + j);
                btn2.on(
                    {
                        click: function () {
                            showdetail($(this).attr('detail-id'));
                        }
                    }
                )

            }

        }
    });
}


function showlist(i) {
    // alert("i=" + i)
    var name = 'position' + i;
    var abc = document.getElementById(name);
    if (abc.style.display == "") {
        name = 'picture' + i;
        var d = document.getElementById(name);

        if (d.src == FULL_ROOT + "/images/brown_arrow1a.gif")
            d.setAttribute('src', ROOT + "/images/brown_arrow1.gif");
        else if (d.src == FULL_ROOT + "/images/brown_arrow2a.gif")
            d.setAttribute('src', ROOT + "/images/brown_arrow2.gif");

        abc.style.display = "none";
    } else {
        name = 'picture' + i;
        var d = document.getElementById(name);
        // alert(d.src)
        if (d.src == FULL_ROOT + "/images/brown_arrow1.gif") {
            d.setAttribute('src', ROOT + "/images/brown_arrow1a.gif");
        } else if (d.src == FULL_ROOT + "/images/brown_arrow2.gif")
            d.setAttribute('src', ROOT + "/images/brown_arrow2a.gif");

        abc.style.display = "";
    }
    // alert(""+d.src)

}

var detail_windowObj;

function showdetail(post_id) {
    var url = ROOT + '/hr_post_detail.html?post_id=' + post_id;
    // alert(url)
    detail_windowObj = window.open(url, "newwindow",
        "height=600, width=764, toolbar =no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no");
}
