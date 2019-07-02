package net.onebean.saas.portal.web.action.upload;

import com.eakay.util.PropUtil;
import net.onebean.component.aliyun.AliyunOssUtil;
import net.onebean.component.aliyun.CkEditerUploadCallBackVo;
import net.onebean.component.aliyun.UploadCallBackVo;
import net.onebean.component.aliyun.image.ImageUtil;
import net.onebean.component.aliyun.image.ImageZipUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * CkEditer上传文件到oss Controller
 * @author 0neBean
 */
@RequestMapping("upload")
@Controller
public class UpLoadFileController {

    @RequestMapping("filebrowserurl")
    @ResponseBody
    public CkEditerUploadCallBackVo filebrowserBrowseUrl(@RequestParam("upload") MultipartFile file, HttpServletRequest request, CkEditerUploadCallBackVo vo) throws Exception{
        String contentType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1,file.getOriginalFilename().length());
        String beforeImagePath = ImageUtil.imageName()+"."+contentType;
        String afterImagePath = ImageUtil.imageName()+"."+contentType;
        contentType = AliyunOssUtil.contentType_map.get(contentType);
        String localPath =  request.getServletContext().getRealPath("/upload/");
        String allPath = localPath+beforeImagePath;
        ImageUtil.savePicToDisk(file.getInputStream(),localPath,beforeImagePath);
        if (ImageUtil.checkImage(allPath)){
            allPath = zipImage(allPath,localPath+afterImagePath);
        }else{
            afterImagePath = beforeImagePath;
        }
        String ossPath;
        if(allPath.contains("\\")){
            ossPath = allPath.substring(allPath.indexOf("\\upload\\")+1,allPath.length()).replace("\\", "/");
        }else{
            ossPath = allPath.substring(allPath.indexOf("/upload/")+1,allPath.length());
        }
        String bucketName =  PropUtil.getInstance().getConfig("aliyun.oss.bucketName",PropUtil.PUBLIC_CONF_ALIYUN_OSS);
        String ossHost =  PropUtil.getInstance().getConfig("aliyun.oss.host",PropUtil.PUBLIC_CONF_ALIYUN_OSS);
        AliyunOssUtil.uploadFile(bucketName,ossPath,allPath,contentType);
        vo.setUploaded(1);
        vo.setFileName(afterImagePath);
        vo.setUrl(ossHost+ossPath);
        return vo;
    }

    /**
     * 上传文件控件上传接口
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("uploadmultipartfile")
    @ResponseBody
    public UploadCallBackVo uploadMultipartFile(MultipartFile file, HttpServletRequest request, UploadCallBackVo vo) throws Exception{
        Map<String,Object> data = new HashMap<>();
        try {
            String ossHost = PropUtil.getInstance().getConfig("aliyun.oss.host",PropUtil.PUBLIC_CONF_ALIYUN_OSS);
            String contentType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1,file.getOriginalFilename().length());
            String beforeImagePath = ImageUtil.imageName()+"."+contentType;
            String afterImagePath = ImageUtil.imageName()+"."+contentType;
            contentType = AliyunOssUtil.contentType_map.get(contentType);
            String localPath =  request.getServletContext().getRealPath("/upload/");
            String allPath = localPath+beforeImagePath;
            ImageUtil.savePicToDisk(file.getInputStream(),localPath,beforeImagePath);
            if (ImageUtil.checkImage(allPath)){
                allPath = zipImage(allPath,localPath+afterImagePath);
            }else{
                afterImagePath = beforeImagePath;
            }
            String ossPath;
            if(allPath.contains("\\")){
                ossPath = allPath.substring(allPath.indexOf("\\upload\\")+1,allPath.length()).replace("\\", "/");
            }else{
                ossPath = allPath.substring(allPath.indexOf("/upload/")+1,allPath.length());
            }
            String bucketName =  PropUtil.getInstance().getConfig("aliyun.oss.bucketName",PropUtil.PUBLIC_CONF_ALIYUN_OSS);
            AliyunOssUtil.uploadFile(bucketName,ossPath,allPath,contentType);
            File uploadFile = new File(allPath);
            if(uploadFile.exists() && uploadFile.isDirectory()){
                uploadFile.deleteOnExit();
            }
            data.put("id",ossHost+ossPath);
            data.put("name",afterImagePath);
        } catch (Exception e) {
            e.printStackTrace();
            vo.setStatus(false);
            vo.setMessage("上传失败!");
        }
        vo.setStatus(true);
        vo.setMessage("上传成功!");
        vo.setData(data);
        return vo;
    }

    /**
     * 上传文件空间上传接口
     * @param store
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping("downfile")
    public ResponseEntity<byte[]> uploadMultipartFile(@RequestParam("store")String store, HttpServletRequest request) throws Exception{
        String filename = store.substring(store.lastIndexOf("/"),store.length()).replace("/","");
        String bucketName = PropUtil.getInstance().getConfig("aliyun.oss.bucketName",PropUtil.PUBLIC_CONF_ALIYUN_OSS);
        String ossHost = PropUtil.getInstance().getConfig("aliyun.oss.host",PropUtil.PUBLIC_CONF_ALIYUN_OSS);
        String key = store.replace(ossHost,"");
        String localPath =  request.getServletContext().getRealPath("/dowmload/");
        String filePath = localPath+key;
        filePath = filePath.replace("/","\\");
        filePath = filePath.substring(0,filePath.lastIndexOf("\\"));
        filePath+="\\";
        File path = new File(filePath);
        File file = new File(filePath+filename);
        path.mkdirs();
        file.createNewFile();
        AliyunOssUtil.downloadFile(bucketName,key,filePath+filename);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file),headers,HttpStatus.CREATED);
    }

    /**
     * 压缩图片质量
     * @param beforePath 压缩前文件路径
     * @param afterPath 压缩后文件路径
     * @return 返回压缩后文件路径
     */
    private String zipImage(String beforePath,String afterPath){
        File before = new File(beforePath);
        File after = new File(afterPath);
        ImageZipUtil.zipImageFile(before,after,ImageUtil.getImgWidth(before),ImageUtil.getImgHeight(before),Float.valueOf(PropUtil.getInstance().getConfig("aliyun.oss.zipimg.limit.quality",PropUtil.PUBLIC_CONF_ALIYUN_OSS)));
        if(before.exists() && before.isDirectory()){
            before.deleteOnExit();
        }
        return after.getPath();
    }


}
