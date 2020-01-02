package io.javalin.example.kotlin

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.example.kotlin.user.UserController
import io.javalin.plugin.openapi.OpenApiOptions
import io.javalin.plugin.openapi.OpenApiPlugin
import io.javalin.plugin.openapi.ui.ReDocOptions
import io.javalin.plugin.openapi.ui.SwaggerOptions
import io.swagger.v3.oas.models.info.Info

fun main() {

    Javalin.create {
        it.registerPlugin(getConfiguredOpenApiPlugin())
        it.defaultContentType = "application/json"
    }.routes {
        path("users") {
            get(UserController::getAll)
            post(UserController::create)
            path(":userId") {
                get(UserController::getOne)
                patch(UserController::update)
                delete(UserController::delete)
            }
        }
    }.start(7001)

    println("Check out ReDoc docs at http://localhost:7001/redoc")
    println("Check out Swagger UI docs at http://localhost:7001/swagger-ui")

}

fun getConfiguredOpenApiPlugin() = OpenApiPlugin(
    OpenApiOptions(
        Info().apply {
            version("1.0")
            description("User API")
        }
    ).apply {
        path("/swagger-docs") // endpoint for OpenAPI json
        swagger(SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
        reDoc(ReDocOptions("/redoc")) // endpoint for redoc
        defaultDocumentation { doc ->
            doc.json("500", ErrorResponse::class.java)
            doc.json("503", ErrorResponse::class.java)
        }
    }
)
