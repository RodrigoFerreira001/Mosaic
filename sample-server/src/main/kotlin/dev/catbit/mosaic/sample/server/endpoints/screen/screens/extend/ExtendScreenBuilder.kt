package dev.catbit.mosaic.sample.server.endpoints.screen.screens.extend

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.UnderConstructionBadge
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.placement.alignToTopEnd
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Box
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyLarge
import dev.catbit.mosaic.server.builder.typography.typographyDisplayMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import io.ktor.server.routing.RoutingCall

/**
 * Every code sample here is lifted near-verbatim from `skill/mosaic-client/SKILL.md` (real
 * `Checkbox`/`OpenExternalLink`/`GetData` source) and from this repo's own third-party extensions
 * (`CodeViewer`, `AdaptiveNavigation`) — nothing invented, nothing abstracted into a "[Name]"
 * placeholder that could drift from the actual API.
 */
private const val THREE_LAYER_CODE = """
Schema     mosaic-core     data class, @Serializable        — the wire contract
Builder    mosaic-server    internal class + public fun      — the DSL function you call
Definition mosaic-client    object                            — binds the schema's KClass to the two below
Renderer/  mosaic-client    object (tiles) / object (events)  — Compose rendering, or execution logic
Runner
Holder     mosaic-client    class                             — one live instance per tile/event in the tree
HolderBuilder mosaic-client object                            — builds a Holder from a freshly-deserialized Schema
"""

private const val TILE_DEFINITION_CODE = """
object CheckboxTileDefinition : TileDefinition<CheckboxTileSchema> {
    override val tileSchemaClass = CheckboxTileSchema::class
    override val tileRenderer = CheckboxTileRenderer
    override val tileHolderBuilder = CheckboxTileHolderBuilder
}
"""

private const val TILE_HOLDER_CODE = """
class CheckboxTileHolder(
    override val id: String,
    override var tile: CheckboxTileSchema,
    override val events: MutableList<EventHolder<*>>,
    override val tiles: MutableList<TileHolder<*>>? = null
) : TileHolder<CheckboxTileSchema>() {

    override fun getTileSchema() = tile.copy(events = events.map { it.get() })

    override fun TileEventScope.onTileEvent(event: TileEvent) {
        when (event) {
            is CheckboxTileEvents.OnCheckChanged -> tile = tile.copy(checked = event.isChecked)
        }
    }

    override fun produceValueWithKey(key: String) = mapOf(key to tile.checked)
}
"""

private const val TILE_RENDERER_CODE = """
object CheckboxTileRenderer : TileRenderer<CheckboxTileSchema> {

    @Composable
    override fun TileRenderingScope.Render(tileSchema: CheckboxTileSchema) {
        with(tileSchema) {
            Checkbox(
                modifier = Modifier.visible(isVisible()).styledWith(style),
                enabled = enabled,
                checked = checked,
                onCheckedChange = { checked ->
                    // Local state (synchronous, always finishes first) AND the remote trigger,
                    // fired on the same tap — the pattern every stateful built-in tile follows.
                    triggerEvent(if (checked) EventTriggers.onCheck() else EventTriggers.onUncheck())
                    triggerEvent(EventTriggers.onCheckChanged())
                    dispatchEvent(CheckboxTileEvents.OnCheckChanged(checked))
                }
            )
        }
    }
}
"""

private const val EVENT_DEFINITION_AND_RUNNER_CODE = """
object OpenExternalLinkEventDefinition : EventDefinition<OpenExternalLinkEventSchema> {
    override val eventSchemaClass = OpenExternalLinkEventSchema::class
    override val eventRunner = OpenExternalLinkEventRunner
    override val eventHolderBuilder = OpenExternalLinkEventHolderBuilder
}

object OpenExternalLinkEventRunner : EventRunner<OpenExternalLinkEventSchema> {
    override suspend fun EventRunningScope.runEvent(event: OpenExternalLinkEventSchema) {
        runSafely(
            onError = { throwable ->
                onTrigger(EventTriggers.onFailure(), data = throwable)
                logError(tag = "OpenExternalLinkEventRunner", throwable = throwable)
            }
        ) {
            openExternalLink(event.url)
            onTrigger(EventTriggers.onSuccess())
        }
    }
}

// IO-bound work (network, disk) wraps the body in withContext(Dispatchers.IO) instead —
// see GetDataEventRunner in skill/mosaic-client/SKILL.md §5 for that variant.
"""

