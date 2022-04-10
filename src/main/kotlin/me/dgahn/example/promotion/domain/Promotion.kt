package me.dgahn.example.promotion.domain

import me.dgahn.example.common.domain.BaseDomain
import java.util.UUID

class Promotion(
    val message: String,
    val recipients: List<Recipient>,
    id: UUID = DEFAULT_UUID
) : BaseDomain(id)
