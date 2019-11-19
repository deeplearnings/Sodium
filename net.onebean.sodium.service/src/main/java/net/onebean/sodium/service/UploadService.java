package net.onebean.sodium.service;

import net.onebean.component.SpringUtil;
import net.onebean.component.aliyun.AliyunOssUtil;
import net.onebean.component.aliyun.CkEditerUploadCallBackVo;
import net.onebean.component.aliyun.UploadCallBackVo;
import net.onebean.util.ImageUtil;
import net.onebean.util.PropUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class UploadService {

    public CkEditerUploadCallBackVo ckEditorUploadImg(MultipartFile file) throws Exception{
        CkEditerUploadCallBackVo vo = new CkEditerUploadCallBackVo();
        String contentType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
        String beforeImagePath = ImageUtil.imageName()+"."+contentType;
        String afterImagePath = ImageUtil.imageName()+"."+contentType;
        contentType = AliyunOssUtil.contentType_map.get(contentType);
        String localPath =  SpringUtil.getHttpServletRequest().getServletContext().getRealPath("/upload/");
        String allPath = localPath+beforeImagePath;
        ImageUtil.savePicToDisk(file.getInputStream(),localPath,beforeImagePath);
        if (ImageUtil.checkImage(allPath)){
            allPath = zipImage(allPath,localPath+afterImagePath);
        }else{
            afterImagePath = beforeImagePath;
        }
        String ossPath;
        if(allPath.contains("\\")){
            ossPath = allPath.substring(allPath.indexOf("\\upload\\")+1).replace("\\", "/");
        }else{
            ossPath = allPath.substring(allPath.indexOf("/upload/")+1);
        }
        String bucketName =  PropUtil.getInstance().getConfig("aliyun.oss.bucketName",PropUtil.PUBLIC_CONF_ALIYUN);
        String ossHost =  PropUtil.getInstance().getConfig("aliyun.oss.host",PropUtil.PUBLIC_CONF_ALIYUN);
        AliyunOssUtil.uploadFile(bucketName,ossPath,allPath,contentType);
        vo.setUploaded(1);
        vo.setFileName(afterImagePath);
        vo.setUrl(ossHost+ossPath);
        return vo;
    }

    public UploadCallBackVo uploadMultipartFile(MultipartFile file)  {
        Map<String,Object> data = new HashMap<>();
        UploadCallBackVo vo = new UploadCallBackVo();
        try {
            String ossHost = PropUtil.getInstance().getConfig("aliyun.oss.host",PropUtil.PUBLIC_CONF_ALIYUN);
            String contentType = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf(".")+1);
            String beforeImagePath = ImageUtil.imageName()+"."+contentType;
            String afterImagePath = ImageUtil.imageName()+"."+contentType;
            contentType = AliyunOssUtil.contentType_map.get(contentType);
            String localPath =  SpringUtil.getHttpServletRequest().getServletContext().getRealPath("/upload/");
            String allPath = localPath+beforeImagePath;
            ImageUtil.savePicToDisk(file.getInputStream(),localPath,beforeImagePath);
            if (ImageUtil.checkImage(allPath)){
                allPath = zipImage(allPath,localPath+afterImagePath);
            }else{
                afterImagePath = beforeImagePath;
            }
            String ossPath;
            if(allPath.contains("\\")){
                ossPath = allPath.substring(allPath.indexOf("\\upload\\")+1).replace("\\", "/");
            }else{
                ossPath = allPath.substring(allPath.indexOf("/upload/")+1);
            }
            String bucketName =  PropUtil.getInstance().getConfig("aliyun.oss.bucketName",PropUtil.PUBLIC_CONF_ALIYUN);
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

    public ResponseEntity<byte[]> uploadFromUrl(String store)throws Exception{
        String filename = store.substring(store.lastIndexOf("/")).replace("/","");
        String bucketName = PropUtil.getInstance().getConfig("aliyun.oss.bucketName",PropUtil.PUBLIC_CONF_ALIYUN);
        String ossHost = PropUtil.getInstance().getConfig("aliyun.oss.host",PropUtil.PUBLIC_CONF_ALIYUN);
        String key = store.replace(ossHost,"");
        String localPath =  SpringUtil.getHttpServletRequest().getServletContext().getRealPath("/download/");
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
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file),headers, HttpStatus.CREATED);
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
        ImageUtil.zipImageFileWithKeepOriginalSize(before,after,0.7f);
        if(before.exists() && before.isDirectory()){
            before.deleteOnExit();
        }
        return after.getPath();
    }
}
