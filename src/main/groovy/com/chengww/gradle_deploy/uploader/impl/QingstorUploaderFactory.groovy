package com.chengww.gradle_deploy.uploader.impl

import com.chengww.gradle_deploy.uploader.UploaderFactory
import com.chengww.gradle_deploy.utils.StringUtils
import org.gradle.api.GradleException

/**
 * Created by chengww on 2019/10/11.
 */
class QingstorUploaderFactory implements UploaderFactory {

    private QingstorUploaderFactory() {}

    static QingstorUploaderFactory getInstance() {
        return QingstorUploaderFactoryHolder.HOLDER
    }

    private String access_key_id, secret_access_key, zone, bucket

    @Override
    QingstorUploader create(Properties properties) {
        access_key_id = properties['qingstor.access_key_id']
        secret_access_key = properties['qingstor.secret_access_key']
        zone = properties['qingstor.zone']
        bucket = properties['qingstor.bucket']
        // Check configurations to keep all of them are not empty
        [access_key_id, secret_access_key, zone, bucket].each { configStr ->
            if (StringUtils.isEmpty(configStr))
                throw new GradleException("Please set configurations of 'qingstor' in local.properties")

        }
        return new QingstorUploader(access_key_id, secret_access_key, zone, bucket)
    }

    static class QingstorUploaderFactoryHolder {
        private static final QingstorUploaderFactory HOLDER = new QingstorUploaderFactory()
    }
}
