package com.uquiz.android.ui.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.ranking.repository.UserRankRepository
import com.uquiz.android.domain.stats.repository.UserStatsRepository
import com.uquiz.android.domain.user.repository.UserProfileRepository
import com.uquiz.android.ui.designsystem.tokens.BrandNavy
import com.uquiz.android.ui.designsystem.tokens.Neutral400
import com.uquiz.android.ui.designsystem.tokens.Neutral500
import com.uquiz.android.ui.feature.home.components.HomeContinuePackCard
import com.uquiz.android.ui.feature.home.components.HomeHeroStatCard
import com.uquiz.android.ui.feature.home.components.HomeUserAvatar
import com.uquiz.android.ui.feature.home.components.UserRankBadge
import com.uquiz.android.ui.designsystem.animations.ranks.UInitiateMascot
import com.uquiz.android.ui.i18n.LocalStrings
import com.uquiz.android.ui.designsystem.components.SectionHeader
import com.uquiz.android.ui.designsystem.tokens.Orange500
import com.uquiz.android.ui.designsystem.tokens.Neutral100
import com.uquiz.android.ui.designsystem.tokens.Navy500
import com.uquiz.android.ui.designsystem.tokens.Teal700
import com.uquiz.android.ui.designsystem.tokens.UIcons
import com.uquiz.android.ui.designsystem.tokens.URadius

@Composable
fun HomeRoute(
    userProfileRepository: UserProfileRepository,
    userRankRepository: UserRankRepository,
    userStatsRepository: UserStatsRepository,
    attemptRepository: AttemptRepository,
    packRepository: PackRepository,
    onContinuePlayingClick: (String) -> Unit,
    onContinueStudyingClick: (String) -> Unit,
    onProfileClick: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModel.Factory(
            userProfileRepository,
            userRankRepository,
            userStatsRepository,
            attemptRepository,
            packRepository
        )
    )
    val uiState by viewModel.uiState.collectAsState()
    val strings = LocalStrings.current

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = strings.navHome,
                style = MaterialTheme.typography.titleLarge,
                color = Neutral500
            )
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Neutral100, RoundedCornerShape(URadius))
                        .background(androidx.compose.ui.graphics.Color.White, RoundedCornerShape(URadius))
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = strings.homeGreeting(uiState.displayName),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = BrandNavy
                        )
                        Text(
                            text = strings.homeReadyPrompt,
                            style = MaterialTheme.typography.bodySmall,
                            color = Neutral400
                        )
                    }
                    HomeUserAvatar(
                        avatarIcon = uiState.avatarIcon,
                        avatarImageUri = uiState.avatarImageUri,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onProfileClick
                            )
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(176.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        UInitiateMascot(modifier = Modifier.size(134.dp))
                        UserRankBadge(
                            rank = uiState.currentRank,
                            badgeSize = 84.dp,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = 102.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        HomeHeroStatCard(
                            value = uiState.totalPoints.toString(),
                            label = strings.homeTotalPointsLabel,
                            iconRes = UIcons.Menu.Stats,
                            containerColor = Navy500,
                            iconTint = Orange500
                        )
                        HomeHeroStatCard(
                            value = uiState.dayStreak.toString(),
                            label = strings.homeDayStreakLabel,
                            iconRes = UIcons.Cards.Session,
                            containerColor = Teal700,
                            iconTint = Orange500
                        )
                    }
                }
            }
        }

        if (uiState.continuePlaying.isNotEmpty()) {
            item { SectionHeader(strings.homeContinuePlaying) }
            itemsIndexed(uiState.continuePlaying, key = { _, item -> item.packId }) { index, item ->
                HomeContinuePackCard(
                    item = item,
                    accentIndex = index,
                    onClick = { onContinuePlayingClick(item.packId) }
                )
            }
        }

        if (uiState.continueStudying.isNotEmpty()) {
            item {
                if (uiState.continuePlaying.isNotEmpty()) {
                    Spacer(Modifier.height(8.dp))
                }
                SectionHeader(strings.homeContinueStudying)
            }
            itemsIndexed(uiState.continueStudying, key = { _, item -> item.packId }) { index, item ->
                HomeContinuePackCard(
                    item = item,
                    accentIndex = index,
                    onClick = { onContinueStudyingClick(item.packId) }
                )
            }
        }
    }
}
