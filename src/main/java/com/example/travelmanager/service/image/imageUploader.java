package com.example.travelmanager.service.image;

import java.io.IOException;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.UploadFileRequest;
import com.aliyun.oss.model.UploadFileResult;


public class imageUploader {
    private static String endpoint = "oss-cn-hangzhou.aliyuncs.com";
    private static String accessKeyId = "LTAI4FvSuXnyoFnrXnY16aGx";
    private static String accessKeySecret = "Sb2TxAdGWuX6sX5x63nrjhdxPV1PeB";
    private static String bucketName = "picturesbed";

    public static void test(String[] args) throws IOException {

        // 文件名
        // TODO: 先分割文件名取得文件后缀，再加上文件名
        String key = "travelmanager/" + generateFileName();
        String uploadFile = "/Users/apple/Desktop/images.png";

        System.out.println(key);

        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key);
            // The local file to upload---it must exist.
            uploadFileRequest.setUploadFile(uploadFile);
            // Sets the concurrent upload task number to 5.
            uploadFileRequest.setTaskNum(5);
            // Sets the part size to 1MB.
            uploadFileRequest.setPartSize(1024 * 1024 * 1);
            // Enables the checkpoint file. By default it's off.
            uploadFileRequest.setEnableCheckpoint(true);

            UploadFileResult uploadResult = ossClient.uploadFile(uploadFileRequest);


            CompleteMultipartUploadResult multipartUploadResult =
                    uploadResult.getMultipartUploadResult();

            System.out.println(multipartUploadResult.getETag());
            System.out.println("https://picturesbed.oss-cn-hangzhou.aliyuncs.com/" + key);

        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    private static String generateFileName() {
        Long l = System.currentTimeMillis();
        String timeString = l.toString();
        Integer hashCode = timeString.hashCode();
        String hashName;

        if(hashCode < 0) {
            Integer negInt = (-hashCode);
            hashName = "0" + negInt.toString();
        }
        else {
            hashName = hashCode.toString();
        }

        Integer salt = hashName.hashCode();
        String saltString = salt.toString();

        return hashName + saltString;
    }
}