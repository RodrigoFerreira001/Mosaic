package dev.catbit.mosaic.client.logger

/**
 * Logging abstraction every part of `mosaic-client` writes through — `EventRunningScope.log`/
 * `logError`, `BuilderScope.log`/`logError`, and every `EventRunner`'s own error handling all
 * eventually call into a `MosaicLogger`. Replaced app-wide via
 * `MosaicDependencyInjectionConfig.logger` (defaults to [DefaultMosaicLogger]) — an app wiring its
 * own crash-reporting/telemetry sink implements this once, and every log call in the framework
 * (built-in or custom) routes through it automatically, with no per-call-site change needed.
 *
 * @property level the minimum severity this logger actually emits — anything below it is silently
 * dropped by [isAt]/[log], never reaching [display] at all.
 */
abstract class MosaicLogger(
    val level: Level = Level.INFO
) {

    /** Emits one already-filtered log line. Implement this to route to `println`, a crash reporter,
     * a remote log sink, etc. — never called directly by framework code; always through [log]. */
    abstract fun display(level: Level, msg: String)

    /** Logs [msg] at [Level.DEBUG]. */
    fun debug(msg: String) {
        log(Level.DEBUG, msg)
    }

    /** Logs [msg] at [Level.INFO]. */
    fun info(msg: String) {
        log(Level.INFO, msg)
    }

    /** Logs [msg] at [Level.WARNING]. */
    fun warn(msg: String) {
        log(Level.WARNING, msg)
    }

    /** Logs [msg] at [Level.ERROR]. */
    fun error(msg: String) {
        log(Level.ERROR, msg)
    }

    /** Whether [lvl] would actually be emitted by this logger, given its own [level]. */
    fun isAt(lvl: Level): Boolean = this.level <= lvl

    /** Logs [msg] at [lvl], only calling [display] if [isAt] returns `true` for [lvl]. */
    fun log(lvl: Level, msg: String) {
        if (isAt(lvl)) display(lvl, msg)
    }

    /** Same as [log], but [msg] is only evaluated if [lvl] would actually be emitted — use this
     * overload when building the message string itself is non-trivial work (e.g. serializing a
     * payload), to avoid paying that cost on a level the logger would discard anyway. */
    inline fun log(lvl: Level, msg: () -> String) {
        if (isAt(lvl)) display(lvl, msg())
    }
}

/** Log severity, ordered from most to least verbose — [MosaicLogger.level] is the minimum level
 * that actually gets emitted; [NONE] as a logger's own [MosaicLogger.level] silences it entirely. */
enum class Level {
    DEBUG, INFO, WARNING, ERROR, NONE
}

