import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.2"
    id("com.google.protobuf") version "0.8.18"
    application
}

group = "com.apexnova"
version = "0.0.1-SNAPSHOT"

val protobufVersion = "3.17.3"
val grpcVersion = "1.60.0"
val grpcKotlinVersion = "1.4.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21 // or another LTS version
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion") // gRPC Kotlin stub
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion") // Netty server for gRPC
    implementation("io.grpc:grpc-protobuf:$grpcVersion") // Protobuf support for gRPC
    implementation("io.grpc:grpc-stub:$grpcVersion") // gRPC stubs
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile>().all {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = JavaVersion.VERSION_21.toString() // Change to a supported version
    }
}

// Look here for set path
// sourceSets {
//     main {
//         proto {
//             srcDir("src/main/proto")
//         }
//     }
// }

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:$protobufVersion" // Ensure the version is correct
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion" // Ensure the version is correct
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:$grpcKotlinVersion:jdk8@jar"
        }
        
    }

    // generatedFilesBaseDir = "$projectDir/src/"

    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins { // this is super important
                create("kotlin")
            }
        }
    }
}

application {
    mainClass.set("com.apexnova.sample.MainApplicationKt")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
