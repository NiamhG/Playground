package org.example

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result4k
import dev.forkhandles.result4k.Success
import org.http4k.core.Status
import org.http4k.format.ConfigurableJackson
import org.http4k.format.asConfigurable
import org.http4k.format.int
import org.http4k.lens.BiDiMapping
import org.http4k.lens.StringBiDiMappings
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object CustomJackson : ConfigurableJackson(
    KotlinModule.Builder().build().asConfigurable()
        .text(StringBiDiMappings.duration())
        .text(StringBiDiMappings.uri())
        .text(StringBiDiMappings.url())
        .text(StringBiDiMappings.uuid())
        .text(StringBiDiMappings.regexObject())
        .text(StringBiDiMappings.instant())
        .text(StringBiDiMappings.yearMonth())
        .text(StringBiDiMappings.localTime())
        .text(localDate())
        .text(StringBiDiMappings.eventCategory())
        .text(StringBiDiMappings.traceId())
        .text(StringBiDiMappings.samplingDecision())
        .text(StringBiDiMappings.throwable())
        .int({ Status(it, "") }, Status::code)
        .done()
        .deactivateDefaultTyping()
        .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
        .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
        .configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true)
)


fun localDate() =
    BiDiMapping<String, LocalDate>(
        r@{
            when (val result = parseLocalDate(it)) {
                is Success -> return@r result.value
                is Failure -> throw IllegalArgumentException(result.reason)
            }
        },
        { it.format(DateTimeFormatter.ISO_LOCAL_DATE) }
    )

fun parseLocalDate(value: String) : Result4k<LocalDate, String> {
    listOf(
        DateTimeFormatter.ISO_LOCAL_DATE,
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSS]"),
        DateTimeFormatter.ISO_LOCAL_DATE_TIME,
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[S]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSSSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.[SSSSSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[S]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSSSSSS]'Z'"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.[SSSSSSSSS]'Z'"),
    ).forEach { formatter ->
        try {
            return LocalDate.parse(value, formatter).let { Success(it) }
        } catch (_: Exception) {
        }
    }
    return Failure("Not a valid date")
}