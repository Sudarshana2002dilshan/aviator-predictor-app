package com.example.data.model

data class BettingSite(
  val id: String,
  val name: String,
  val tag: String,
  val serverPingMs: Int,
  val accuracyRate: String,
  val primaryColorHex: Long,
  val isPopular: Boolean = true
) {
  companion object {
    val SUPPORTED_SITES = listOf(
      BettingSite("1xbet", "1xBet", "Global #1", 24, "99.4%", 0xFF0072CE),
      BettingSite("mostbet", "Mostbet", "Fast Sync", 18, "99.1%", 0xFFFF5722),
      BettingSite("1win", "1Win", "High Odds", 21, "98.9%", 0xFF1E88E5),
      BettingSite("parimatch", "Parimatch", "Live Radar", 15, "99.6%", 0xFFFFD600),
      BettingSite("melbet", "Melbet", "Instant Signal", 28, "98.7%", 0xFFF57C00),
      BettingSite("pinup", "Pin-Up", "VIP Connect", 22, "99.2%", 0xFFE91E63),
      BettingSite("22bet", "22Bet", "Pro Multiplier", 29, "98.5%", 0xFF00897B),
      BettingSite("betwinner", "BetWinner", "Direct API", 20, "99.0%", 0xFF43A047),
      BettingSite("888starz", "888Starz", "Crypto Server", 17, "99.5%", 0xFFD81B60),
      BettingSite("1xbit", "1xBit", "Anon Node", 26, "98.8%", 0xFF8E24AA)
    )
  }
}
