package com.nuno.app.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nuno.app.core.common.UiState
import com.nuno.app.screens.home.*
import com.nuno.app.screens.lobby.*
import com.nuno.app.screens.game.*
import com.nuno.app.screens.social.*
import com.nuno.app.screens.profile.*
import com.nuno.app.screens.shop.*
import com.nuno.app.screens.social.SearchPlayerData
import com.nuno.app.screens.home.OnlineFriendData

sealed class GameScreen(val route: String) {
    data object Splash : GameScreen("splash")
    data object Login : GameScreen("login")
    data object Register : GameScreen("register")
    data object Home : GameScreen("home")
    data object PlayMenu : GameScreen("play_menu")
    data object CreateRoom : GameScreen("create_room")
    data object JoinRoom : GameScreen("join_room")
    data object RoomLobby : GameScreen("room_lobby")
    data object Matchmaking : GameScreen("matchmaking")
    data object Gameplay : GameScreen("gameplay")
    data object Friends : GameScreen("friends")
    data object Leaderboard : GameScreen("leaderboard")
    data object Profile : GameScreen("profile")
    data object Settings : GameScreen("settings")
    data object Shop : GameScreen("store")
    data object DailyRewards : GameScreen("daily_rewards")
    data object Notifications : GameScreen("notifications")
    data object SeasonPass : GameScreen("season_pass")
}

