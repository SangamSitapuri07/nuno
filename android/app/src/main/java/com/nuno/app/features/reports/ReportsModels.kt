package com.nuno.app.features.reports

import kotlinx.serialization.Serializable

@Serializable
data class ReportRequest(
    val playerId: String,
    val reason: String,
    val matchId: String? = null
)

@Serializable
data class BlockRequest(
    val playerId: String
)