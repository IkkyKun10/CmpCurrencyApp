package com.riezki.cmpcurrencyapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform