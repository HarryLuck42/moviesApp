package com.corp.luqman.movielisting.utils

object Const {
    init {
        System.loadLibrary("native-lib")
    }

    private val environmentStage = 1
    private external fun appUrl(environmentStage: Int): String
    private external fun imageUrl(environmentStage: Int): String
//    private external fun appLoginUrl(environmentStage: Int): String
//    private external fun appPhotoUrl(environmentStage: Int): String
//    private external fun pubKey(environmentStage: Int): String
//    private external fun getClient(environmentStage: Int): String
//    private external fun hostName(environmentStage: Int): String
//    private external fun certPin(environmentStage: Int): String
//    private external fun certPin2(environmentStage: Int): String
//    private external fun appCode(environmentStage: Int): String

    val appUrl = appUrl(environmentStage)
    val imageUrlbase = imageUrl(environmentStage)
    val apikey = "c150d6ed0761c4202fb739070e3486c7"
    val language = "en-US"
    val POPULAR_PATH = "popular"
    val NOW_PLAYING_PATH = "now_playing"
    val UPCOMING_PATH = "upcoming"
//    val appLoginUrl = appLoginUrl(environmentStage)
//    val appCode = appCode(environmentStage)
//    val appPhotoUrl = appPhotoUrl(environmentStage)
//    val puK = pubKey(environmentStage)
//    val clientBasic = getClient(environmentStage)
//    val hostName = hostName(environmentStage)
//    val certPin = certPin(environmentStage)
//    val certPin2 = certPin2(environmentStage)

    //Enum ViewPager
    var WALLBOARD_VIEWPAGER = 0
    var INBOX_VIEWPAGER = 1
}