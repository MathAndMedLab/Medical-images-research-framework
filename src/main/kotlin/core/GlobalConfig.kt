package core


object GlobalConfig {

    private val options: HashMap<String, Any>

    init {
        options = hashMapOf(
                Pair("workingDir", "C:\\src\\MIRF_TEMP"),
                Pair("elastix_dir", "C:\\MS-DATA\\elastix (registrator)"))
    }

    fun <T> get(name: String): T {
        val value = options[name] ?: throw ConfigException("no value for $name provided")
        return value as T
    }
}