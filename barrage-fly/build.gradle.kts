/*
 * Copyright 2023 OrdinaryRoad
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.16"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "tech.ordinaryroad"
version = "1.1.9"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven { url = uri("https://maven.aliyun.com/repository/public") }
    maven { url = uri("https://maven.aliyun.com/repository/central") }
    maven { url = uri("https://maven.aliyun.com/repository/gradle-plugin") }
    maven { url = uri("https://ordinaryroad-maven.pkg.coding.net/repository/ordinaryroad/maven-pro/") }
    mavenCentral() // 如果提示未找到依赖，尝试把这行放到第一行
    mavenLocal()
}

extra["springBootAdminVersion"] = "2.7.4"

// val operatingSystem: OperatingSystem = DefaultNativePlatform.getCurrentOperatingSystem()

dependencies {
    val liveChatClientVersion = "0.9.0"
    val liveChatClientBrotliVersion = "1.16.0"
    val ordinaryroadVersion = "1.6.0"
    val saTokenVersion = "1.36.0"
    val qLExpressVersion = "3.3.2"

    implementation("de.codecentric:spring-boot-admin-starter-client")
    implementation("de.codecentric:spring-boot-admin-starter-server")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springframework.integration:spring-integration-rsocket")
    implementation("tech.ordinaryroad:live-chat-client-bilibili:$liveChatClientVersion")
    implementation("tech.ordinaryroad:live-chat-client-douyu:$liveChatClientVersion")
    implementation("tech.ordinaryroad:live-chat-client-huya:$liveChatClientVersion")
    implementation("tech.ordinaryroad:live-chat-client-douyin:$liveChatClientVersion")
    implementation("tech.ordinaryroad:live-chat-client-kuaishou:$liveChatClientVersion")
    implementation("tech.ordinaryroad:ordinaryroad-commons-core:$ordinaryroadVersion") {
        exclude("org.springframework")
        exclude("org.springframework.cloud")
        exclude("org.springframework.boot")
        exclude("com.alibaba.fastjson2")
        exclude("org.mapstruct")
        exclude("org.apache.commons")
    }
    implementation("tech.ordinaryroad:ordinaryroad-commons-mybatis:$ordinaryroadVersion") {
        exclude("tech.ordinaryroad", "ordinaryroad-commons-satoken")
        exclude("tech.ordinaryroad", "ordinaryroad-push-api")
    }
    implementation("cn.dev33:sa-token-reactor-spring-boot-starter:$saTokenVersion")
    implementation("com.alibaba:QLExpress:$qLExpressVersion")
    implementation("com.hubspot.jackson:jackson-datatype-protobuf:0.9.15")

    // implementation("com.aayushatharva.brotli4j:brotli4j:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-windows-aarch64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-windows-x86_64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-osx-aarch64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-osx-x86_64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-linux-armv7:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-linux-aarch64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-linux-x86_64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-linux-s390x:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-linux-riscv64:$liveChatClientBrotliVersion")
    implementation("com.aayushatharva.brotli4j:native-linux-ppc64le:$liveChatClientBrotliVersion")
//    compileOnly(
//        "com.aayushatharva.brotli4j:native-" +
//                if (operatingSystem.isWindows) {
//                    if (DefaultNativePlatform.getCurrentArchitecture().isArm()) {
//                        "windows-aarch64"
//                    } else {
//                        "windows-x86_64"
//                    }
//                } else if (operatingSystem.isMacOsX) {
//                    if (DefaultNativePlatform.getCurrentArchitecture().isArm()) {
//                        "osx-aarch64"
//                    } else {
//                        "osx-x86_64"
//                    }
//                } else if (operatingSystem.isLinux) {
//                    if (Architectures.ARM_V7.isAlias(DefaultNativePlatform.getCurrentArchitecture().name)) {
//                        "linux-armv7"
//                    } else if (Architectures.AARCH64.isAlias(DefaultNativePlatform.getCurrentArchitecture().name)) {
//                        "linux-aarch64"
//                    } else if (Architectures.X86_64.isAlias(DefaultNativePlatform.getCurrentArchitecture().name)) {
//                        "linux-x86_64"
//                    }
////                    else if (Architectures.S390X.isAlias(DefaultNativePlatform.getCurrentArchitecture().name)) {
////                        "linux-s390x"
////                    } else if (Architectures.RISCV_64.isAlias(DefaultNativePlatform.getCurrentArchitecture().name)) {
////                        "linux-riscv64"
////                    } else if (Architectures.PPC64LE.isAlias(DefaultNativePlatform.getCurrentArchitecture().name)) {
////                        "linux-ppc64le"
////                    }
//                    else {
//                        throw IllegalStateException("Unsupported architecture: ${DefaultNativePlatform.getCurrentArchitecture().name}")
//                    }
//                } else {
//                    throw IllegalStateException("Unsupported operating system: $operatingSystem")
//                } + ":$brotliVersion"
//    )

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

dependencyManagement {
    imports {
        mavenBom("de.codecentric:spring-boot-admin-dependencies:${property("springBootAdminVersion")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

// 复制Jar包到docker目录下
tasks.withType<BootJar> {
    val appName = "ordinaryroad-barrage-fly"
    doLast {
        file("$buildDir/libs/${project.name}-${project.version}.jar").let {
            if (file("$projectDir/src/main/resources/static/index.html").exists()) {
                it.copyTo(file("$projectDir/../.docker/${appName}-with-ui/app/${appName}.jar"), true)
            } else {
                it.copyTo(file("$projectDir/../.docker/${appName}/app/${appName}.jar"), true)
            }
        }
    }
}

