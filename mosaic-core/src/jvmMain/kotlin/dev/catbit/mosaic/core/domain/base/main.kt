package dev.catbit.mosaic.core.domain.base

import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() {
    val coroutineScope = CoroutineScope(Dispatchers.Main)

    runBlocking {
        for (i in 0..100) {
            launch {
                delay(Random.nextInt(1, 3).seconds)
                println(i)
            }
        }
    }
}