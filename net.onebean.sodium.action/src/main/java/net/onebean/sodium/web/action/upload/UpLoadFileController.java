package net.onebean.sodium.web.action.upload;

import net.onebean.component.aliyun.CkEditerUploadCallBackVo;
import net.onebean.component.aliyun.UploadCallBackVo;
import net.onebean.sodium.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


/**
 * CkEditor上传文件到oss Controller
 * @author 0neBean
 */
@RequestMapping("upload")
@Controller
public class UpLoadFileController {

    @Autowired
    private UploadService uploadService;

    @RequestMapping("ckEditorUploadImg")
    @ResponseBody
    public CkEditerUploadCallBackVo fileBrowseUrl(@RequestParam("upload") MultipartFile file) throws Exception{
        return uploadService.ckEditorUploadImg(file);
    }

    /**
     * 上传文件控件上传接口
     */
    @RequestMapping("uploadMultipartFile")
    @ResponseBody
    public UploadCallBackVo uploadMultipartFile(MultipartFile file){
        return uploadService.uploadMultipartFile(file);
    }

    /**
     * 根据URL下载文件再上传
     */
    @RequestMapping("downfile")
    public ResponseEntity<byte[]> uploadMultipartFile(@RequestParam("store")String store) throws Exception{
        return uploadService.uploadFromUrl(store);
    }




}

