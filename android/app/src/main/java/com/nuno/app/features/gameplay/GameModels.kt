package com.nuno.app.features.gameplay

import kotlinx.serialization.Serializable

@Serializable
data class GameCard(
    val cardId: String,
    val type: String,
    val color: String,
    val value: String
)


@Serializable
data class GameState(
    val matchId: String,
    val roomId: String,
    val currentTurn: String,
    val direction: String,
    val currentColor: String,
    val currentValue: String,
    val status: String,
    val topCard: GameCard? = null,
    val drawPileCount: Int = 0,
    val myHand: List<GameCard> = emptyList(),
    val playerCardCounts: Map<String, Int> = emptyMap(),
    val players: List<String> = emptyList(),
    val playerNames: Map<String, PlayerInfo> = emptyMap(),
    val winner: String? = null,
    val totalTurns: Int = 0
)

@Serializable
data class PlayerInfo(
    val username: String,
    val level: Int
)

@Serializable
data class GameResult(
    val winner: String,
    val duration: Int,
    val totalTurns: Int
)