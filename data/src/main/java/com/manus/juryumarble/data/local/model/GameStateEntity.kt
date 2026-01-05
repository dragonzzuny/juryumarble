package com.manus.juryumarble.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity for saving game state
 *
 * Stores a serialized snapshot of the entire game state
 * to enable save/resume functionality
 */
@Entity(tableName = "saved_games")
data class GameStateEntity(
    @PrimaryKey
    val sessionId: String,

    // Serialized GameState as JSON
    val gameStateJson: String,

    // Metadata for user display
    val playerNames: String, // Comma-separated
    val currentPlayerIndex: Int,
    val currentTurn: Int,
    val totalPlayers: Int,

    // Timestamps
    val savedAt: Long,
    val startedAt: Long,

    // Quick access flags
    val isCompleted: Boolean = false
)
