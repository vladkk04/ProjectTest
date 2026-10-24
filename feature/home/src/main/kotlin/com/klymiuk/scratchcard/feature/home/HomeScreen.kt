package com.klymiuk.scratchcard.feature.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.klymiuk.scratchcard.core.designsystem.component.AppButton
import com.klymiuk.scratchcard.core.designsystem.component.ScreenScaffold
import com.klymiuk.scratchcard.core.designsystem.component.AppOutlinedButton
import com.klymiuk.scratchcard.core.designsystem.theme.ScratchCardTheme
import com.klymiuk.scratchcard.core.ui.ScratchCardView
import com.klymiuk.scratchcard.core.ui.stateLabel
import com.klymiuk.scratchcard.domain.model.ScratchCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeContent(
        uiState = uiState,
        onAction = viewModel::onAction,
        modifier = modifier,
    )
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    onAction: (HomeScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenScaffold(title = stringResource(R.string.feature_home_title), modifier = modifier) {
        ScratchCardView(card = uiState.card)
        Text(
            text = stringResource(R.string.feature_home_current_state, uiState.card.stateLabel()),
            style = MaterialTheme.typography.titleMedium,
        )
        AppButton(
            text = stringResource(R.string.feature_home_go_to_scratch),
            onClick = { onAction(HomeScreenAction.NavigateToScratch) },
        )
        AppOutlinedButton(
            text = stringResource(R.string.feature_home_go_to_activation),
            onClick = { onAction(HomeScreenAction.NavigateToActivation) },
        )
    }
}

@Preview
@Composable
private fun HomeContentPreview() {
    ScratchCardTheme {
        HomeContent(
            uiState = HomeUiState(ScratchCard.Scratched("6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55")),
            onAction = {},
        )
    }
}
