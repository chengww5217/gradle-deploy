package com.chengww.gradle_deploy.uploader.model

/**
 * Created by chengww on 2019/10/11.
 */
class UploadResult {
    private boolean success
    private String message

    boolean getSuccess() {
        return success
    }

    void setSuccess(boolean success) {
        this.success = success
    }

    String getMessage() {
        return message
    }

    void setMessage(String message) {
        this.message = message
    }

}
