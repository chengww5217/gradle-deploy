package com.chengww.gradle_deploy.uploader

import com.chengww.gradle_deploy.uploader.model.UploadResult

/**
 * Created by chengww on 2019/10/11.
 */
interface Uploader {
    UploadResult upload(String objectKey, File file)
}
