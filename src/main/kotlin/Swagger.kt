import org.http4k.core.Method
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.*

internal const val API_DESCRIPTION_PATH = "/swagger.json"

/**
 * Exposes Swagger UI with /docs path as its entry point.
 */
fun swaggerUi(): RoutingHttpHandler = routes(
    "docs" bind Method.GET to {
        Response(Status.FOUND).header("Location", "/docs/index.html?url=$API_DESCRIPTION_PATH")
    },
    // For some reason the static handler does not work without "/" path prefix.
    "/docs" bind static(ResourceLoader.Classpath("META-INF/resources/webjars/swagger-ui/3.52.3"))
)