private const val REGISTRATION_CODE = """
// sample-client/.../App.kt — this exact app's real registration call
MosaicApplication(
    applicationId = "MosaicSample",
    baseUrl = "http://192.168.3.105:9090",
    dependencyInjectionConfig = mosaicDependencyInjectionConfig(
        tileDefinitions = listOf(
            AdaptiveNavigationTileDefinition,
            CodeViewerTileDefinition,
        ),
        eventTriggerDefinition = listOf(
            OnAdaptiveNavigationItemClickEventTriggerDefinition
        ),
    ),
    // ...themeConfig, appSplash
)
"""

private const val MECHANISMS_CODE = """
get<NetworkParametersHolder>()   // stage a value for the *next* network call
get<DataMailer>()                // move a value between two unrelated screens
get<CancellableEventsHolder>()   // make a long-running chain stoppable elsewhere
get<NavigatorsHolder>()          // trigger navigation from any tree depth
get<ScreenDataHolder>()          // already EventRunningScope.screenDataHolder
getAll<DataProcessor>()          // resolve every registered DataProcessor, match by id yourself
"""

private const val REAL_EXAMPLES_CODE = """
CodeViewer — minimal, no custom trigger (6 files total):
  sample-core/.../schemas/tiles/code/CodeViewerTileSchema.kt
  sample-server/.../dsl/tiles/code/CodeViewerTileSchemaBuilder.kt
  sample-client/.../sdui/tiles/code/code_viewer/
      CodeViewerTileDefinition.kt
      CodeViewerTileHolder.kt
      CodeViewerTileHolderBuilder.kt
      CodeViewerTileRenderer.kt

AdaptiveNavigation — complex, ships its own custom EventTrigger:
  sample-core/.../schemas/tiles/navigation/AdaptiveNavigationTileSchema.kt
  sample-core/.../schemas/triggers/OnAdaptiveNavigationItemClickEventTrigger.kt
  sample-server/.../dsl/tiles/navigation/AdaptiveNavigationTileSchemaBuilder.kt
  sample-client/.../sdui/tiles/navigation/adaptive_navigation/
      AdaptiveNavigationTileDefinition.kt
      AdaptiveNavigationTileEvents.kt
      AdaptiveNavigationTileHolder.kt
      AdaptiveNavigationTileHolderBuilder.kt
      AdaptiveNavigationTileRenderer.kt
  sample-client/.../sdui/triggers/OnAdaptiveNavigationItemClickEventTriggerDefinition.kt

This very code sample is itself rendered by CodeViewer — a third-party tile, registered exactly
like any built-in one. Nothing in the built-in vocabulary is privileged.
"""

private data class ExtendStep(
    val title: String,
    val intro: String,
    val code: String,
    val codeIsKotlin: Boolean = true,
)