@Composable
fun GameNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val inviteVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.core.social.InviteViewModel>()
    val incomingInvite by inviteVm.incomingInvite.collectAsState()

    incomingInvite?.let { invite ->
        com.nuno.app.core.social.IncomingInviteDialog(
            invite = invite,
            onAccept = {
                inviteVm.acceptInvite()
                navController.navigate(GameScreen.RoomLobby.route) {
                    popUpTo(GameScreen.Home.route)
                }
            },
            onDismiss = { inviteVm.dismissInvite() }
        )
    }

    NavHost(
        navController = navController,
        startDestination = GameScreen.Splash.route
    ) {

        // ═══════════════════════════════════════
        // SPLASH
        // ═══════════════════════════════════════

        composable(GameScreen.Splash.route) {
            com.nuno.app.features.auth.SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(GameScreen.Login.route) {
                        popUpTo(GameScreen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(GameScreen.Home.route) {
                        popUpTo(GameScreen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // ═══════════════════════════════════════
        // LOGIN
        // ═══════════════════════════════════════

        composable(GameScreen.Login.route) {
            com.nuno.app.features.auth.LoginScreen(
                onLoginSuccess = {
                    navController.navigate(GameScreen.Home.route) {
                        popUpTo(GameScreen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(GameScreen.Register.route)
                }
            )
        }

        // ═══════════════════════════════════════
        // REGISTER
        // ═══════════════════════════════════════

        composable(GameScreen.Register.route) {
            com.nuno.app.features.auth.RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // ═══════════════════════════════════════
        // HOME
        // ═══════════════════════════════════════

        composable(GameScreen.Home.route) {
            val profileVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.profile.ProfileViewModel>()
            val profileState by profileVm.profileState.collectAsState()
            val rewardsVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.rewards.RewardsViewModel>()
            val friendsVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.friends.FriendsViewModel>()
            val friendsState by friendsVm.friendsState.collectAsState()

            val username = (profileState as? UiState.Success)?.data?.username ?: "Player"
            val level = (profileState as? UiState.Success)?.data?.level ?: 1
            val coins = (profileState as? UiState.Success)?.data?.coins ?: 0
            val rank = (profileState as? UiState.Success)?.data?.leaderboard?.tier ?: "BRONZE"

            val onlineFriends = (friendsState as? UiState.Success)?.data?.map { f ->
                OnlineFriendData(
                    userId = f.userId,
                    username = f.username,
                    status = when (f.status) {
                        "ONLINE" -> "Online"
                        "IN_MATCH" -> "In Game"
                        "IN_LOBBY" -> "In Lobby"
                        else -> "Offline"
                    },
                    isOnline = f.status != "OFFLINE"
                )
            } ?: emptyList()

            HomeScreen(
                username = username,
                level = level,
                coins = coins,
                gems = 0,
                rank = rank,
                onlineFriends = onlineFriends,
                onPlay = { navController.navigate(GameScreen.PlayMenu.route) },
                onNotifications = { navController.navigate(GameScreen.Notifications.route) },
                onDailyReward = {
                    rewardsVm.claimDailyReward()
                    navController.navigate(GameScreen.DailyRewards.route)
                },
                onInviteFriend = { friendId ->
                    // TODO: Create room and send invite
                },
                onNavigate = { route -> handleNavigation(navController, route) }
            )
        }

        // ═══════════════════════════════════════
        // PLAY MENU
        // ═══════════════════════════════════════

        composable(GameScreen.PlayMenu.route) {
            PlayMenuScreen(
                onBack = { navController.popBackStack() },
                onQuickMatch = { navController.navigate(GameScreen.Matchmaking.route) },
                onCreateRoom = { navController.navigate(GameScreen.CreateRoom.route) },
                onJoinRoom = { navController.navigate(GameScreen.JoinRoom.route) },
                onMatchHistory = { navController.navigate(GameScreen.Profile.route) },
                onNavigate = { route -> handleNavigation(navController, route) }
            )
        }

        // ═══════════════════════════════════════
// CREATE ROOM
// ═══════════════════════════════════════

        composable(GameScreen.CreateRoom.route) {
            val activity = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity
            val lobbyVm: com.nuno.app.features.lobby.LobbyViewModel = androidx.hilt.navigation.compose.hiltViewModel(activity)
            val lobbyState by lobbyVm.uiState.collectAsState()

            LaunchedEffect(lobbyState.room) {
                if (lobbyState.room != null) {
                    navController.navigate(GameScreen.RoomLobby.route) {
                        popUpTo(GameScreen.Home.route)
                    }
                }
            }

            CreateRoomScreen(
                onBack = { navController.popBackStack() },
                onCreate = { name, maxPlayers, rules ->
                    lobbyVm.createRoomWithPlayerCount(maxPlayers)
                }
            )
        }

        // ═══════════════════════════════════════
        // JOIN ROOM
        // ═══════════════════════════════════════

        composable(GameScreen.JoinRoom.route) {
            val activity = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity
            val lobbyVm: com.nuno.app.features.lobby.LobbyViewModel = androidx.hilt.navigation.compose.hiltViewModel(activity)
            val lobbyState by lobbyVm.uiState.collectAsState()

            LaunchedEffect(lobbyState.room) {
                if (lobbyState.room != null) {
                    navController.navigate(GameScreen.RoomLobby.route) {
                        popUpTo(GameScreen.Home.route)
                    }
                }
            }

            JoinRoomScreen(
                onBack = { navController.popBackStack() },
                onJoin = { code -> lobbyVm.joinRoomByCode(code) }
            )
        }

        // ═══════════════════════════════════════
        // ROOM LOBBY
        // ═══════════════════════════════════════

        composable(GameScreen.RoomLobby.route) {
            val activity = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity
            val lobbyVm: com.nuno.app.features.lobby.LobbyViewModel = androidx.hilt.navigation.compose.hiltViewModel(activity)
            val lobbyState by lobbyVm.uiState.collectAsState()
            val friendsVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.friends.FriendsViewModel>(activity)
            val friendsState by friendsVm.friendsState.collectAsState()
            val inviteVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.core.social.InviteViewModel>(activity)

            LaunchedEffect(lobbyState.gameStarted) {
                if (lobbyState.gameStarted && lobbyState.matchId != null) {
                    navController.navigate("${GameScreen.Gameplay.route}/${lobbyState.matchId}") {
                        popUpTo(GameScreen.Home.route)
                    }
                }
            }

            val players = lobbyState.room?.players?.map { player ->
                LobbyPlayerData(
                    userId = player.userId,
                    username = player.username,
                    level = 1,
                    isReady = player.isReady,
                    isHost = player.isHost,
                    ping = 45
                )
            } ?: emptyList()

            val onlineFriends = (friendsState as? UiState.Success)?.data?.map { f ->
                InvitableFriend(
                    userId = f.userId,
                    username = f.username,
                    isOnline = f.status != "OFFLINE"
                )
            } ?: emptyList()

            RoomLobbyScreen(
                roomCode = lobbyState.room?.roomCode ?: "-----",
                players = players,
                maxPlayers = lobbyState.room?.maxPlayers ?: 4,
                currentUserId = lobbyState.currentUserId,
                countdown = lobbyState.countdown,
                onlineFriends = onlineFriends,
                onBack = {
                    lobbyVm.leaveRoom()
                    navController.popBackStack()
                },
                onReady = { lobbyVm.toggleReady() },
                onStartGame = { lobbyVm.toggleReady() },
                onInviteFriend = { friendId ->
                    lobbyState.room?.roomCode?.let { code ->
                        inviteVm.sendInvite(friendId, code)
                    }
                },
                onKickPlayer = { lobbyVm.kickPlayer(it) }
            )
        }

        // ═══════════════════════════════════════
// MATCHMAKING
// ═══════════════════════════════════════

        composable(GameScreen.Matchmaking.route) {
            val matchVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.matchmaking.MatchmakingViewModel>()
            val matchState by matchVm.uiState.collectAsState()

            val mmStatus = when (matchState.status) {
                com.nuno.app.features.matchmaking.QueueStatus.IDLE -> MatchmakingStatus.IDLE
                com.nuno.app.features.matchmaking.QueueStatus.CONNECTING -> MatchmakingStatus.CONNECTING
                com.nuno.app.features.matchmaking.QueueStatus.SEARCHING -> MatchmakingStatus.SEARCHING
                com.nuno.app.features.matchmaking.QueueStatus.MATCH_FOUND -> MatchmakingStatus.MATCH_FOUND
                com.nuno.app.features.matchmaking.QueueStatus.GAME_STARTING -> MatchmakingStatus.GAME_STARTING
                com.nuno.app.features.matchmaking.QueueStatus.ERROR -> MatchmakingStatus.ERROR
            }

            // Navigate when game starts
            LaunchedEffect(matchState.status) {
                if (matchState.status == com.nuno.app.features.matchmaking.QueueStatus.GAME_STARTING) {
                    val matchId = matchState.matchId
                    if (!matchId.isNullOrEmpty()) {
                        navController.navigate("${GameScreen.Gameplay.route}/$matchId") {
                            popUpTo(GameScreen.Home.route)
                        }
                    }
                }
            }

            // Fallback: Navigate after match found + delay
            LaunchedEffect(matchState.matchId) {
                val matchId = matchState.matchId
                if (!matchId.isNullOrEmpty()) {
                    kotlinx.coroutines.delay(4000)
                    // Check if still on this screen
                    if (matchState.matchId != null) {
                        try {
                            navController.navigate("${GameScreen.Gameplay.route}/$matchId") {
                                popUpTo(GameScreen.Home.route)
                            }
                        } catch (e: Exception) {
                            // Already navigated
                        }
                    }
                }
            }

            NewMatchmakingScreen(
                status = mmStatus,
                elapsedSeconds = matchState.elapsedSeconds,
                errorMessage = matchState.errorMessage,
                onStartSearch = { mode, playerCount -> matchVm.joinQueue(mode, playerCount) },
                onCancelSearch = { matchVm.leaveQueue() },
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════════════════════════════════
        // GAMEPLAY
        // ═══════════════════════════════════════

        composable(
            route = "${GameScreen.Gameplay.route}/{matchId}",
            arguments = listOf(navArgument("matchId") { type = NavType.StringType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getString("matchId") ?: ""
            com.nuno.app.features.gameplay.GameplayScreen(
                matchId = matchId,
                onGameEnd = { winner ->
                    navController.navigate(GameScreen.Home.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ═══════════════════════════════════════
        // FRIENDS
        // ═══════════════════════════════════════

        composable(GameScreen.Friends.route) {
            val activity = androidx.compose.ui.platform.LocalContext.current as androidx.activity.ComponentActivity
            val friendsVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.friends.FriendsViewModel>(activity)
            val friendsState by friendsVm.friendsState.collectAsState()
            val requestsState by friendsVm.requestsState.collectAsState()
            val searchState by friendsVm.searchState.collectAsState()
            val actionMessage by friendsVm.actionMessage.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current

            LaunchedEffect(actionMessage) {
                actionMessage?.let {
                    android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show()
                    friendsVm.clearMessage()
                }
            }

            val friends = (friendsState as? UiState.Success)?.data?.map { f ->
                FriendData(
                    userId = f.userId,
                    username = f.username,
                    status = when (f.status) {
                        "ONLINE" -> "Online"
                        "IN_MATCH" -> "In Game"
                        "IN_LOBBY" -> "In Lobby"
                        else -> "Offline"
                    },
                    rating = 0,
                    isOnline = f.status != "OFFLINE"
                )
            } ?: emptyList()

            val requests = (requestsState as? UiState.Success)?.data?.map { r ->
                FriendRequestData(
                    requestId = r.id,
                    username = r.sender.username
                )
            } ?: emptyList()

            val searchResults = (searchState as? UiState.Success)?.data?.map { p ->
                SearchPlayerData(
                    userId = p.id,
                    username = p.username,
                    rating = p.rankPoints
                )
            } ?: emptyList()

            FriendsScreen(
                friends = friends,
                requests = requests,
                searchResults = searchResults,
                onBack = { navController.popBackStack() },
                onInvite = { },
                onJoin = { },
                onAcceptRequest = { friendsVm.acceptRequest(it) },
                onRejectRequest = { friendsVm.rejectRequest(it) },
                onSearch = { friendsVm.searchPlayers(it) },
                onSendFriendRequest = { friendsVm.sendRequest(it) },
                onNavigate = { route -> handleNavigation(navController, route) }
            )
        }

        // ═══════════════════════════════════════
        // LEADERBOARD
        // ═══════════════════════════════════════

        composable(GameScreen.Leaderboard.route) {
            val leaderboardVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.leaderboard.LeaderboardViewModel>()
            val globalState by leaderboardVm.globalState.collectAsState()

            val players = (globalState as? UiState.Success)?.data?.map { entry ->
                LeaderboardPlayerData(
                    rank = entry.rank,
                    username = entry.username,
                    points = entry.rating,
                    isYou = false
                )
            } ?: emptyList()

            LeaderboardScreen(
                players = players,
                seasonInfo = "Season ends in: 30d",
                onBack = { navController.popBackStack() },
                onNavigate = { route -> handleNavigation(navController, route) }
            )
        }

        // ═══════════════════════════════════════
        // PROFILE
        // ═══════════════════════════════════════

        composable(GameScreen.Profile.route) {
            val profileVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.profile.ProfileViewModel>()
            val profileState by profileVm.profileState.collectAsState()

            val profile = (profileState as? UiState.Success)?.data

            ProfileScreen(
                username = profile?.username ?: "Player",
                level = profile?.level ?: 1,
                rank = profile?.leaderboard?.tier ?: "BRONZE",
                coins = profile?.coins ?: 0,
                gems = 0,
                stats = mapOf(
                    "Matches Played" to (profile?.statistics?.gamesPlayed?.toString() ?: "0"),
                    "Matches Won" to (profile?.statistics?.gamesWon?.toString() ?: "0"),
                    "Win Rate" to "${((profile?.statistics?.winRate ?: 0f) * 100).toInt()}%",
                    "Best Streak" to (profile?.statistics?.longestWinStreak?.toString() ?: "0"),
                    "Cards Played" to (profile?.statistics?.cardsPlayed?.toString() ?: "0"),
                    "Cards Drawn" to (profile?.statistics?.cardsDrawn?.toString() ?: "0")
                ),
                onBack = { navController.popBackStack() },
                onNavigate = { route -> handleNavigation(navController, route) }
            )
        }

        // ═══════════════════════════════════════
        // SETTINGS
        // ═══════════════════════════════════════

        composable(GameScreen.Settings.route) {
            val authVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.auth.AuthViewModel>()

            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    authVm.logout()
                    navController.navigate(GameScreen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // ═══════════════════════════════════════
        // SHOP
        // ═══════════════════════════════════════

        composable(GameScreen.Shop.route) {
            val storeVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.store.StoreViewModel>()
            val storeState by storeVm.storeState.collectAsState()
            val balance by storeVm.balanceState.collectAsState()

            val items = (storeState as? UiState.Success)?.data?.map { item ->
                ShopItemData(
                    id = item.itemId,
                    name = item.name,
                    price = item.price,
                    currency = item.currency,
                    rarity = item.rarity,
                    type = item.type,
                    icon = when (item.type) {
                        "AVATAR" -> "👤"
                        "CARD_BACK" -> "🎴"
                        "EMOTE" -> "😀"
                        "BADGE" -> "🏅"
                        "VOICE_PACK" -> "🎙️"
                        "CARD_THEME" -> "🎨"
                        "PROFILE_BANNER" -> "🖼️"
                        "TITLE" -> "🏆"
                        else -> "🎁"
                    }
                )
            } ?: getSampleShopItems()

            ShopScreen(
                coins = balance,
                gems = 0,
                items = items,
                onBack = { navController.popBackStack() },
                onPurchase = { item ->
                    storeVm.purchaseItem(
                        com.nuno.app.features.store.StoreItem(
                            itemId = item.id,
                            name = item.name,
                            description = "",
                            type = item.type,
                            rarity = item.rarity,
                            price = item.price,
                            currency = item.currency
                        )
                    )
                },
                onNavigate = { route -> handleNavigation(navController, route) }
            )
        }

        // ═══════════════════════════════════════
        // DAILY REWARDS
        // ═══════════════════════════════════════

        composable(GameScreen.DailyRewards.route) {
            val rewardsVm = androidx.hilt.navigation.compose.hiltViewModel<com.nuno.app.features.rewards.RewardsViewModel>()
            val message by rewardsVm.message.collectAsState()
            val context = androidx.compose.ui.platform.LocalContext.current

            LaunchedEffect(message) {
                message?.let {
                    android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show()
                    rewardsVm.clearMessage()
                }
            }

            DailyRewardsScreen(
                rewards = getSampleDailyRewards(),
                onClaim = { rewardsVm.claimDailyReward() },
                onBack = { navController.popBackStack() }
            )
        }

        // ═══════════════════════════════════════
        // NOTIFICATIONS
        // ═══════════════════════════════════════

        composable(GameScreen.Notifications.route) {
            NotificationsScreen(
                notifications = getSampleNotifications(),
                onBack = { navController.popBackStack() },
                onMarkAllRead = { }
            )
        }

        // ═══════════════════════════════════════
        // SEASON PASS
        // ═══════════════════════════════════════

        composable(GameScreen.SeasonPass.route) {
            SeasonPassScreen(
                seasonName = "Season 12",
                endsIn = "6d 12h",
                currentLevel = 14,
                maxLevel = 100,
                xpProgress = 0.14f,
                rewards = getSampleSeasonRewards(),
                onBack = { navController.popBackStack() },
                onPremiumPass = { }
            )
        }
    }
}

// ═══════════════════════════════════════
// NAVIGATION HELPER
// ═══════════════════════════════════════

private fun handleNavigation(navController: NavHostController, route: String) {
    val targetRoute = when (route) {
        "home" -> GameScreen.Home.route
        "friends" -> GameScreen.Friends.route
        "leaderboard" -> GameScreen.Leaderboard.route
        "store" -> GameScreen.Shop.route
        "profile" -> GameScreen.Profile.route
        "settings" -> GameScreen.Settings.route
        "notifications" -> GameScreen.Notifications.route
        else -> return
    }

    navController.navigate(targetRoute) {
        popUpTo(GameScreen.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}

// ═══════════════════════════════════════
// SAMPLE DATA
// ═══════════════════════════════════════

private fun getSampleShopItems(): List<ShopItemData> {
    return listOf(
        ShopItemData("1", "Classic Pack", 200, icon = "🎴", rarity = "COMMON"),
        ShopItemData("2", "Neon Pack", 500, icon = "✨", rarity = "RARE"),
        ShopItemData("3", "Gold Pack", 1000, icon = "👑", rarity = "EPIC"),
        ShopItemData("4", "Diamond Pack", 1500, icon = "💎", rarity = "LEGENDARY"),
        ShopItemData("5", "Avatar Frame", 300, icon = "🖼️", rarity = "RARE"),
        ShopItemData("6", "Emote Pack", 150, icon = "😀", rarity = "COMMON"),
        ShopItemData("7", "Table Theme", 800, icon = "🎨", rarity = "EPIC"),
        ShopItemData("8", "Badge", 100, icon = "🏅", rarity = "COMMON")
    )
}

private fun getSampleDailyRewards(): List<DailyRewardData> {
    return listOf(
        DailyRewardData(1, "🪙", 100, isClaimed = true, isToday = false),
        DailyRewardData(2, "🪙", 100, isClaimed = true, isToday = false),
        DailyRewardData(3, "🪙", 250, isClaimed = false, isToday = true),
        DailyRewardData(4, "💎", 300, isClaimed = false, isToday = false),
        DailyRewardData(5, "🪙", 250, isClaimed = false, isToday = false),
        DailyRewardData(6, "🪙", 300, isClaimed = false, isToday = false),
        DailyRewardData(7, "💎", 500, isClaimed = false, isToday = false)
    )
}

private fun getSampleNotifications(): List<NotificationData> {
    return listOf(
        NotificationData("1", "👤", "Rohan", "Invited you to a room", "Just now", false),
        NotificationData("2", "👥", "Sneha", "Sent you a friend request", "2m ago", false),
        NotificationData("3", "🎁", "Daily Reward", "Your daily reward is ready", "5m ago", true),
        NotificationData("4", "🎉", "Event", "New event is live!", "10m ago", true)
    )
}

private fun getSampleSeasonRewards(): List<SeasonRewardData> {
    return (1..20).map { level ->
        SeasonRewardData(
            level = level,
            icon = when {
                level % 5 == 0 -> "💎"
                level % 3 == 0 -> "🎴"
                else -> "🪙"
            },
            isClaimed = level <= 14,
            isPremium = level % 4 == 0
        )
    }
}