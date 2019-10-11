package com.chengww.gradle_deploy.plugin

import com.chengww.gradle_deploy.uploader.Uploader
import com.chengww.gradle_deploy.uploader.impl.QingstorUploaderFactory
import com.chengww.gradle_deploy.uploader.model.UploadResult
import com.chengww.gradle_deploy.utils.StringUtils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * <P>
 *     Main Plugin, add a task named <code>gradle-deploy</code> to deploy files   .
 *     Created by chengww on 2019/10/11.
 * </P>
 *
 */
class MyAwesomePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.task("gradle-deploy") {
            doLast {
                initProperties(project)
            }
        }
    }

    /**
     * Init properties in <code>local.properties</code>.
     * @param project project
     */
    private static void initProperties(Project project) {
        Properties properties = new Properties()
        properties.load(project.rootProject.file("local.properties").newDataInputStream())
        String[] deploy_type = (properties["deploy_type"] as String).split(",")
        if (deploy_type == null || deploy_type.size() < 1) throw new GradleException("Please set 'deploy_type' in local.properties")

        String before_deploy_task = properties["before_deploy_task"]
        if (StringUtils.isNotEmpty(before_deploy_task)) {
            project.tasks.findByName(before_deploy_task)
        }
        deploy_type.each { type ->
            switch (type.trim()) {
                case "qingstor":
                    Uploader qingstor = QingstorUploaderFactory.instance.create(properties)
                    deployFiles(properties, qingstor)
                    break
            }
        }
    }

    /**
     * Deploy/upload files.
     * @param properties properties in <code>local.properties</code>
     * @param uploader {@link Uploader}
     */
    private static void deployFiles(Properties properties, Uploader uploader) {
        String file_path = properties["file_path"]
        if (StringUtils.isEmpty(file_path)) {
            throw new GradleException("Please set 'file_path' in local.properties")
        }
        File file = new File(file_path)
        String filename = file.getName()
        String file_key = properties["file_key"]
        file_key = checkKey(file_key, filename)

        println("Uploading ${file_key}...")
        UploadResult result = uploader.upload(file_key, file)
        println(result.message)
        if (!result.success)
            throw new GradleException(result.message)

        String file_json_path = properties["file_json_path"]
        if (StringUtils.isNotEmpty(file_json_path)) {
            File jsonFile = new File(file_json_path)
            filename = jsonFile.getName()
            String file_json_key = properties["file_json_key"]
            file_json_key = checkKey(file_json_key, filename)
            println("Uploading ${file_json_key}...")
            result = uploader.upload(file_json_key, jsonFile)
            println(result.message)
            if (!result.success)
                throw new GradleException(result.message)
        }
    }

    private static String checkKey(String key, String filename) {
        StringUtils.isEmpty(key) ? filename : key.replace("\${filename}", filename)
    }
}
