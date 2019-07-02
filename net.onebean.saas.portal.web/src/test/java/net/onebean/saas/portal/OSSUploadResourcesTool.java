package net.onebean.saas.portal;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import net.onebean.component.aliyun.AliyunOssUtil;
import net.onebean.component.aliyun.prop.PropUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;


public class OSSUploadResourcesTool {
	/*路径参数系*/

	private static PropUtils propUtils = PropUtils.getInstance();
	private static String projectPath = propUtils.getConfing("aliyun.oss.projectPath");
	private static String sourcePath = propUtils.getConfing("aliyun.oss.sourcePath");
	/*oss参数系*/
	private static String bucketName = propUtils.getConfing("aliyun.oss.bucketName");


	public static TreeMap<File, LinkedList<File>> dirFiles = new TreeMap<File, LinkedList<File>>();

	
	static void getDirectoryFiles(File dir) {
        if (!dir.isDirectory()) {
            return;
        }
        LinkedList<File> files= new LinkedList<File>();
        File[] filesinDir = dir.listFiles();
        if(filesinDir.length > 0) {
            for (int i = 0; i < filesinDir.length; i++) {
                files.add(filesinDir[i]);
            }
        } else {
            dirFiles.put(dir, null);
            return;
        }
        dirFiles.put(dir, files);
        for(int i = 0; i < filesinDir.length; i++) {
            if (filesinDir[i].isDirectory()) {
                getDirectoryFiles(filesinDir[i]);
            }
        }
    }
	
	public static void main(String[] ags){
		String key = "";
		String contentType = "";
		String allPath = "";

        try {
        	String workPath = System.getProperty("user.dir");

			workPath += projectPath;

        	String sourcePath = new StringBuffer().append(workPath).append(OSSUploadResourcesTool.sourcePath).toString();
        	
        	OSSUploadResourcesTool.getDirectoryFiles(new File(sourcePath));
        	Iterator<File> iterator = OSSUploadResourcesTool.dirFiles.keySet().iterator();
            while (iterator.hasNext()) {
                File dir = iterator.next();
                LinkedList<File> fileInDir = OSSUploadResourcesTool.dirFiles.get(dir);
                if (fileInDir != null) {
                    Iterator<File> it = fileInDir.iterator();
                    while (it.hasNext()) {
                    	File file = it.next();
                    	if(file.isFile()) {
                        	allPath = file.getAbsolutePath();
                        	key = allPath.substring(allPath.indexOf("static")).replace("\\", "/");
                        	String fileName = file.getName();
                        	String contentTypeTemp = fileName.substring(fileName.lastIndexOf(".") + 1);
                        	if(null == AliyunOssUtil.contentType_map.get(contentTypeTemp)) {
                        		contentType = "application/json";
                        	} else {
                        		contentType = AliyunOssUtil.contentType_map.get(contentTypeTemp);
                        	}
							System.out.println(AliyunOssUtil.uploadFile(bucketName, key, allPath, contentType));
                        }
                        
                    }
                }
                
            }
		} catch (OSSException e) {
			e.printStackTrace();
		} catch(ClientException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}   
	}
}