private val extendSteps = listOf(
    ExtendStep(
        title = "The three-layer pattern",
        intro = "Every Tile and Event — built-in or third-party — splits across the same three " +
            "layers. Definition/Renderer/Runner are always object (stateless singletons); Holder " +
            "is always a class (one live instance per tile/event in the tree).",
        code = THREE_LAYER_CODE.trim(),
        codeIsKotlin = false,
    ),
    ExtendStep(
        title = "A custom Tile, from real source — Checkbox's Definition",
        intro = "TileDefinition<T> requires exactly these 3 properties. This is the actual " +
            "built-in Checkbox — the walkthrough in skill/mosaic-client/SKILL.md §4 uses it as " +
            "the template because it has local state AND fires a remote trigger, the most " +
            "complete case to learn from.",
        code = TILE_DEFINITION_CODE.trim(),
    ),
    ExtendStep(
        title = "...its Holder — local TileEvent, synchronous",
        intro = "onTileEvent(event: TileEvent) is where a tile's own local, instantly-visible " +
            "state lives — checked here, applied synchronously, independent of whatever the " +
            "remote trigger chain ends up doing. produceValueWithKey exposes this same state to " +
            "GetData/EvaluateData reading it via the Tile data source.",
        code = TILE_HOLDER_CODE.trim(),
    ),
    ExtendStep(
        title = "...and its Renderer — local + remote, same tap",
        intro = "Every stateful built-in tile fires both on the same interaction: triggerEvent " +
            "(remote, scheduled onto the screen's coroutine scope) and dispatchEvent (local, " +
            "resolved synchronously — it always finishes and recomposes first). " +
            "Modifier.styledWith(style) is the real helper behind every built-in tile's spacing/" +
            "click/border stacking — always prefer it over hand-rolling the modifier chain.",
        code = TILE_RENDERER_CODE.trim(),
    ),
    ExtendStep(
        title = "A custom Event, from real source",
        intro = "OpenExternalLinkEventRunner is the synchronous/simple template: wrap the real " +
            "work in runSafely(onError) { ... } and route failures to onFailure — the pattern " +
            "every built-in EventRunner follows.",
        code = EVENT_DEFINITION_AND_RUNNER_CODE.trim(),
    ),
    ExtendStep(
        title = "Registering with the framework",
        intro = "There is no serializer to touch and no internal list to append to — " +
            "MosaicSerializer is built for you, automatically, from exactly the Definitions you " +
            "pass here. This is the real, complete registration call from this app's own " +
            "sample-client/.../App.kt.",
        code = REGISTRATION_CODE.trim(),
    ),
    ExtendStep(
        title = "Mechanisms a custom Tile/Event can reach",
        intro = "Every mechanism the built-in vocabulary uses is a plain Koin single — reach it " +
            "with get<T>() from EventRunningScope/BuilderScope, or koinInject<T>() from a " +
            "@Composable in TileRenderingScope. Full table in skill/mosaic-client/SKILL.md §8.",
        code = MECHANISMS_CODE.trim(),
        codeIsKotlin = false,
    ),
    ExtendStep(
        title = "Real third-party extensions, in this very repo",
        intro = "Not a hypothetical — sample-client ships two tiles built exactly this way, " +
            "registered through the same mosaicDependencyInjectionConfig call shown above.",
        code = REAL_EXAMPLES_CODE.trim(),
        codeIsKotlin = false,
    ),
)

private fun TileSchemaBuilderScope.ExtendStepSection(step: ExtendStep) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(
            text = step.title,
            typography = typographyHeadlineSmall()
        )
        SimpleText(
            text = step.intro,
            typography = typographyBodyLarge(),
            color = color(themeColorOnSurfaceVariant())
        )
        CodeViewer(
            code = step.code,
            language = if (step.codeIsKotlin) CodeViewerTileSchema.Language.KOTLIN else CodeViewerTileSchema.Language.DEFAULT,
            theme = CodeViewerTileSchema.Theme.ATOM_ONE,
            style = { size(width = fillHorizontally(), height = wrapVertically()) }
        )
    }
}

object ExtendScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "extend"

    override suspend fun RoutingCall.build() = Screen(id = "extend") {
        Column(
            id = "extend_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 16, top = 16, bottom = 32)
            },
            arrangement = arrangeVerticallySpacedBy(36),
            scrollable = true
        ) {
            // Hero: dark card topped by a big overlapping-blob illustration, same DNA as
            // About/Tiles/Events/Get started/Mechanisms.
            Column(
                id = "extend_hero",
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                    clip(roundedCornerShape(all = 28))
                    background(color(themeColorInverseSurface()))
                }
            ) {
                Box(
                    alignment = alignToTopEnd(),
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorErrorContainer()))
                    }
                ) {
                    UnderConstructionBadge()
                }
                Column(
                    style = {
                        size(width = fillHorizontally(), height = wrapVertically())
                        padding(horizontal = 24, top = 20, bottom = 24)
                    },
                    arrangement = arrangeVerticallySpacedBy(8)
                ) {
                    SimpleText(
                        text = "Extend",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "A custom Tile or Event is built exactly like a built-in one — same three " +
                            "layers, registered through the same mosaicDependencyInjectionConfig call. " +
                            "Every sample below is real source, not an invented template.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            extendSteps.forEach { step ->
                ExtendStepSection(step)
            }
        }
    }
}
