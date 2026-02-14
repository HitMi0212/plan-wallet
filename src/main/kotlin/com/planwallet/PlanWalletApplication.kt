package com.planwallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

/**
 * plan-wallet 애플리케이션 엔트리포인트.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
class PlanWalletApplication

/**
 * 애플리케이션 실행.
 */
fun main(args: Array<String>) {
    runApplication<PlanWalletApplication>(*args)
}
