package dev.catbit.mosaic.client.logger

/**
 * The [MosaicLogger] `MosaicDependencyInjectionConfig.logger` defaults to when an app doesn't supply
 * its own — just `println("$level: $msg")`, no filtering beyond [MosaicLogger]'s own `level` cutoff,
 * no crash reporting, no remote sink. Meant for development; a real app typically supplies its own
 * `MosaicLogger` wired to whatever telemetry/crash-reporting tool it already uses.
 */
class DefaultMosaicLogger(
    level: Level = Level.INFO
) : MosaicLogger(level = level) {
    override fun display(level: Level, msg: String) {
        println("$level: $msg")
    }
}