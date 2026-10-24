package com.klymiuk.scratchcard.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.klymiuk.scratchcard.core.designsystem.theme.CodeTextStyle
import com.klymiuk.scratchcard.core.designsystem.theme.ScratchCardTheme
import com.klymiuk.scratchcard.domain.model.ScratchCard

@Composable
fun ScratchCardView(
    card: ScratchCard,
    modifier: Modifier = Modifier,
) {
    val color = card.color()
    val stateLabel = card.stateLabel()
    val stateDescription = stringResource(R.string.core_ui_card_state_description, stateLabel)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(CARD_ASPECT_RATIO)
            .semantics { contentDescription = stateDescription },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(CARD_ASPECT_RATIO)
                .background(Brush.linearGradient(listOf(color, color.copy(alpha = 0.7f))))
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.core_ui_card_title),
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
            )
            Text(
                text = card.codeOrNull() ?: stringResource(R.string.core_ui_code_hidden),
                style = if (card.codeOrNull() != null) CodeTextStyle else MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
            Surface(shape = RoundedCornerShape(50), color = Color.White.copy(alpha = 0.25f)) {
                Text(
                    text = stateLabel,
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                )
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun ScratchCard.stateLabel(): String = stringResource(
    when (this) {
        ScratchCard.Unscratched -> R.string.core_ui_state_unscratched
        is ScratchCard.Scratched -> R.string.core_ui_state_scratched
        is ScratchCard.Activated -> R.string.core_ui_state_activated
    },
)

@Composable
private fun ScratchCard.color(): Color = with(ScratchCardTheme.cardColors) {
    when (this@color) {
        ScratchCard.Unscratched -> unscratched
        is ScratchCard.Scratched -> scratched
        is ScratchCard.Activated -> activated
    }
}

private fun ScratchCard.codeOrNull(): String? = when (this) {
    ScratchCard.Unscratched -> null
    is ScratchCard.Scratched -> code
    is ScratchCard.Activated -> code
}

private const val CARD_ASPECT_RATIO = 1.586f

@Preview
@Composable
private fun ScratchCardViewPreview() {
    ScratchCardTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
            ScratchCardView(ScratchCard.Unscratched)
            ScratchCardView(ScratchCard.Scratched("6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55"))
            ScratchCardView(ScratchCard.Activated("6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55"))
        }
    }
}
