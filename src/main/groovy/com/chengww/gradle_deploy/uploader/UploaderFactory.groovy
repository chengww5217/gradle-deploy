package com.chengww.gradle_deploy.uploader
/**
 * Created by chengww on 2019/10/11.
 */
interface UploaderFactory {
    Uploader create(Properties properties)
}
