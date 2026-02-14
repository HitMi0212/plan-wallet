package com.planwallet

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class PlanWalletApplication

fun main(args: Array<String>) {
    runApplication<PlanWalletApplication>(*args)
}
