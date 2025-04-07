package stsa.kotlin_htmx.api

import kotlinx.serialization.Serializable


@Serializable
data class ApiResponse<out T>(
    val code: Int,
    val status: String,
    val message: String,
    @JvmSuppressWildcards val data: T? = null,
    val errors: List<String>? = null
) {
    companion object {
        fun <T> success(data: T, message: String = "Success"): ApiResponse<T> {
            return ApiResponse(
                code = 200,
                status = "OK",
                message = message,
                data = data
            )
        }

        fun <T> error(message: String, errors: List<String>? = null, code: Int = 400): ApiResponse<T> {
            return ApiResponse(
                code = code,
                status = "Error",
                message = message,
                errors = errors
            )
        }
    }
}
