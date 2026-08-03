package dev.catbit.mosaic.sample.server.endpoints.screen.screens.extend

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.sample.server.endpoints.screen.ScreenBuilder
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorErrorContainer
import dev.catbit.mosaic.server.builder.color.themeColorInverseOnSurface
import dev.catbit.mosaic.server.builder.color.themeColorInverseSurface
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSecondaryContainer
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.color.themeColorTertiaryContainer
import dev.catbit.mosaic.server.builder.placement.alignToBottomEnd
import dev.catbit.mosaic.server.builder.placement.alignToTopStart
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
 * Visual identity ported from m3.material.io: dark blob hero (mirrors About/Tiles/Events/Get
 * started/Mechanisms), then each step as a big headline + prose paragraph + code sample directly
 * on the page background — not boxed into small uniform cards.
 */
private const val TILE_STRUCTURE_CODE = """
mosaic-core/.../schemas/tile/tiles/[package]/[Name]TileSchema.kt
mosaic-server/.../builder/tile/builders/[package]/[Name]TileSchemaBuilder.kt
mosaic-client/.../implementations/tile/tiles/[package]/[name]/
    [Name]TileDefinition.kt
    [Name]TileHolder.kt
    [Name]TileHolderBuilder.kt
    [Name]TileRenderer.kt
"""

private const val TILE_BUILDER_CODE = """
internal class [Name]TileSchemaBuilder(
    private val id: String,
    private val events: EventSchemaBuilderScope.() -> Unit,
    private val style: StyleSchemaBuilderScope.() -> Unit,
    private val searchableTerms: List<String>?,
    private val visibility: TileSchema.Visibility,
    // campos específicos do schema
) : TileSchemaBuilder<[Name]TileSchema>() {

    override fun build() = [Name]TileSchema(
        id = id,
        events = EventSchemaBuilderScope().apply(events).build(),
        style = StyleSchemaBuilderScope().apply(style).buildStyle(),
        searchableTerms = searchableTerms?.toImmutableList(),
        visibility = visibility,
        // mapeie os campos
    )
}

fun TileSchemaBuilderScope.[Name](
    id: String = randomId(),
    events: EventSchemaBuilderScope.() -> Unit = {},
    style: StyleSchemaBuilderScope.() -> Unit = {},
    searchableTerms: List<String>? = null,
    visibility: TileSchema.Visibility = TileSchema.Visibility.VISIBLE,
    // parâmetros específicos
) {
    addBuilder([Name]TileSchemaBuilder(id, events, style, searchableTerms, visibility))
}
"""

private const val EVENT_STRUCTURE_CODE = """
mosaic-core/.../schemas/event/events/[package]/[Name]EventSchema.kt
mosaic-server/.../builder/event/builders/[package]/[Name]EventBuilder.kt
mosaic-client/.../implementations/event/events/[package]/[name]/
    [Name]EventDefinition.kt
    [Name]EventHolder.kt
    [Name]EventHolderBuilder.kt
    [Name]EventRunner.kt
"""

private const val EVENT_RUNNER_CODE = """
object [Name]EventRunner : EventRunner<[Name]EventSchema> {
    override suspend fun EventRunningScope.runEvent(event: [Name]EventSchema) {
        runSafely(
            onError = {
                onTrigger(EventTriggers.onFailure(), data = it)
            }
        ) {
            /* lógica do evento */
            onTrigger(EventTriggers.onSuccess())
        }
    }
}
"""

private const val REGISTRATION_CODE = """
// mosaic-core: MosaicSerializer.kt
[Name]TileSchema::class to [Name]TileSchema.serializer()

// mosaic-client: MosaicModules.kt
private val baseTilesDefinitions = listOf(
    // ...
    [Name]TileDefinition,
)
"""

private data class ExtendStep(
    val title: String,
    val intro: String,
    val code: String,
    val codeIsKotlin: Boolean = true,
)

private val extendSteps = listOf(
    ExtendStep(
        title = "Criar um novo Tile — estrutura de pacotes",
        intro = "Tiles e events novos seguem um padrão fixo: um schema em mosaic-core, um builder " +
            "DSL em mosaic-server, e a implementação client-side (definition, holder, holder " +
            "builder, renderer/runner) em mosaic-client.",
        code = TILE_STRUCTURE_CODE.trim(),
        codeIsKotlin = false,
    ),
    ExtendStep(
        title = "O builder do lado do servidor",
        intro = "Expõe uma função de extensão de TileSchemaBuilderScope — é essa função que você " +
            "usa na DSL.",
        code = TILE_BUILDER_CODE.trim(),
    ),
    ExtendStep(
        title = "Criar um novo Event — estrutura de pacotes",
        intro = "Mesmo padrão dos tiles, trocando \"tile\" por \"event\" nos três módulos.",
        code = EVENT_STRUCTURE_CODE.trim(),
        codeIsKotlin = false,
    ),
    ExtendStep(
        title = "O EventRunner",
        intro = "É onde a lógica realmente roda no cliente, disparando triggers filhos com " +
            "onTrigger(...).",
        code = EVENT_RUNNER_CODE.trim(),
    ),
    ExtendStep(
        title = "Registrando no framework",
        intro = "Todo tile/event novo precisa ser registrado em dois lugares: o serializer no " +
            "mosaic-core (para JSON funcionar nos dois lados) e a definition na injeção de " +
            "dependências do mosaic-client (para o renderer/runner ser encontrado em tempo de " +
            "execução).",
        code = REGISTRATION_CODE.trim(),
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
                    style = {
                        size(width = fillHorizontally(), height = fixedVertically(140))
                        background(color(themeColorErrorContainer()))
                    }
                ) {
                    Box(
                        alignment = alignToTopStart(),
                        style = {
                            size(width = fixedHorizontally(90), height = fixedVertically(90))
                            clip(circleShape())
                            background(color(themeColorSecondaryContainer()))
                            margin(top = 8, start = 8)
                        }
                    ) {}
                    Box(
                        alignment = alignToBottomEnd(),
                        style = {
                            size(width = fixedHorizontally(120), height = fixedVertically(120))
                            clip(circleShape())
                            background(color(themeColorTertiaryContainer()))
                            margin(bottom = 8, end = 8)
                        }
                    ) {}
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
                        text = "Tiles e events novos seguem um padrão fixo em três módulos — schema, " +
                            "builder DSL e implementação client-side — mais dois registros para ligar " +
                            "tudo ao framework.",
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
