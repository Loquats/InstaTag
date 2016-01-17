$(document).ready(function(){
    $("#fileupload").change(function(){
        var file = this.files[0];
        var size = file.size;
        if(size > 0) {
            var reader = new FileReader();
            reader.onload = function(e) {
                $('#main-img')
                    .attr('src', e.target.result)
                    .width(400);
            };
            reader.readAsDataURL(file);
            startUpload( 'tag', new FormData($('#uploadform')[0]) );
        }
    });
    $("#url-field").hide();
    $("#caption").hide();
    $("#progressbar").hide();
    $("#upload-row").show();
    $("#btn-url").click(function(){
        $("#url-field").toggle();
    });
    $("#uploadurl").click(function(){
        startUpload( 'tagurl', $("#mediaurl").val() );
        $('#main-img').attr('src', $("#mediaurl").val());
    });
});

function hashtag(arr){
    return "#" + arr.join([separator = ', #']);
}

function startUpload(action, formData){
    $("#progressbar").show();
    $("#url-field").hide();
    $("#upload-row").hide();
    $("#caption").hide();
    $.ajax({
        url: action,  //Server script to process data
        type: 'POST',
        dataType : "json",
        xhr: function() {  // Custom XMLHttpRequest
            var myXhr = $.ajaxSettings.xhr();
            if(myXhr.upload){ // Check if upload property exists
                myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // For handling the progress of the upload
            }
            return myXhr;
        },
        //Ajax events
        success: function(data){
            $("#caption").show();
            $("#popular").text(hashtag(data[0]));
            $("#eccentric").text(hashtag(data[1]));
            $("#common").text(hashtag(data[2]));
            console.log(data);
        },
        error: function( xhr, status, errorThrown ){
            alert( "Request failed" );
            console.log( "Error: " + errorThrown );
            console.log( "Status: " + status );
            console.dir( xhr );
        },
        complete : function(){
            $("#progressbar").hide();
            $("#upload-row").show();
        },
        // Form data
        data: formData,
        //Options to tell jQuery not to process data or worry about content-type.
        cache: false,
        contentType: false,
        processData: false
    });
}

function progressHandlingFunction(e){
    if(e.lengthComputable){
        var valeur = e.loaded/e.total * 100;
        $('.progress-bar').css('width', valeur+'%').attr('aria-valuenow', valeur);
    }
}

function readFile(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function(e) {
            $('#main-img')
                .attr('src', e.target.result)
                .width(400)
                .height(400);
        };
        document.getElementById("main-img").style.display = "";
        document.getElementById("upload-row").style.display = "none";
        document.getElementById("processing-row").style.display = "";
        reader.readAsDataURL(input.files[0]);
    }
}
        

function generateCaptions() {
    document.getElementById("caption").style.display = "";
    document.getElementById("processing-row").style.display = "none";
    document.getElementById("btn-refresh").style.display = "";
    document.getElementById("bar-div").style.display = "";
    var div = document.getElementById('caption');
    div.innerHTML = div.innerHTML + "Sample Caption";
}

function cancel() {
    document.getElementById("upload-row").style.display = "";
    document.getElementById("processing-row").style.display = "none";
    location.reload();
}

function refresh() {
    location.reload();
}