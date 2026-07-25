package com.nuno.app.core.utils

object Constants {

    // Socket Events - Auth
    const val EVENT_AUTHENTICATE = "socket:authenticate"
    const val EVENT_AUTHENTICATED = "socket:authenticated"

    // Socket Events - Matchmaking
    const val EVENT_QUEUE_JOIN = "queue.join"
    const val EVENT_QUEUE_LEAVE = "queue.leave"
    const val EVENT_QUEUE_JOINED = "queue.joined"
    const val EVENT_QUEUE_LEFT = "queue.left"
    const val EVENT_MATCH_FOUND = "match.found"

    // Socket Events - Room
    const val EVENT_ROOM_CREATE = "room.create"
    const val EVENT_ROOM_JOIN = "room.join"
    const val EVENT_ROOM_LEAVE = "room.leave"
    const val EVENT_ROOM_CREATED = "room.created"
    const val EVENT_ROOM_JOINED = "room.joined"
    const val EVENT_ROOM_LEFT = "room.left"
    const val EVENT_ROOM_UPDATED = "room.updated"
    const val EVENT_ROOM_HOST_CHANGED = "room.hostChanged"
    const val EVENT_ROOM_COUNTDOWN = "room.countdown"
    const val EVENT_ROOM_COUNTDOWN_CANCELLED = "room.countdownCancelled"
    const val EVENT_ROOM_READY = "room.ready"
    const val EVENT_ROOM_KICK = "room.kick"
    const val EVENT_ROOM_KICKED = "room.kicked"

    // Socket Events - Game
    const val EVENT_GAME_STARTED = "game.started"
    const val EVENT_GAME_INITIAL_STATE = "game.initialState"
    const val EVENT_GAME_FINISHED = "game.finished"
    const val EVENT_GAME_SYNC_REQUEST = "game.syncRequest"
    const val EVENT_GAME_SYNC_STATE = "game.syncState"
    const val EVENT_CARD_PLAY = "card.play"
    const val EVENT_CARD_DRAW = "card.draw"
    const val EVENT_CARD_ACCEPTED = "card.accepted"
    const val EVENT_TURN_CHANGED = "turn.changed"
    const val EVENT_PLAYER_PLAYED_CARD = "player.playedCard"
    const val EVENT_PLAYER_DREW_CARD = "player.drewCard"
    const val EVENT_DIRECTION_CHANGED = "direction.changed"

    // Socket Events - Surrender / Rematch
    const val EVENT_SURRENDER = "game.surrender"
    const val EVENT_PLAYER_SURRENDERED = "player.surrendered"
    const val EVENT_REMATCH_REQUEST = "rematch.request"
    const val EVENT_REMATCH_ACCEPT = "rematch.accept"
    const val EVENT_REMATCH_DECLINE = "rematch.decline"
    const val EVENT_REMATCH_STARTED = "rematch.started"

    // Socket Events - Voice
    const val EVENT_VOICE_JOIN = "voice.join"
    const val EVENT_VOICE_LEAVE = "voice.leave"
    const val EVENT_VOICE_OFFER = "voice.offer"
    const val EVENT_VOICE_ANSWER = "voice.answer"
    const val EVENT_VOICE_ICE = "voice.iceCandidate"

    // Socket Events - Chat & Emotes
    const val EVENT_CHAT_SEND = "chat.send"
    const val EVENT_CHAT_RECEIVED = "chat.received"
    const val EVENT_QUICK_CHAT = "chat.quick"
    const val EVENT_EMOTE_SEND = "emote.send"
    const val EVENT_EMOTE_RECEIVED = "emote.received"

    // Socket Events - Spectator
    const val EVENT_SPECTATE_JOIN = "spectate.join"
    const val EVENT_SPECTATE_LEAVE = "spectate.leave"
    const val EVENT_SPECTATE_STATE = "spectate.state"

    // Socket Events - System
    const val EVENT_ERROR = "error"

    // Game Modes
    const val MODE_CASUAL = "CASUAL"
    const val MODE_RANKED = "RANKED"
    const val MODE_PRIVATE = "PRIVATE"

    // Card Colors
    const val COLOR_RED = "RED"
    const val COLOR_BLUE = "BLUE"
    const val COLOR_GREEN = "GREEN"
    const val COLOR_YELLOW = "YELLOW"
    const val COLOR_WILD = "WILD"

    // Quick Chat Messages
    val QUICK_CHAT_MESSAGES = mapOf(
        "GG" to "Good Game!",
        "GL" to "Good Luck!",
        "WP" to "Well Played!",
        "NM" to "Nice Move!",
        "OOPS" to "Oops!",
        "HURRY" to "Hurry Up!",
        "THANKS" to "Thanks!",
        "SORRY" to "Sorry!"
    )

    // Emotes
    val EMOTES = listOf("😀", "😂", "😎", "😢", "😡", "👍", "👎", "❤️", "🎉", "🔥")

    // Report Reasons
    val REPORT_REASONS = listOf(
        "Harassment",
        "Offensive language",
        "Cheating",
        "Griefing",
        "Inappropriate username",
        "Spamming",
        "Other"
    )

    // Screen Routes
    const val ROUTE_SPLASH = "splash"
    const val ROUTE_LOGIN = "login"
    const val ROUTE_REGISTER = "register"
    const val ROUTE_HOME = "home"
    const val ROUTE_PROFILE = "profile"
    const val ROUTE_SETTINGS = "settings"
    const val ROUTE_MATCHMAKING = "matchmaking"
    const val ROUTE_LOBBY = "lobby"
    const val ROUTE_GAMEPLAY = "gameplay"
    const val ROUTE_RESULTS = "results"
    const val ROUTE_FRIENDS = "friends"
    const val ROUTE_LEADERBOARD = "leaderboard"
    const val ROUTE_STORE = "store"
    const val EVENT_UNO_CALL = "uno.call"
    const val EVENT_UNO_CALLED = "uno.called"
    const val EVENT_UNO_CATCH = "uno.catch"
    const val EVENT_UNO_PENALTY = "uno.penalty"
    const val EVENT_CHALLENGE_WILD_FOUR = "challenge.wildDrawFour"
    const val EVENT_CHALLENGE_RESULT = "challenge.result"
    const val EVENT_INVITE_SEND = "invite.send"
    const val EVENT_INVITE_RECEIVED = "invite.received"
    const val EVENT_INVITE_ACCEPT = "invite.accept"
    const val EVENT_INVITE_SENT = "invite.sent"
}