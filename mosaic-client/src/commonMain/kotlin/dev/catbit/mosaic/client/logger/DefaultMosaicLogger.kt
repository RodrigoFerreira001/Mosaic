package dev.catbit.mosaic.client.logger

class DefaultMosaicLogger : MosaicLogger() {
    override fun display(level: Level, msg: String) {
        println("$level: $msg")
    }
}