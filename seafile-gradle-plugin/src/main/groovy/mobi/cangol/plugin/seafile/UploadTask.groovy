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
                log.info("apkPath ===> " + apkPath)

                def destDir=extension.getProperty(variant.buildType.name+"Dir");
                if(destDir==null){
                    destDir=""
                }
                log.info("destDir ===> " + destDir)

                def result=createDir(destDir)
                log.info("createDir ===> " + result)

                def file=new File(apkPath)
                def filename="V"+variant.versionName+"."+variant.versionCode+"_"+new Date().format("yyyy-MM-dd_HH-mm-ss", TimeZone.getTimeZone("GMT+8"));
                def link=client.getUploadLink(destDir)
                log.info("uploadLink ===> " + link)

                def url=client.upload(link,destDir,filename,apkPath)
                log.info("upload ===> " + url)

        }

}
