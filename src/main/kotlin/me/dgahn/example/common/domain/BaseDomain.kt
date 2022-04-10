package me.dgahn.example.common.domain

import java.util.UUID

abstract class BaseDomain(
    val id: UUID = DEFAULT_UUID
) {
    companion object {
        val DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseDomain

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
