package com.chengww.gradle_deploy.uploader.impl

import com.chengww.gradle_deploy.uploader.Uploader
import com.chengww.gradle_deploy.uploader.model.UploadResult
import com.qingstor.sdk.config.EnvContext
import com.qingstor.sdk.service.Bucket
/**
 * Created by chengww on 2019/10/11.
 */
class QingstorUploader implements Uploader {

    private String access_key_id
    private String secret_access_key
    private String zone
    private String bucketName
    private Bucket bucket

    QingstorUploader(String access_key_id, String secret_access_key, String zone, String bucket) {
        this.access_key_id = access_key_id
        this.secret_access_key = secret_access_key
        this.zone = zone
        this.bucketName = bucket
        init()
    }

    void init() {
        EnvContext context = new EnvContext(access_key_id, secret_access_key)
        bucket = new Bucket(context, zone, bucketName)
    }

    @Override
    UploadResult upload(String objectKey, File file) {
        Bucket.PutObjectInput input = new Bucket.PutObjectInput()
        input.setBodyInputFile(file)
        if (file.getName().endsWith(".json")) input.setContentType("application/json;charset=UTF-8")
        Bucket.PutObjectOutput output = bucket.putObject(objectKey, input)
        def status = output.getStatueCode()
        UploadResult result = new UploadResult()
        if (status == 200 || status == 201) {
            result.setSuccess(true)
            result.setMessage("Upload ${objectKey} success.\n" +
                    "Download URL: https://${zone}.qingstor.com/${bucketName}/${objectKey}")
        } else {
            result.setSuccess(false)
            result.setMessage("Upload ${objectKey} failed.\n" +
                    "StatusCode: ${status}\nCode: ${output.getCode()}\n" +
                    "RequestID: ${output.getRequestId()}\nMessage: ${output.getMessage()}\n" +
                    "HelpUrl: ${output.getUrl()}")
        }
        return result
    }
}
