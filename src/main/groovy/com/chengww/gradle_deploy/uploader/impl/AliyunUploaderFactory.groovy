package com.chengww.gradle_deploy.uploader.impl

import com.chengww.gradle_deploy.uploader.UploaderFactory
import com.chengww.gradle_deploy.utils.StringUtils
import org.gradle.api.GradleException

/**
 * Created by chengww on 2019/10/12.
 */
class AliyunUploaderFactory implements UploaderFactory {
    private AliyunUploaderFactory() {}

    static AliyunUploaderFactory getInstance() {
        return AliyunUploaderFactoryHolder.HOLDER
    }

    private String access_key_id, secret_access_key, endpoint, bucket

    @Override
    AliyunUploader create(Properties properties) {
        access_key_id = properties['aliyun.access_key_id']
        secret_access_key = properties['aliyun.secret_access_key']
        endpoint = properties['aliyun.endpoint']
        bucket = properties['aliyun.bucket']
        // Check configurations to keep all of them are not empty
        [access_key_id, secret_access_key, endpoint, bucket].each { configStr ->
            if (StringUtils.isEmpty(configStr))
                throw new GradleException("Please set configurations of 'aliyun' in local.properties")

        }
        return new AliyunUploader(access_key_id, secret_access_key, endpoint, bucket)
    }

    static class AliyunUploaderFactoryHolder {
        private static final AliyunUploaderFactory HOLDER = new AliyunUploaderFactory()
    }
}
