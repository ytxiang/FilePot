$.ajaxSetup({
    headers: {
        'X-CSRF-TOKEN': $("#csrf_token").attr("content")
    }
});

$(function () {
    fileUpload('ffsubmit');
    fileUpdate('refresher');
    fileDelete('trash');
});

function gonext(data) {
    if (data) {
        alert(data);
    } else {
        window.location.reload();
    }
}

function fileUpload(name) {
    $("#" + name).click(function (event) {
        event.preventDefault();
        var form = $('#fileform')[0];
        var file = $("#file")[0].files[0]
        var data = new FormData(form);
        if (file == null) {
            alert("No file to upload");
            return;
        }
        if (file.size > 10485760) {
            alert("Failed to upload! File size must be no more than 10MB!");
            return;
        }
        $.ajax({
            type: 'POST',
            url: "/file/upload",
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            success: function (data) {
                window.location.reload();
            },
            error: function (data) {
                window.location.reload();
            }
        })
    })
}

function fileUpdate(name) {
    $("span[name='" + name + "']").click(function () {
        var form = $('#fileform')[0];
        var data = new FormData(form);
        var file = $("#file")[0].files[0];
        var msgstr = "No file has been selected.\n" +
                     "Are you sure to continue updating? " +
                     "Doing so will purge the original file."

        if (file == null && false == confirm(msgstr)) {
		return;
        }
        if (file != null && file.size > 10485760) {
            alert("Failed to upload! File size must be no more than 10MB!");
            return;
        }
       var opts = {
            type: 'POST',
            method: 'POST',
            url: $(this).attr("data"),
            data: data,
            cache: false,
            contentType: false,
            processData: false,
            success: gonext
        }
        /*
        // Old browser emulation
        if (data.fake) {
            // Make sure no text encoding stuff is done by xhr
            opts.xhr = function () {
                var xhr = jQuery.ajaxSettings.xhr();
                xhr.send = xhr.sendAsBinary;
                return xhr;
            }
            opts.contentType = "multipart/form-data; boundary=" + data.boundary;
            opts.data = data.toString();
        }
        */
        $.ajax(opts);
    })
}

function fileDelete(name) {
    $("span[name='" + name + "']").click(function () {
        if (false == confirm("Are you sure to delete ?"))
            return;
        $.ajax({
            type: 'DELETE',
            url: $(this).attr("data"),
            success: gonext
        })
    })
}
