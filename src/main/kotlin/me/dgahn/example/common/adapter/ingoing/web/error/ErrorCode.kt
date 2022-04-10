package me.dgahn.example.common.adapter.ingoing.web.error

private const val BAD_REQUEST_CODE = 400

enum class ErrorCode(
    val status: Int,
    val code: String,
) {
    INVALID_PARAMETER(BAD_REQUEST_CODE, "C001");
}
