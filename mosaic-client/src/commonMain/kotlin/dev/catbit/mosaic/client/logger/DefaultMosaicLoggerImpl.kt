package dev.catbit.mosaic.client.logger

class DefaultMosaicLoggerImpl : MosaicLogger() {
    override fun display(level: Level, msg: String) {
        println("$level: $msg")
    }
}