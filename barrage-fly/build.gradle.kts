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
    id("org.springframework.boot") version "2.7.15"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "tech.ordinaryroad"
version = "1.0.2-beta"

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
    mavenLocal()
    mavenCentral()
}

dependencies {
    val liveChatClientVersion = "0.0.11-SNAPSHOT"
    val ordinaryroadVersion = "1.6.0"
    val saTokenVersion = "1.35.0.RC"
    val qLExpressVersion = "3.3.2"

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

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
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
        file("$buildDir/libs/${project.name}-${project.version}.jar")
            .copyTo(file("$projectDir/../.docker/${appName}/app/${appName}.jar"), true)
    }
}

