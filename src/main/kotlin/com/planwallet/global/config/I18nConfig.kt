package com.planwallet.global.config

import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.nio.charset.StandardCharsets
import java.util.Locale

/**
 * 다국어(i18n) 설정.
 */
@Configuration
class I18nConfig {
    @Bean
    fun messageSource(): MessageSource {
        return ReloadableResourceBundleMessageSource().apply {
            setBasename("classpath:i18n/messages")
            setDefaultEncoding(StandardCharsets.UTF_8.name())
            setFallbackToSystemLocale(false)
        }
    }

    @Bean
    fun localeResolver(): LocaleResolver {
        return AcceptHeaderLocaleResolver().apply {
            setDefaultLocale(Locale.KOREAN)
        }
    }
}
