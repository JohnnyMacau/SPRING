//函数对象App
var App = function () {
    /*
     * 初始化DataTables
    * */
    let handlerInitDataTables = function (url, columns, sidx, sord, aoColumnDefs) {
        /* let aoColumnDefs = [
             {"bSortable": false, "aTargets": [8]},
             {"bSortable": false, "aTargets": [0]}
         ];*/
        let dataTable = $("#tableList").DataTable({
            "bAutoWidth": false,

            "bPaginate": true,
            "bLengthChange": true,
            "aLengthMenu": [5, 10, 25, 50, 100, 200, 300],
            // dom: 'ftrpli',
            dom: "ftr<'self_info'il><'self_paginate'p>",
            //l- 每页显示数量
            //f - 过滤输入
            //t - 表单Table
            //i - 信息
            //p - 翻页
            //r - pRocessing

            "paging": true,
            "pageLength": 10,

            // "bSort" : true,





            "bordering": true,

            "stateSave": false,
            "processing": true,
            "searching": false,
            "deferRender": true,
            "serverSide": true,//true代表后台处理分页，false代表前台处理分页
            "aoColumnDefs": aoColumnDefs,
            "aaSorting": [[sidx, sord]],
            "ajax": {
                "url": url,
                "data": getParams()
            },
            "columns": columns,


            "language": {
                "sProcessing": "查詢中...",
                "sLengthMenu": "每頁 _MENU_ 條",
                "sZeroRecords": "沒有數據",
                "sInfo": "第 _PAGE_ 頁 / 總 _PAGES_ 頁       &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp &nbsp            當前第 _START_ - _END_ 條 / 共計 _TOTAL_ 條",
                "sInfoEmpty": "沒有數據",
                "sInfoFiltered": "(從 _MAX_ 條記錄中過濾)",
                "sInfoPostFix": "",
                "sSearch": "檢索:",
                "sUrl": "",
                "sEmptyTable": "表中數據為空",
                "sLoadingRecords": "加載中...",
                "sInfoThousands": ",",
                "oPaginate": {
                    "sFirst": "首頁",
                    "sPrevious": "上一頁",
                    "sNext": "下一頁",
                    "sLast": "尾頁"

                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },


        });


        return dataTable;
    };
    return {
        //初始化DataTables
        initDataTables: function (url, columns, sidx, sord, aoColumnDefs) {
            let handlerInitDataTables1 = handlerInitDataTables(url, columns, sidx, sord, aoColumnDefs);
            setRowEvent();
            return handlerInitDataTables1;
        }
    }
}();
//懸浮事件不用綁定,直接用table-hover即可, 同時在 table.dataTable tbody tr:hover 設定顏色
function setRowEvent() {
    //DataTable 行悬浮事件，用来设置样式悬浮事件
    /* $('.table-bordered tbody').on('mouseover', 'tr', function () {
         if (!$(this).hasClass('hoverd')) {
             $(this).addClass('hoverd');
         }
     }).on('mouseleave', 'tr', function () {
         if ($(this).hasClass('hoverd')) {
             $(this).removeClass('hoverd');
         }
     });*/


    //DataTable 行点击事件，用来设置样式点击事件
    $('.table-bordered tbody').on('click', 'tr', function () {
        // console.log('sssss')
        if ($(this).hasClass('selected')) {
            $(this).removeClass('selected');
        } else {
            dataTable.$('tr.selected').removeClass('selected');
            $(this).addClass('selected');
        }
    });
}
