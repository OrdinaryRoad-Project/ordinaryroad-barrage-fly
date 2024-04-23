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
package tech.ordinaryroad.barrage.fly.property

import cn.hutool.crypto.asymmetric.RSA
import org.slf4j.LoggerFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import tech.ordinaryroad.commons.base.exception.BaseException
import javax.annotation.PostConstruct

/**
 * @author mjz
 * @date 2023/9/11
 */
@Configuration
@ConfigurationProperties(prefix = "tech.ordinaryroad.barrage.fly.config.admin")
data class AdminProperties(
    var username: String = "",
    var password: String = "",
    var rsaPublicKey: String = "",
    var rsaPrivateKey: String = "",
) {
    private val log = LoggerFactory.getLogger(AdminProperties::class.java)

    @PostConstruct
    fun postConstruct() {
        if (rsaPublicKey.isEmpty() || rsaPrivateKey.isEmpty()) {
            if (rsaPublicKey.isEmpty() && rsaPrivateKey.isEmpty()) {
                if (log.isInfoEnabled) {
                    log.info("RSA Key Not Set, Generating")
                }

                val rsa = RSA()
                val publicKey = rsa.publicKeyBase64
                val privateKey = rsa.privateKeyBase64

                this@AdminProperties.rsaPublicKey = publicKey
                this@AdminProperties.rsaPrivateKey = privateKey

                if (log.isInfoEnabled) {
                    log.info("RSA Key Successfully Generated")
                }
            } else {
                log.error("RSA Key Error")
                throw BaseException("RSA Key Error, Please Check Your Configuration")
            }
        }
    }
}
