package dev.catbit.mosaic.client.logger

class DefaultMosaicLogger(
    level: Level = Level.INFO
) : MosaicLogger(level = level) {
    override fun display(level: Level, msg: String) {
        println("$level: $msg")
    }
}