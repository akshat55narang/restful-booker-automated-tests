import utils.Helpers.getParameterValue
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

object ConfigManager {
    private const val BASE_URI = "base.uri"
    private const val DEFAULT_USERNAME = "default.username"
    private const val DEFAULT_PASSWORD = "default.password"

    private fun loadProperties(): Properties {
        val properties = Properties()
        val propertyFile = Paths.get(javaClass.getResource("/default.properties")!!.toURI()).toFile()
        val reader = FileInputStream(propertyFile)
        properties.load(reader)
        return properties
    }

    private fun getConfig(property: String): String {
        val properties = loadProperties()
        if (!loadProperties().containsKey(property)) throw RuntimeException("Property $property not defined")
        return properties.getProperty(property)
    }

    fun getBaseUri(): String {
        return getParameterValue("baseUri", getConfig(BASE_URI))
    }

    fun getDefaultUsername(): String {
        return getParameterValue("defaultUsername", getConfig(DEFAULT_USERNAME))
    }

    fun getDefaultPassword(): String {
        return getParameterValue("defaultPassword", getConfig(DEFAULT_PASSWORD))
    }
}
