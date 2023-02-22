import org.gradle.kotlin.dsl.version
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

@Suppress("ObjectPropertyName")
val PluginDependenciesSpec.checkDependencyUpdates: PluginDependencySpec
    get() = id(Plugins.CHECK_DEPENDENCY_UPDATES) version Versions.checkDependencyUpdates

@Suppress("ObjectPropertyName")
val PluginDependenciesSpec.detekt: PluginDependencySpec
    get() = id(Plugins.DETEKT) version Versions.detekt
