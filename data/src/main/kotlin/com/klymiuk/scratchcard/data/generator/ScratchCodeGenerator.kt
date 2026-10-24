package com.klymiuk.scratchcard.data.generator

fun interface ScratchCodeGenerator {
    suspend fun generate(): String
}
