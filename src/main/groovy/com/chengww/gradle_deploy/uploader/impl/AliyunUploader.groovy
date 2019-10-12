package com.chengww.gradle_deploy.uploader.impl

import com.aliyun.oss.ClientException
import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import com.aliyun.oss.OSSException
import com.chengww.gradle_deploy.uploader.Uploader
import com.chengww.gradle_deploy.uploader.model.UploadResult

/**
 * Created by chengww on 2019/10/12.
 */
class AliyunUploader implements Uploader {

    private String access_key_id, secret_access_key, endpoint, bucket

    AliyunUploader(String access_key_id, String secret_access_key, String endpoint, String bucket) {
        this.access_key_id = access_key_id
        this.secret_access_key = secret_access_key
        this.endpoint = endpoint
        this.bucket = bucket
    }

    @Override
    UploadResult upload(String objectKey, File file) {
        if (!endpoint.startsWith("https://") && !endpoint.startsWith("http://")) {
            endpoint = "https://" + endpoint
        }

        UploadResult result = new UploadResult()
        OSS client = new OSSClientBuilder().build(endpoint, access_key_id, secret_access_key)
        try {
            client.putObject(bucket, objectKey, file)

            String protocol, host;
            if (endpoint.startsWith("https://")) {
                protocol = "https://"
                host = endpoint.substring(8)
            } else {
                protocol = "http://"
                host = endpoint.substring(7)
            }
            result.success = true
            result.message = "Upload ${objectKey} success.\n" +
                    "Download URL: ${protocol + bucket}.${host}/${objectKey}"
        } catch (OSSException oe) {
            result.message = "Upload ${objectKey} failed.\n" + "Error Message: ${oe.errorMessage}\n"
                                + "Error Code: ${oe.errorCode}\nRequest ID: ${oe.requestId}\n"
                                + "Host ID: ${oe.hostId}"
        } catch (ClientException ce) {
            result.message = "Upload ${objectKey} failed.\nError Message: ${ce.message}\n" +
                    "Maybe the client is not able to access the network."
        } finally {
            client.shutdown()
        }
        return result
    }
}
