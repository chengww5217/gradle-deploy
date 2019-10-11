# gradle-deploy

[![Download](https://api.bintray.com/packages/chengww5217/maven/gradle-deploy/images/download.svg)](https://bintray.com/chengww5217/maven/gradle-deploy)
[![GitHub issues](https://img.shields.io/github/issues/chengww5217/gradle-deploy)](https://github.com/chengww5217/gradle-deploy/issues)
[![GitHub](https://img.shields.io/github/license/chengww5217/gradle-deploy?color=yellow)](https://github.com/chengww5217/gradle-deploy/blob/master/LICENSE)

> gradle-deploy 是一个对以 gradle 构建的项目产物发布到对象存储或自定义服务器的一个 gradle 插件。

例如对 Android 项目打包后的 `.apk` 文件进行发布更新。

您只需要进行简单的配置，就可以很方便的在 gradle 中发布您的构建产物到对象存储或自定义服务器。

## Useage 使用

1. 在项目 **根目录的 build.gradle** 文件中新增如下代码:

   ```groovy
   buildscript {
       repositories {
           jcenter()
       }
       dependencies {
           classpath 'com.chengww:gradle-deploy:0.0.1'
       }
   }
   ```

2. 在 **app 模块的 build.gradle** 文件中新增如下代码：

   ```groovy
   apply plugin: 'com.chengww.gradle-deploy'
   ```

   如您项目中没有子模块，请将上面代码放进您项目中唯一的 `build.gradle` 文件中。

3. 在项目根目录新增 `local.properties` 文件，然后进行配置

   具体参考[Configuration 配置](#Configuration 配置)一节。

4. 等待 gradle 同步完成后，在项目根目录运行如下 gradle task 即可：

   ```bash
   ./gradlew gradle-deploy
   ```

##  Configuration 配置

上传构建产物的配置文件位于项目根目录下的 `local.properties` 文件中。

这个文件中的配置信息指示了插件如何上传构建产物，上传配置信息等。

> 注：`local.properties` 文件中包含了很多关键信息，这个文件一般都会被加入到 `.gitignore` 文件中。

配置信息以键值对的形式书写：`key=value`

具体写法参考如下：

```properties
# ====== Gradle-deploy parameters start ======
deploy_type=qingstor,custom_server,aliyun
file_path=/Users/chengww/StudioProjects/demo/app/build/outputs/apk/_huawei/release/Demo_huawei_1.5.0_beta3.apk
# Optional
file_json_path=/Users/chengww/Desktop/work_space/apk_update/demo_test_update.json
file_key=android-release/${filename}
file_json_key=android-release/${filename}
depends_on_task=assemble_huaweiRelease
# Parameters of deploy_type
qingstor.access_key_id=XXXXXXXX
qingstor.secret_access_key=XXXXXXXXXXXXXXXXXXX
qingstor.zone=pek3b
qingstor.bucket=bucket-name
# ====== Gradle-deploy parameters end ======
```



~~现支持将构建产物上传到自定义服务器、青云对象存储、阿里云 OSS 上（TBD）~~

现支持将构建产物上传到青云对象存储上。

具体参数解释：

### 一级参数

一级参数是最外层的参数，基本是待发布文件相关的内容。

| 参数名         | 参数说明                                                     | 是否必填 | 参考数据                      |
| -------------- | ------------------------------------------------------------ | -------- | ----------------------------- |
| deploy_type    | 发布类型，多个类型用逗号分隔                                 | 是       | qingstor,custom_server,aliyun |
| file_path      | 待发布文件路径，如 `.apk` 文件                               | 是       |                               |
| file_json_path | 待发布的检查更新文件路径，如 `.json` 文件                    | 否       |                               |
| file_key       | 待发布文件的 key，支持 `${filename}` 作为文件名的通配符。如果是上传到对象存储可以在前面加上 `folder/${filename}` 指示其远程前缀路径 | 否       | android-release/${filename}   |
| file_json_key  | 待发布的检查更新文件的 key，支持 `${filename}` 作为文件名的通配符。如果是上传到对象存储可以在前面加上 `folder/${filename}` 指示其远程前缀路径 | 否       | android-release/${filename}   |

### 二级参数

二级参数是填写 `deploy_type` 之后相应的配置文件。

1. 添加 `deploy_type=qingstor` 之后需要配置青云对象存储的相关信息以进行上传。

   | 参数名                     | 参数说明          | 是否必填 | 参考数据    |
   | -------------------------- | ----------------- | -------- | ----------- |
   | qingstor.access_key_id     | access_key_id     | 是       |             |
   | qingstor.secret_access_key | secret_access_key | 是       |             |
   | qingstor.zone              | zone 区域         | 是       | pek3b       |
   | qingstor.bucket            | bucket 名称       | 是       | bucket-name |

2. 添加 `deploy_type=custom_server` 之后需要配置自定义上传服务器的相关信息以进行上传。

   TBD

3. 添加 `deploy_type=aliyun` 之后需要配置阿里云 OSS 的相关信息以进行上传。

   TBD

