package de.config

object DatabaseConfig {

    fun validateEnvironmentVariables() {
        requireEnvironmentVariable("DATABASE_URL")
        requireEnvironmentVariable("DATABASE_USER")
        requireEnvironmentVariable("DATABASE_PASSWORD")

        println("Database environment variables were found.")
    }

    private fun requireEnvironmentVariable(name: String): String {
        return System.getenv(name)
            ?: error("$name environment variable is not set.")
    }
}