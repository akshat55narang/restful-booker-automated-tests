import utils.Helpers.getParameterValue
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

object ConfigManager {
    private const val DEFAULT_URI = "default.uri"
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

    fun getDefaultUri(): String {
        return getParameterValue("defaultUri", getConfig(DEFAULT_URI))
    }

    fun getDefaultUsername(): String {
        return getParameterValue("defaultUsername", getConfig(DEFAULT_USERNAME))
    }

    fun getDefaultPassword(): String {
        return getParameterValue("defaultPassword", getConfig(DEFAULT_PASSWORD))
    }
}
