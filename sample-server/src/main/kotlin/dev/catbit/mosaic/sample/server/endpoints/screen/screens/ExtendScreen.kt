package dev.catbit.mosaic.sample.server.endpoints.screen.screens

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

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

suspend fun RoutingCall.respondExtend() = respond(
    Screen(id = "extend") {
        Column(
            id = "extend_screen_root",
            style = {
                size(width = fillHorizontally(), height = fillVertically())
                windowInsets(windowInsetsSystemBars())
                background(color(themeColorSurfaceContainerLowest()))
                padding(horizontal = 24, top = 24, bottom = 24)
            },
            arrangement = arrangeVerticallySpacedBy(24),
            scrollable = true
        ) {
            SimpleText(
                text = "Estendendo o Mosaic",
                typography = typographyHeadlineSmall()
            )
            SimpleText(
                text = "Tiles e events novos seguem um padrão fixo: um schema em mosaic-core, um " +
                    "builder DSL em mosaic-server, e a implementação client-side (definition, holder, " +
                    "holder builder, renderer/runner) em mosaic-client — mais dois registros para " +
                    "ligar tudo ao framework.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            SimpleText(
                text = "Criar um novo Tile",
                typography = typographyTitleMedium()
            )
            SimpleText(
                text = "Estrutura de pacotes:",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            CodeViewer(
                code = TILE_STRUCTURE_CODE.trim(),
                language = CodeViewerTileSchema.Language.DEFAULT,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = { size(width = fillHorizontally(), height = wrapVertically()) }
            )
            SimpleText(
                text = "O builder do lado do servidor expõe uma função de extensão de " +
                    "TileSchemaBuilderScope — é essa função que você usa na DSL:",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            CodeViewer(
                code = TILE_BUILDER_CODE.trim(),
                language = CodeViewerTileSchema.Language.KOTLIN,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = { size(width = fillHorizontally(), height = wrapVertically()) }
            )
            SimpleText(
                text = "Criar um novo Event",
                typography = typographyTitleMedium()
            )
            SimpleText(
                text = "Estrutura de pacotes:",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            CodeViewer(
                code = EVENT_STRUCTURE_CODE.trim(),
                language = CodeViewerTileSchema.Language.DEFAULT,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = { size(width = fillHorizontally(), height = wrapVertically()) }
            )
            SimpleText(
                text = "O EventRunner é onde a lógica realmente roda no cliente, disparando " +
                    "triggers filhos com onTrigger(...):",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            CodeViewer(
                code = EVENT_RUNNER_CODE.trim(),
                language = CodeViewerTileSchema.Language.KOTLIN,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = { size(width = fillHorizontally(), height = wrapVertically()) }
            )
            SimpleText(
                text = "Registrando no framework",
                typography = typographyTitleMedium()
            )
            SimpleText(
                text = "Todo tile/event novo precisa ser registrado em dois lugares: o " +
                    "serializer no mosaic-core (para JSON funcionar nos dois lados) e a definition " +
                    "na injeção de dependências do mosaic-client (para o renderer/runner ser " +
                    "encontrado em tempo de execução).",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            CodeViewer(
                code = REGISTRATION_CODE.trim(),
                language = CodeViewerTileSchema.Language.KOTLIN,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = { size(width = fillHorizontally(), height = wrapVertically()) }
            )
        }
    }
)
