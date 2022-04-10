package me.dgahn.example.promotion.domain

@JvmInline
value class PhoneNumber(
    val value: String
) {
    init {
        require(validatePhoneNumber(value)) {
            "You must follow the phone number format. ex) +821012341234, +82021231234"
        }
    }

    companion object {
        private val PHONE_NUMBER_REGEX = "\\+[0-9]{11,12}".toRegex()
    }

    private fun validatePhoneNumber(value: String) = PHONE_NUMBER_REGEX.matches(value)
}
