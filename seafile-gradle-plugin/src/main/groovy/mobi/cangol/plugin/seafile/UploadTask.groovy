package mobi.cangol.plugin.seafile

import com.android.build.gradle.api.ApkVariantOutput
import com.android.build.gradle.api.ApplicationVariant
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class UploadTask extends DefaultTask {
    UploadPluginExtension extension
    UploadClient client
    ApplicationVariant variant

    @TaskAction
    upload() {
        def log = project.logger
        if (client == null) {
            client = UploadClient.init(extension)
        }

        def apkOutput = variant.outputs.find { variantOutput -> variantOutput instanceof ApkVariantOutput }

        String apkPath = apkOutput.outputFile.getAbsolutePath()
        log.warn("apkPath ===> " + apkPath)

        if (extension.token==null) {
            extension.token=client.getToken();
            log.warn("getToken ===> " + extension.token)
        }

        def destDirPath = extension.getProperty(variant.buildType.name + "Dir");
        if (destDirPath == null) {
            destDirPath = ""
        }
        log.warn("destDirPath ===> " + destDirPath)

        def fileDir = destDirPath.substring(destDirPath.lastIndexOf("/")+1)
        log.warn("fileDir ===> " + fileDir)
        if(fileDir.startsWith("V")){
            def result = client.createDir(destDirPath)
            log.warn("createDir ===> " + result)
        }

        def link = client.getUploadLink(destDirPath)
        log.warn("uploadLink ===> " + link)

        def url = client.upload(link, destDirPath, apkOutput.outputFile.name, apkPath)
        log.warn("upload ===> " + url)

    }

}
