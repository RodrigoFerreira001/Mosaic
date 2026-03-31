package dev.catbit.mosaic.client.logger

abstract class MosaicLogger(var level: Level = Level.INFO) {

    abstract fun display(level: Level, msg: String)

    fun debug(msg: String) {
        log(Level.DEBUG, msg)
    }

    fun info(msg: String) {
        log(Level.INFO, msg)
    }

    fun warn(msg: String) {
        log(Level.WARNING, msg)
    }

    fun error(msg: String) {
        log(Level.ERROR, msg)
    }

    fun isAt(lvl: Level): Boolean = this.level <= lvl

    fun log(lvl: Level, msg: String) {
        if (isAt(lvl)) display(lvl, msg)
    }

    inline fun log(lvl: Level, msg: () -> String) {
        if (isAt(lvl)) display(lvl, msg())
    }
}

enum class Level {
    DEBUG, INFO, WARNING, ERROR
}

