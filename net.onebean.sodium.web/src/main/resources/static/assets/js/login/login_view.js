$(function(){
    if (window.frames.length != parent.frames.length) {
        parent.location.reload();
    }
    $("#loginFrom").validate({
        rules: {
            username: {
                required:true,
                noChinese:true
            },
            password:{
                required:true,
                noChinese:true
            }
        }
    });
});


