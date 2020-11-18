package mobi.cangol.plugin.seafile

import com.android.build.gradle.AppPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class UploadPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def log = project.logger

        def hasAppPlugin = project.plugins.find { p -> p instanceof AppPlugin }
        if (!hasAppPlugin) {
            throw new IllegalStateException("The 'com.android.application' plugin is required.")
        }
        def seaExtension = project.extensions.create('uploadSeaFile', UploadPluginExtension)

        project.android.applicationVariants.all { variant ->
            if (seaExtension == null) {
                log.error("Please config your sea apiToken in your build.gradle.")
                return
            }
            def buildTypeName = variant.buildType.name.capitalize()

            def productFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
            if (productFlavorNames.isEmpty()) {
                productFlavorNames = [""]
            }
            def productFlavorName = productFlavorNames.join('')
            def variationName = "${productFlavorName}${buildTypeName}"
            def uploadApkTaskName = "uploadSeaFileApk${variationName}"
            def assembleTask = seaExtension.dependsOn != null ? seaExtension.dependsOn : variant.assemble
            log.info("uploadApkTaskName == " + uploadApkTaskName)
            def uploadApkTask = project.tasks.create(uploadApkTaskName, UploadTask)
            uploadApkTask.extension = seaExtension
            uploadApkTask.variant = variant
            uploadApkTask.description = "Uploads the APK for the ${variationName} build"
            uploadApkTask.group = "seafile"
            uploadApkTask.dependsOn assembleTask
        }
    }
}
