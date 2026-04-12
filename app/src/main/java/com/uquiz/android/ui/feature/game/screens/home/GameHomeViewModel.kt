package com.uquiz.android.ui.feature.game.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.uquiz.android.domain.attempts.enums.AttemptMode
import com.uquiz.android.domain.attempts.repository.AttemptRepository
import com.uquiz.android.domain.content.enums.DifficultyLevel
import com.uquiz.android.domain.content.repository.PackRepository
import com.uquiz.android.domain.stats.projection.PackGameCard
import com.uquiz.android.ui.feature.game.screens.home.model.GameHomeUiEvent
import com.uquiz.android.ui.feature.game.screens.home.model.GameHomeUiState
import com.uquiz.android.ui.feature.game.utils.computeAverageDifficulty
import com.uquiz.android.ui.feature.game.utils.computeExpectedPlayTime
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * ViewModel de la pantalla principal del Game mode.
 *
 * Combina la lista reactiva de packs con el progreso activo de partidas en juego para
 * construir las tarjetas de pack. Computa y cachea la dificultad media y el tiempo estimado
 * de cada pack para evitar re-cargas entre actualizaciones del flujo.
 */
class GameHomeViewModel(
    private val packRepository: PackRepository,
    private val attemptRepository: AttemptRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameHomeUiState())
    val uiState: StateFlow<GameHomeUiState> = _uiState.asStateFlow()
    private val events = Channel<GameHomeUiEvent>(Channel.BUFFERED)
    val uiEvents = events.receiveAsFlow()

    // Cache: packId → (averageDifficulty, expectedPlayTimeMs)
    private val packMetricsCache = mutableMapOf<String, Pair<DifficultyLevel, Long>>()

    // Lista completa de cards (sin filtrar) para búsqueda sobre to-do el catálogo
    private var allGameCards: List<PackGameCard> = emptyList()

    init {
        viewModelScope.launch {
            combine(
                packRepository.observeAllWithQuestionCounts(),
                attemptRepository.observeActivePackProgress(AttemptMode.GAME),
            ) { packs, progress ->
                packs to progress
            }.collect { (packs, progress) ->
                val progressByPackId = progress.associateBy { it.packId }

                // Carga en paralelo los packs sin métricas cacheadas
                val uncached = packs.filter { !packMetricsCache.containsKey(it.pack.id) }
                if (uncached.isNotEmpty()) {
                    coroutineScope {
                        uncached
                            .map { packWithCount ->
                                async {
                                    val questions = packRepository.getWithQuestions(packWithCount.pack.id)
                                    packMetricsCache[packWithCount.pack.id] =
                                        computeAverageDifficulty(questions) to computeExpectedPlayTime(questions)
                                }
                            }.forEach { it.await() }
                    }
                }

                val gameCards =
                    packs.map { packWithCount ->
                        val packId = packWithCount.pack.id
                        val (difficulty, timeMs) =
                            packMetricsCache[packId]
                                ?: (DifficultyLevel.MEDIUM to (packWithCount.questionCount * 20_000L))
                        val activeProgress = progressByPackId[packId]
                        PackGameCard(
                            pack = packWithCount.pack,
                            questionCount = packWithCount.questionCount,
                            averageDifficulty = difficulty,
                            expectedPlayTimeMs = timeMs,
                            activeAttemptId = activeProgress?.attemptId,
                            answeredCount = activeProgress?.answeredCount ?: 0,
                        )
                    }

                allGameCards = gameCards
                // Solo se muestran packs con preguntas: sin preguntas no hay partida posible.
                val activeGames = gameCards.filter { it.hasActiveAttempt && it.questionCount > 0 }
                val recentPacks =
                    gameCards
                        .filter { !it.hasActiveAttempt && it.questionCount > 0 }
                        .sortedByDescending { it.pack.updatedAt }
                        .take(10)

                _uiState.value =
                    GameHomeUiState(
                        isLoading = false,
                        searchQuery = _uiState.value.searchQuery,
                        activeGames = activeGames,
                        recentPacks = recentPacks,
                        searchResults = filterCards(_uiState.value.searchQuery, gameCards),
                        hasPlayablePacks = gameCards.any { it.questionCount > 0 },
                    )
            }
        }
    }

    /** Actualiza el texto de búsqueda y recalcula los resultados filtrados. */
    fun onSearchQueryChange(query: String) {
        _uiState.value =
            _uiState.value.copy(
                searchQuery = query,
                searchResults = filterCards(query, allGameCards),
            )
    }

    /** Selecciona un pack jugable al azar y solicita abrir su flujo de introducción. */
    fun onRandomPlayRequested() {
        val randomPack =
            allGameCards
                .filter { it.questionCount > 0 }
                .randomOrNull(Random.Default)
                ?: return

        viewModelScope.launch {
            events.send(GameHomeUiEvent.OpenRandomPack(randomPack.pack.id))
        }
    }

    private fun filterCards(
        query: String,
        cards: List<PackGameCard>,
    ): List<PackGameCard> {
        if (query.isBlank()) return emptyList()
        return cards.filter { it.pack.title.contains(query, ignoreCase = true) }
    }

    class Factory(
        private val packRepository: PackRepository,
        private val attemptRepository: AttemptRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            GameHomeViewModel(packRepository, attemptRepository) as T
    }
}
