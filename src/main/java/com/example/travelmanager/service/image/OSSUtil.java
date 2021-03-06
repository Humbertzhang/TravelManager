package com.example.travelmanager.service.image;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class OSSUtil {

    @Getter @Setter
    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    @Getter @Setter
    private static String accessKeyId = "LTAI4FvSuXnyoFnrXnY16aGx";
    @Getter @Setter
    private static String accessKeySecret = "Sb2TxAdGWuX6sX5x63nrjhdxPV1PeB";
    @Getter @Setter
    private static String bucketName = "picturesbed";

    public static OSSUtil ossUtil;

    //获取OSSClient实例
    private static OSSClient getOSSClient(){
        OSSClient ossClient = new OSSClient(getEndpoint(), getAccessKeyId(), getAccessKeySecret());
        return ossClient;
    }

    //文件流上传
    public static void uploadFile(File file, String key ) throws FileNotFoundException {
        OSSClient ossClient = getOSSClient();
        // 上传文件流
        //InputStream inputStream = new FileInputStream(fileName);
        ossClient.putObject(getBucketName(), key , file);
        // 关闭client
        ossClient.shutdown();
    }

    // MultipartFile方式上传
    public static String uploadPic(MultipartFile file, String key) throws IOException {
        OSSClient ossClient = getOSSClient();
        ossClient.putObject(getBucketName(), key , file.getInputStream());
        ossClient.shutdown();
        return key;
    }

    // 删除文件
    public static void deleteFile(String key ){
        // 创建OSSClient实例
        OSSClient ossClient = getOSSClient();
        // 删除Object
        ossClient.deleteObject(getBucketName(), key);
        // 关闭client
        ossClient.shutdown();

    }

    //获取存储对象的名字
    public static void listObject(){
        OSSClient ossClient = getOSSClient();
        // 获取指定bucket下的所有Object信息
        ObjectListing listing = ossClient.listObjects(getBucketName());

        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }
    }

}
