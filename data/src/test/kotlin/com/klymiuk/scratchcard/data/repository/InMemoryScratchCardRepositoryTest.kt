package com.klymiuk.scratchcard.data.repository

import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemoryScratchCardRepositoryTest {

    private val repository = InMemoryScratchCardRepository()

    @Test
    fun `card is initially unscratched`() {
        assertEquals(ScratchCardEntity.Unscratched, repository.scratchCard.value)
    }

    @Test
    fun `compareAndSet writes when expected card matches`() = runTest {
        val scratched = ScratchCardEntity.Scratched("code")

        assertTrue(repository.compareAndSet(ScratchCardEntity.Unscratched, scratched))
        assertEquals(scratched, repository.scratchCard.value)
    }

    @Test
    fun `compareAndSet keeps card when expected card does not match`() = runTest {
        val written = repository.compareAndSet(
            expected = ScratchCardEntity.Scratched("code"),
            new = ScratchCardEntity.Activated("code"),
        )

        assertFalse(written)
        assertEquals(ScratchCardEntity.Unscratched, repository.scratchCard.value)
    }
}
