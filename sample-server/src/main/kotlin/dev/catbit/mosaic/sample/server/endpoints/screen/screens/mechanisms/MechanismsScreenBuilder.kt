package dev.catbit.mosaic.sample.server.endpoints.screen.screens.mechanisms

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.UnderConstructionBadge
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorPrimaryContainer
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
 * Every shape and API on this page mirrors `skill/mosaic/SKILL.md` §4 (explicit mechanisms) and §5
 * (implicit conventions), and the real receiver classes in `mosaic-client` — not an abstracted
 * summary. Same editorial layout as About/Tiles/Events/Get started/Extend: dark blob hero, then each
 * mechanism as headline + prose + code, directly on the page background.
 */
private data class MechanismTopic(
    val title: String,
    val description: String,
    val code: String?,
    val codeIsKotlin: Boolean = true,
)

private val mechanismTopics = listOf(
    MechanismTopic(
        title = "DataSourceSchema — where server-authored data reads/writes point to",
        description = "Every GetData/UpdateData/RemoveData reading or update targets one of these. " +
            "Screen* is in-memory and dies with the screen; *DataBase is persistent (SQLite); " +
            "Application* is in-memory but shared app-wide; Tile reads a value a specific tile " +
            "exposes (read-only) and ScreenNavigationData reads the arguments the screen was " +
            "navigated to with (also read-only — no add/remove counterpart, since navigation " +
            "arguments are fixed for a screen instance's whole lifetime).",
        code = """
            screenPlainData()                  // in-memory, screen-scoped, flat key-value
            screenSegmentedData("form")         // in-memory, screen-scoped, per segment
            screenNavigationData()              // read-only — arguments from the Navigate that opened this screen
            applicationPlainData()              // in-memory, app-wide scope
            applicationSegmentedData("id")      // in-memory, app-wide scope, per segment
            plainDataBase()                     // persistent SQLite, flat key-value
            segmentedDataBase("auth")           // persistent SQLite, per segment
            tile("tf_email", "text")            // value exposed by a tile's produceValueWithKey (read-only)
        """.trimIndent()
    ),
    MechanismTopic(
        title = "AccessModeSchema — how much of the source to touch",
        description = "Controls how many records a GetData read or an UpdateData/RemoveData write " +
            "affects within the chosen source.",
        code = """
            fullAccessMode()                          // every record in the source
            singleAccessMode("email")                 // one specific record
            batchAccessMode(listOf("email", "name"))  // several records at once
        """.trimIndent()
    ),
    MechanismTopic(
        title = "ScreenDataHolder — a screen's own in-memory store",
        description = "What screenPlainData()/screenSegmentedData()/screenNavigationData() " +
            "actually resolve to at runtime — one fresh instance per screen, created by the " +
            "MosaicModules.stateHolder view model factory and discarded the moment the screen " +
            "leaves the back stack. Reached directly as EventRunningScope.screenDataHolder — " +
            "there's no get<ScreenDataHolder>() Koin lookup, since it's per-screen rather than a " +
            "single app-wide binding (that's ApplicationDataHolder instead, reachable via " +
            "get<ApplicationDataHolder>() and surviving navigation).",
        code = """
            interface ScreenDataHolder {
                fun addPlainData(data: Any?, dataId: String)
                fun addSegmentedData(data: Any?, segmentId: String, dataId: String)
                fun getPlainData(dataId: String): Any?
                fun getSegmentedData(dataId: String, segmentId: String): Any?
                fun getNavigationData(dataId: String): Any?   // read-only, no add/remove counterpart
                fun wipePlainData()
                fun wipeSegmentedData(segmentId: String)
            }
        """.trimIndent()
    ),
    MechanismTopic(
        title = "EventRunningScope — the real context every EventRunner receives",
        description = "One fresh instance per event execution. incomingData is the payload from " +
            "whatever fired this event; onTrigger scans this event's own child events for one " +
            "matching the given trigger and runs it, passing data along as the child's new " +
            "incomingData. get/getOrNull/getAll resolve from the same Koin scope every built-in " +
            "EventRunner uses — how a custom runner reaches any mechanism on this page.",
        code = """
            data class EventRunningScope(
                val screenId: String,
                val triggerOwner: EventSchema,
                val incomingData: Any? = null,
                val tilesEditor: TilesEditor,
                val tilesEventDispatcher: TilesEventDispatcher,
                val tilesOverlaysEditor: TilesOverlaysEditor,
                val tilesValueProducer: TilesValueProducer,
                val screenDataHolder: ScreenDataHolder,
                val screenBehaviorsHolder: ScreenBehaviorsHolder,
            ) {
                suspend fun onTrigger(eventTrigger: EventTrigger, data: Any? = null)
                suspend fun runEventInline(eventSchema: EventSchema, data: Any? = null)
                suspend fun runEventsInline(eventsSchema: List<EventSchema>, data: Any? = null)
                fun broadcastData(data: ScreenTilesBroadcastData)

                inline fun <reified T : Any> get(...): T
                inline fun <reified T : Any> getOrNull(...): T?
                inline fun <reified T : Any> getAll(): List<T>

                fun logError(throwable: Throwable, tag: String)
                fun log(level: Level, msg: String)
            }
        """.trimIndent()
    ),
    MechanismTopic(
        title = "TileRenderingScope — the real context every TileRenderer receives",
        description = "triggerEvent fires this tile's own remote events (its onClick, say) in " +
            "reaction to a user interaction — scheduled onto the screen's coroutine scope. " +
            "dispatchEvent applies a tile's own local, instantly-visible state change " +
            "synchronously, via that tile's Holder.onTileEvent override — it always finishes " +
            "before triggerEvent's chain does (see Checkbox on the Extend page for the pattern " +
            "every stateful built-in tile follows). dispatchGroupEvent reaches every tile in the " +
            "whole screen whose Holder opts in — RadioButton's mutual-exclusion mechanism.",
        code = """
            data class TileRenderingScope(
                val tileId: String,
                val events: ImmutableList<EventSchema>?,
                val onEvent: (UIEvent) -> Unit
            ) {
                fun dispatchEvent(tileEvent: TileEvent)
                fun dispatchGroupEvent(tileGroupEvent: TileGroupEvent)
                fun triggerEvent(trigger: EventTrigger, data: Any? = null)

                @Composable fun RenderChild(tileSchema: TileSchema)
                @Composable fun RenderChildren(tileSchemas: ImmutableList<TileSchema>)
            }
        """.trimIndent()
    ),
    MechanismTopic(
        title = "TilesEditor — mutating the live tile tree by id",
        description = "The collaborator behind EventRunningScope.tilesEditor, and in turn behind " +
            "AddTiles/RemoveTiles/ReplaceTiles/WipeTiles/UpdateTiles/CheckIfTileContainsChildren/" +
            "GetTileChildrenCount. Every mutating method returns Result<Unit> rather than " +
            "throwing or silently no-op-ing: addressing an id that doesn't currently exist fails " +
            "the Result (a TileNotFoundException), letting the caller decide how to react — " +
            "typically by firing its own onFailure. Root-level and grouped-container overloads " +
            "both exist for add/remove/replace.",
        code = """
            interface TilesEditor {
                fun addTile(tileSchema: TileSchema, groupingTileId: String, where: InsertionPosition = End): Result<Unit>
                fun removeTile(tileId: String, groupingTileId: String): Result<Unit>
                fun replaceTiles(tileSchemas: List<TileSchema>, groupingTileId: String): Result<Unit>
                fun wipeTiles(groupingTileId: String): Result<Unit>
                fun updateTile(tileId: String, updateData: Map<String, Any?>): Result<Unit>
                fun checkIfTileHasChildren(groupingTileId: String, childrenIds: List<String>): Boolean
                fun getTileChildrenCount(groupingTileId: String): Int?
            }
        """.trimIndent()
    ),
    MechanismTopic(
        title = "Named mechanisms, at a glance",
        description = "Each one exists to solve a specific problem — a named class/singleton you " +
            "can point at. Reached from a custom EventRunner/TileRenderer via get<T>()/" +
            "koinInject<T>() — see skill/mosaic-client/SKILL.md §8 for exactly how each one is " +
            "reached, and skill/mosaic/SKILL.md §4 for the full table with every entry.",
        code = """
            NetworkParametersHolder    Stages a dynamically-computed value (signed URL, token) for the NEXT network call.
            DataMailer                 Moves a value between two unrelated screens, no navigation arguments involved.
            CancellableEventsHolder    Makes a long-running chain (timers, polls) stoppable later, from elsewhere.
            ScreenExtrasHolder         Decouples "the nav graph loaded" from "this screen was actually opened."
            NavigatorsHolder           Lets any event, at any tree depth, trigger navigation without threading the backstack as a parameter.
            ScreenTilesBroadcastChannel / SystemBroadcastChannel   Pub/sub within one screen vs. app-wide.
            stackableOverlays          Bottom sheets/dialogs stacking, addressable by id, with a real exit animation.
            MosaicSerializer           Resolves polymorphic JSON to the right concrete type — built for you from your own Definitions, never hand-edited.
            DrawableResourcesHolder    The Image tile resolving a resource name to an app-bundled asset.
            CameraManager              One platform-agnostic takePicture(), bound per-platform, behind TakePicture.
            DataProcessor              Extension point for ProcessData(processWith = id) — Koin-multibound, resolved via getAll<DataProcessor>().
            MosaicHeadersPlugin        Stamps every outgoing request with 9 x-mosaic-* device/platform headers, no opt-out.
            MosaicColors               Runtime-swappable theme, driven by SetTheme/ResetTheme.
            TemplateProcessor          The <|path|> template engine behind TransformData/UpdateTiles.
            ThresholdReachedEffect     Infinite-scroll pagination's "don't re-fire until the list grows" guard.
        """.trimIndent(),
        codeIsKotlin = false,
    ),
    MechanismTopic(
        title = "Implicit patterns — conventions, not classes",
        description = "No dedicated class enforces any of these — a typo or a missed step fails " +
            "silently: it still compiles, still serializes, it just never fires. Worth knowing " +
            "them precisely for that reason.",
        code = """
            Event chaining is plain data class equality — events.filter { it.trigger == eventTrigger }, no listener registry.
            incomingData propagation — onTrigger(trigger, data) hands data to the matching child as ITS incomingData; no shared mutable context.
            field ?: incomingData fallback — several schemas accept a literal value OR fall back to incomingData when omitted (SendData.data, UploadFile.url via staging).
            Local TileEvent + remote EventTrigger, same tap — every stateful input tile (Checkbox, RadioButton, Tabs) fires both; dispatchEvent (local) always finishes first.
            TileGroupEvent — RadioButton's mutual exclusion: a plain groupId field, every matching tile in the tree decides for itself.
            Two-phase overlay dismissal — isDismissing flips first, the exit animation plays, only then the overlay leaves the tree.
            GetScreen -> ChangeScreenState — deliberately 2 separate events, fused by RefreshScreen and by every entry{}'s default initialEvents.
            StyleSchema application order — windowInsets -> margin -> size -> clip -> background -> click handling -> border -> padding, fixed, via Modifier.styledWith.
            @Triggers annotation — source-only documentation (SOURCE retention), never read at runtime.
        """.trimIndent(),
        codeIsKotlin = false,
    ),
)

