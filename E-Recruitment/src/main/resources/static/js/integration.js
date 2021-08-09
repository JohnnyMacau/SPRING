// function URLPath(msg){
//     var pathName = window.document.location.pathname;
//     var projectName = pathName.substring(0,pathName.substr(1).indexOf('/')+1);
//     return projectName+msg;
// }

function getProjectName() {
    var pathName = window.document.location.pathname;
    return pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
}

var ROOT = getProjectName();

function getRootPath() {
    //获取当前网址，如： http://localhost:8088/test/test.jsp
    var curPath = window.document.location.href;
    //获取主机地址之后的目录，如： test/test.jsp
    var pathName = window.document.location.pathname;
    var pos = curPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8088
    var localhostPath = curPath.substring(0, pos);
    //获取带"/"的项目名，如：/test
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPath + projectName);//发布前用此
}

var FULL_ROOT = getRootPath();

// $(function() {
//     //初始化默认缩放级别
//     var zoomNum = 1.1;
//     var $body = $('.center');
//     //页面放大函数
//     function PageBig() {
//         zoomNum += 0.1;
//         //兼容firefox浏览器代码
//         //需要transform-origin:center top设置，否则页面顶部看不到了
//         $body.css({
//             '-moz-transform': 'scale(' + zoomNum + ')',
//             'transform-origin': 'center top'
//         });
//         $body.css('zoom', zoomNum)
//     }
//     //页面缩小函数
//     function PageSmall() {
//         zoomNum -= 0.1;
//         //兼容firefox浏览器代码
//         //需要transform-origin:center top设置，否则页面顶部看不到了
//         $body.css({
//             '-moz-transform': 'scale(' + zoomNum + ')',
//             'transform-origin': 'center top'
//         });
//         $body.css('zoom', zoomNum);
//     }
//     // $('#pageBig').click(function() {
//         PageBig();
//     // });
//     // $('#pageSmall').click(function() {
//         PageSmall();
//     // });
// })


function alertUserByTag(data) {
    if (data['tag'] === 'reLogin' ||
        data['tag'] === 'firstLogin') {
        alert('會話過期');
    } else if (data['tag'] === 'inRegister') {
        alert('填表超時,未完成註冊,資料會流失');
    }
}

function checkSessionTimeoutAndJump(data) {
    $.ajax({
        "url": ROOT + '/applicant/checkSessionTimeout',
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {

                let incompletePageNoStr = $('#incompletePageNo').val();
                if (!!incompletePageNoStr && getNum(CURRENT_GOTO) <= Number(incompletePageNoStr)) {
                    location.href = CURRENT_GOTO;
                } else {
                    alert('請先完成當前頁面,並點擊下方的下一步跳轉');
                }

            } else if (json.state === 620) {
                alertUserByTag(data);
                location.href = ROOT + "/hr_top_m1.html";

            }
        }
    });
}

function checkSessionTimeoutAndJump2(data, url) {
    $.ajax({
        "url": ROOT + '/applicant/checkSessionTimeout',
        "type": "post",
        "data": data,
        "dataType": "json",
        "success": function (json) {
            if (json.state === 200) {

                location.href = url;

            } else if (json.state === 620) {
                alertUserByTag(data);
                location.href = ROOT + "/hr_top_m1.html";
            }
        }
    });
}


/**
 * 獲取字串中的數字
 * @param str
 */
function getNum(str) {
    // console.log(str)
    return Number(str.replace(/[^0-9]/ig, ""));
}

function checkTagAndAddTagData(isLogin, reLogin) {
    if (reLogin === true) {
        return {'tag': 'reLogin'}
    } else if (isLogin === true) {
        return {'tag': 'firstLogin'};//既有 islogin 而又沒有 relogin , 就是第一次開戶自動登錄
    } else {
        return {'tag': 'inRegister'};
    }
}

function alertMessage() {
    let tagData = checkTagAndAddTagData($('#tab').data('islogin'), $('#tab').data('reLogin'));
    alertUserByTag(tagData);
}
