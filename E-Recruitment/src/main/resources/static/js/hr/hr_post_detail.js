function showPosition() {
    var url = ROOT + "/positionDesc";
    var data = location.search.substring(1);
    $.ajax({
        "url": url,
        "type": "get",
        data: data,
        "dataType": "json",
        "success": function (json) {
            if (json.state !== 200) {
                alert(json.message);
                return;
            }
            $(".center").empty();
            var list = json.data;

            if (list.length == 0) {
                return;
            }


            var html = list[0].jobDesc;
            html += "<table width=\"764\" height=\"270\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" >" +
                "<tr>" +
                "<td valign=\"top\" class=\"bg\"> <br>" +
                "</td>" +
                "</tr>" +
                "</table>";


            $(".center").append(html);


        }
    });

}


$(function () {
    if (window.$ && window.$.ajaxSetup) {
        $.ajaxSetup({
            "contentType": "application/x-www-form-urlencoded;charset=utf-8",
            "dataType": "json"
        });
    }
    // showPosition();

    $('.self_applicant_a').on({
        click: function () {

            let suspendAllApplicant = $('#suspendAllApplicant').val();
            if (suspendAllApplicant == 'true') {
                alert('目前您已有三個應徵申請處理中,請待工作人員處理完再申請')
                return false;
            }

            let deptPosDetailId = $('#deptPosDetailId').val();
            let url = $(this).data('thref') + deptPosDetailId;

            $.ajax({
                "url": ROOT + '/applicant/checkLogin',
                "type": "post",
                "data": {'c': 'c'},
                "dataType": "json",
                "success": function (json) {
                    if (json.state === 200) {
                        window.opener.location.href = ROOT + url;
                        window.close()

                    } else if (json.state === 620 || json.state === 611) {
                        alert("請先登錄或註冊個人資料")
                        $.cookie('GoBackToPostListHtmlStatus', deptPosDetailId, {
                            path: "/",
                        });
                        window.opener.location.href = ROOT + "/hr_top_m1.html";
                        window.close()
                    } else if (json.state === 630) {
                        alert("請先補齊個人資料")
                        window.opener.location.href = ROOT + "/hr_top1.html";
                        window.close()
                    }
                }
            });
        }
    })
});



