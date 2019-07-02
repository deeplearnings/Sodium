/**
 * 生成代码
 * @param id
 */
function generatedCode(id) {
    var disIputs = $('input[disabled=disabled]');
    $(disIputs).removeAttr("disabled");
    var parent = $('#DataFrom').serializeJson();
    if(typeof(id) == 'undefined' || id == null){
        var id = $('#entityId').val();
    }
    var url = "/databasetable/generate"
    var completeHandler = function (data) {
        alert("提示",data.msg,"好的")
    };
    doPost(url,{id:id},completeHandler)
}