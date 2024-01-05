package org.example

import org.http4k.contract.ContractRoute
import org.http4k.contract.PreFlightExtraction
import org.http4k.contract.meta
import org.http4k.core.Body
import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.core.Status.Companion.OK
import org.http4k.format.auto
import org.http4k.lens.*
import java.io.File
import java.time.LocalDate

fun postExample(): ContractRoute =
    "/example" meta {
        receiving(exampleFormBody)
//        receiving(CustomJackson.autoBody<ExampleRequestDTO>().toLens() to ExampleRequestDTO.example)
        preFlightExtraction = PreFlightExtraction.IgnoreBody
        returning(Status.OK)
    } bindContract Method.POST to
            { request ->
                val form = exampleFormBody(request)
                val r = exampleMultipart(form)
                println(r.title)
                println(r.date)
                println(inputFilesMultipart(form)?.map {
                    val tempDir = kotlin.io.path.createTempFile().toFile().parentFile.absolutePath
                    val file = File("$tempDir/${it.filename}")
                    file.writeBytes(it.content.readAllBytes())
                })
                println(r.options)
                Response(OK)
            }

val inputFilesMultipart = MultipartFormFile.multi.optional("inputFiles")
val exampleMultipart = MultipartFormField.auto<ExampleRequestDTO>(CustomJackson).required("example")

data class ExampleRequestDTO(
    val title: String,
    val date: LocalDate,
    val existingDocumentIds: List<String>,
    val options: List<ExampleOptionRequestDTO>
) {
    companion object {
        val example = ExampleRequestDTO(
            title = "",
            date = LocalDate.parse("2023-01-02"),
            existingDocumentIds = listOf(""),
            options = listOf(
                ExampleOptionRequestDTO(
                    option = "Against",
                    voted = true,
                )
            ),
        )
    }
}

data class ExampleOptionRequestDTO(
    val option: String,
    val voted: Boolean,
)

val exampleFormBody = Body.multipartForm(Validator.Strict, inputFilesMultipart, exampleMultipart).toLens()

