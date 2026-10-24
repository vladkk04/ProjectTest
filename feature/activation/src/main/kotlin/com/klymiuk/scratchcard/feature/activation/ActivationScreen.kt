package com.klymiuk.scratchcard.feature.activation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun ActivationScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<ActivationViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ActivationContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
private fun ActivationContent(
    uiState: ActivationUiState,
    onAction: (ActivationScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenScaffold(
        title = stringResource(R.string.feature_activation_title),
        onBack = { onAction(ActivationScreenAction.Back) },
        backContentDescription = stringResource(R.string.feature_activation_back),
        modifier = modifier,
    ) {
        ScratchCardView(card = uiState.card)
        Text(
            text = stringResource(
                when {
                    uiState.isActivating -> R.string.feature_activation_in_progress
                    uiState.card is ScratchCard.Unscratched -> R.string.feature_activation_hint_unscratched
                    uiState.card is ScratchCard.Scratched -> R.string.feature_activation_hint_scratched
                    else -> R.string.feature_activation_hint_activated
                },
            ),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        AppButton(
            text = stringResource(R.string.feature_activation_action),
            onClick = { onAction(ActivationScreenAction.Activate) },
            enabled = uiState.canActivate,
            loading = uiState.isActivating,
        )
    }

    uiState.error?.let { error ->
        ActivationErrorDialog(error = error, onDismiss = { onAction(ActivationScreenAction.DismissError) })
    }
}

@Composable
private fun ActivationErrorDialog(
    error: ActivationError,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.feature_activation_error_ok))
            }
        },
        title = { Text(stringResource(R.string.feature_activation_error_title)) },
        text = {
            Text(
                when (error) {
                    is ActivationError.Rejected ->
                        stringResource(R.string.feature_activation_error_rejected, error.androidVersion)
                    ActivationError.Network -> stringResource(R.string.feature_activation_error_network)
                    ActivationError.InvalidResponse ->
                        stringResource(R.string.feature_activation_error_invalid_response)
                    ActivationError.InvalidState -> stringResource(R.string.feature_activation_error_invalid_state)
                },
            )
        },
    )
}

@Preview
@Composable
private fun ActivationContentPreview() {
    ScratchCardTheme {
        ActivationContent(
            uiState = ActivationUiState(
                card = ScratchCard.Scratched("6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55"),
                error = ActivationError.Rejected(androidVersion = 277_028L),
            ),
            onAction = {},
        )
    }
}
