package com.klymiuk.scratchcard.feature.scratch

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klymiuk.scratchcard.core.designsystem.component.AppButton
import com.klymiuk.scratchcard.core.designsystem.component.ScreenScaffold
import com.klymiuk.scratchcard.core.designsystem.theme.ScratchCardTheme
import com.klymiuk.scratchcard.core.ui.ScratchCardView
import com.klymiuk.scratchcard.domain.model.ScratchCard

@Composable
fun ScratchScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<ScratchViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ScratchContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
private fun ScratchContent(
    uiState: ScratchUiState,
    onAction: (ScratchScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenScaffold(
        title = stringResource(R.string.feature_scratch_title),
        onBack = { onAction(ScratchScreenAction.Back) },
        backContentDescription = stringResource(R.string.feature_scratch_back),
        modifier = modifier,
    ) {
        ScratchCardView(card = uiState.card)
        Text(
            text = stringResource(
                when {
                    uiState.isScratching -> R.string.feature_scratch_in_progress
                    uiState.card is ScratchCard.Unscratched -> R.string.feature_scratch_hint_unscratched
                    else -> R.string.feature_scratch_hint_done
                },
            ),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        AppButton(
            text = stringResource(R.string.feature_scratch_action),
            onClick = { onAction(ScratchScreenAction.Scratch) },
            enabled = uiState.canScratch,
            loading = uiState.isScratching,
        )
    }
}

@Preview
@Composable
private fun ScratchContentPreview() {
    ScratchCardTheme {
        ScratchContent(uiState = ScratchUiState(isScratching = true), onAction = {})
    }
}
