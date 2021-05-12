val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String = "0.31.1"

plugins {
    application
    kotlin("jvm") version "1.5.0"
}

group = "com.greenfodor.medical_reports_server"
version = "0.0.1"

application {
    mainClass.set("com.greenfodor.medical_reports_server.ApplicationKt")
}

repositories {
    mavenCentral()
    jcenter()
}

sourceSets.main {
    withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
        kotlin.srcDirs("src/main/kotlin")
    }

    resources.srcDirs("src/main/resources")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //JSON
    implementation("io.ktor:ktor-gson:$ktor_version")

    //Auth
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("org.mindrot:jbcrypt:0.4")

    //Exposed ORM library
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")

    //JDBC Connection Pool
    implementation("com.zaxxer:HikariCP:3.4.5")
    //JDBC Connector for PostgreSQL
    implementation("org.postgresql:postgresql:42.2.1")

    //FreeMarker
    implementation("io.ktor:ktor-freemarker:$ktor_version")

    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
}