private fun TileSchemaBuilderScope.MechanismSection(topic: MechanismTopic) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        },
        arrangement = arrangeVerticallySpacedBy(12)
    ) {
        SimpleText(
            text = topic.title,
            typography = typographyHeadlineSmall()
        )
        SimpleText(
            text = topic.description,
            typography = typographyBodyLarge(),
            color = color(themeColorOnSurfaceVariant())
        )
        if (topic.code != null) {
            CodeViewer(
                code = topic.code,
                language = if (topic.codeIsKotlin) CodeViewerTileSchema.Language.KOTLIN else CodeViewerTileSchema.Language.DEFAULT,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                }
            )
        }
    }
}

object MechanismsScreenBuilder : ScreenBuilder {

    override fun canBuild(screenId: String) = screenId == "mechanisms"

    override suspend fun RoutingCall.build() = Screen(id = "mechanisms") {
        Column(
            id = "mechanisms_screen_root",
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
            // About/Tiles/Events/Get started/Extend.
            Column(
                id = "mechanisms_hero",
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
                        background(color(themeColorPrimaryContainer()))
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
                        text = "Mechanisms",
                        typography = typographyDisplayMedium(),
                        color = color(themeColorInverseOnSurface())
                    )
                    SimpleText(
                        text = "Underneath the tiles and events DSL are the real receiver classes and " +
                            "runtime conventions that explain how data flows and how the tile tree gets " +
                            "manipulated — the same ones documented in skill/mosaic/SKILL.md.",
                        typography = typographyBodyLarge(),
                        color = color(themeColorInverseOnSurface())
                    )
                }
            }

            mechanismTopics.forEach { topic ->
                MechanismSection(topic)
            }
        }
    }
}
