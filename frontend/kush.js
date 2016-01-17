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
        
function readURL(input) {
    
}

function generateCaptions() {
    <!-- Do Some Outsourcing Shit Here -->
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