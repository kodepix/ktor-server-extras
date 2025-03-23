@file:Suppress("unused")

package io.github.kodepix.ktor

import de.comahe.i18n4k.*
import de.comahe.i18n4k.config.*
import io.github.kodepix.*
import java.util.Locale


/**
 * Sets language for I18N internationalization.
 *
 * @param language a language code
 */
fun locale(language: String) {
    (i18n4k as I18n4kConfigDefault).locale = Locale.of(language)
}


private val log by logger()
