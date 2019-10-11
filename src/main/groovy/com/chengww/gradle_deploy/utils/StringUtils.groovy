package com.chengww.gradle_deploy.utils

/**
 * Created by chengww on 2019/10/11.
 */
class StringUtils {
    static boolean isEmpty(String str) {
        return str == null || str.length() == 0
    }

    static boolean isNotEmpty(String str) {
        return !isEmpty(str)
    }
}
