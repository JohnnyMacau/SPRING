$(function () {
    $('[name="logout"]').on(
        {
            click: function () {
            	url='applicant/delete/session';
                $.ajax({
                    "url": url,
                    "type": "post",
                    "dataType": "json",
                    "success": function (json) {
                        if (json.state === 200) {
                        	location = "hr_top_m1.html";
                        }
                        else {
                            alert(json.message);
                        }
                    }
                });
            }
        }
    );
});
