
if($.fn.dataTable){
	$.fn.dataTable.ext.errMode = function (settings, tn, msg) {
		console.log(msg);

	    if (msg.indexOf("Invalid JSON response") != -1) {
	      toastr.warning('會話過期');
	      setTimeout(function () {
//	    	  window.location = "[[@{/dispatch/main}]]";
	          window.location = "/dispatch/main";
	      }, 1500);
	    }
	  }
}

var title = '';
$().ready(function(){
	var url = window.location.pathname;
	console.log(url);
	var menu_item = "";

//setting
	if(url.includes("/dispatch/setting/sourceType")){
		menu_item = "職位空缺來源選項";
	}
	if(url.includes("/dispatch/setting/editSourceType")){
		menu_item = "職位空缺來源選項";
	}

//job
	if(url.includes("/dispatch/job/search")){
		menu_item = "管理職位";
	}
	if(url.includes("/dispatch/job/edit")){
		menu_item = "管理職位";
		title = "編輯職位";
	}
	if(url.includes("/dispatch/job/add")){
		menu_item = "開啟新空缺";
		title = "開啟新職位";
		console.log(menu_item);
	}
	if(url.includes("/dispatch/job/draft")){
		menu_item = "職位草稿";
	}
	if(url.includes("/dispatch/job/editDraft")){
		menu_item = "職位草稿";
		title = "編輯職位草稿";
	}
	if(url.includes("/dispatch/job/template")){
		menu_item = "職位範本";
	}
	if(url.includes("/dispatch/job/editTemplate")){
		menu_item = "職位範本";
		title = "編輯職位範本";
	}
	if(url.includes("/dispatch/job/loadTemplate")){
		menu_item = "開啟新空缺";
		title = "另存為新職位";
	}
	if(url.includes("/dispatch/job/closed")){
		menu_item = "已關閉職位";
	}
	if(url.includes("/dispatch/job/opening")){
		menu_item = "開啟中職位";
	}
	if(url.includes("/dispatch/job/show")){
		menu_item = "已關閉職位";
		title = "空缺內容";
	}

//application
	if(url.includes("/dispatch/application/search")){
		menu_item = "尋找職位申請";
	}
	if(url.includes("/dispatch/profile/show")){
		menu_item = "尋找職位申請";
	}

	if(url.includes("/dispatch/application/inbox")){
		menu_item = "履曆收件箱";
		title = "履曆收件箱";
	}

	if (title) {
		$("#title").html(title);
	}


});
