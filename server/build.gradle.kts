plugins {
    id("org.jetbrains.kotlin.jvm")
    application
}

application {
    mainClass.set("com.ecommerce.server.ServerMainKt")
}

dependencies {
    // MySQL JDBC Driver
    implementation("com.mysql:mysql-connector-j:8.3.0")

    // JSON Serialization
    implementation("com.google.code.gson:gson:2.10.1")
}
