@file:Suppress("unused")

package io.github.kodepix.ktor

import de.comahe.i18n4k.*
import de.comahe.i18n4k.config.*
import java.util.Locale


/**
 * Sets language for I18N internationalization.
 *
 * Available `en` and `ru` in Ktor Server Extras.
 *
 * @param language a language code
 */
fun ktorLocale(language: String) {
    (i18n4k as I18n4kConfigDefault).locale = Locale.of(language)
}
