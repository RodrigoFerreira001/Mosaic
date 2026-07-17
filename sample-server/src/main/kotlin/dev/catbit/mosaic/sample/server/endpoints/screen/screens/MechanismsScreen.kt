package dev.catbit.mosaic.sample.server.endpoints.screen.screens

import dev.catbit.mosaic.sample.core.schemas.tiles.code.CodeViewerTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.code.CodeViewer
import dev.catbit.mosaic.server.builder.color.color
import dev.catbit.mosaic.server.builder.color.themeColorOnSurfaceVariant
import dev.catbit.mosaic.server.builder.color.themeColorSurfaceContainerLowest
import dev.catbit.mosaic.server.builder.placement.arrangeVerticallySpacedBy
import dev.catbit.mosaic.server.builder.screen.Screen
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Column
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import dev.catbit.mosaic.server.builder.typography.typographyBodyMedium
import dev.catbit.mosaic.server.builder.typography.typographyHeadlineSmall
import dev.catbit.mosaic.server.builder.typography.typographyTitleMedium
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingCall

private data class MechanismTopic(
    val title: String,
    val description: String,
    val code: String?
)

private val mechanismTopics = listOf(
    MechanismTopic(
        title = "DataSourceSchema — onde os dados vivem",
        description = "Cada leitura/escrita de dado no Mosaic aponta para uma dessas fontes. " +
            "Screen* são em memória e somem ao sair da tela; *DataBase são persistentes (SQLite); " +
            "Application* são em memória mas compartilhados por todo o app; Tile lê o valor exposto " +
            "por um tile específico (ex: o texto de um TextField) e é somente leitura.",
        code = """
            screenPlainData()                 // memória, escopo da tela, chave-valor plano
            screenSegmentedData("form")        // memória, escopo da tela, por segmento
            screenNavigationData()             // dados vindos do Navigate, somente leitura
            applicationPlainData()             // memória, escopo do app inteiro
            applicationSegmentedData("id")     // memória, escopo do app, por segmento
            plainDataBase()                    // SQLite persistente, chave-valor plano
            segmentedDataBase("auth")          // SQLite persistente, por segmento
            tile("tf_email", "text")           // valor exposto por um tile (somente leitura)
        """.trimIndent()
    ),
    MechanismTopic(
        title = "AccessModeSchema — quanto ler",
        description = "Controla quantos registros uma leitura (GetData) ou escrita (UpdateData) " +
            "afeta dentro da fonte escolhida.",
        code = """
            fullAccessMode()                          // todos os registros da fonte
            singleAccessMode("email")                 // um registro específico
            batchAccessMode(listOf("email", "name"))  // vários registros de uma vez
        """.trimIndent()
    ),
    MechanismTopic(
        title = "DataHolder — memória da tela",
        description = "É onde screenPlainData()/screenSegmentedData() de fato residem em " +
            "runtime, dentro do EventRunningScope de cada evento em execução. Três namespaces: " +
            "plainData, segmentedData (por segmento) e navigationData (somente leitura, vinda do " +
            "Navigate que abriu a tela).",
        code = null
    ),
    MechanismTopic(
        title = "EventRunningScope — contexto de execução de um Event",
        description = "Todo EventRunner roda dentro desse escopo no cliente. incomingData é o " +
            "dado recebido do evento pai; onTrigger dispara os eventos filhos que casam com o " +
            "trigger informado, passando data como o novo incomingData deles.",
        code = """
            incomingData: Any?
            suspend fun onTrigger(trigger: EventTrigger, data: Any? = null)
            suspend fun runEventInline(eventSchema: EventSchema, data: Any? = null)
            val dataHolder: DataHolder
            val tilesEditor: TilesEditor
            fun broadcastData(data: ScreenTilesBroadcastData)
        """.trimIndent()
    ),
    MechanismTopic(
        title = "TileRenderingScope — contexto de renderização de um Tile",
        description = "Todo TileRenderer roda dentro desse escopo no cliente. triggerEvent " +
            "dispara os events registrados no próprio tile (ex: onClick de um Button) em resposta " +
            "a uma interação do usuário.",
        code = """
            fun triggerEvent(trigger: EventTrigger, data: Any? = null)

            @Composable fun RenderChild(tileSchema: TileSchema)
            @Composable fun RenderChildren(tileSchemas: ImmutableList<TileSchema>)
        """.trimIndent()
    ),
    MechanismTopic(
        title = "TilesEditor — manipulando a árvore de tiles",
        description = "Usado internamente pelos events de Tile Management (AddTiles, " +
            "RemoveTiles, UpdateTiles, etc.) para alterar a árvore de tiles da tela em runtime. " +
            "Toda mutação dispara recomposição no cliente.",
        code = """
            fun addTile(tileSchema: TileSchema, groupingTileId: String, where: InsertionPosition)
            fun removeTile(tileId: String, groupingTileId: String? = null)
            fun updateTile(tileId: String, updateData: Map<String, Any?>)
            fun replaceTiles(tileSchemas: List<TileSchema>, groupingTileId: String? = null)
        """.trimIndent()
    ),
)

private fun TileSchemaBuilderScope.MechanismSection(topic: MechanismTopic) {
    Column(
        style = {
            size(width = fillHorizontally(), height = wrapVertically())
        },
        arrangement = arrangeVerticallySpacedBy(8)
    ) {
        SimpleText(
            text = topic.title,
            typography = typographyTitleMedium()
        )
        SimpleText(
            text = topic.description,
            typography = typographyBodyMedium(),
            color = color(themeColorOnSurfaceVariant())
        )
        if (topic.code != null) {
            CodeViewer(
                code = topic.code,
                language = CodeViewerTileSchema.Language.KOTLIN,
                theme = CodeViewerTileSchema.Theme.ATOM_ONE,
                style = {
                    size(width = fillHorizontally(), height = wrapVertically())
                }
            )
        }
    }
}

suspend fun RoutingCall.respondMechanisms() = respond(
    Screen(id = "mechanisms") {
        Column(
            id = "mechanisms_screen_root",
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
                text = "Mecanismos do Mosaic",
                typography = typographyHeadlineSmall()
            )
            SimpleText(
                text = "Por baixo da DSL de tiles e events existem alguns conceitos runtime que " +
                    "explicam como dados fluem e como a árvore de tiles é manipulada.",
                typography = typographyBodyMedium(),
                color = color(themeColorOnSurfaceVariant())
            )
            mechanismTopics.forEach { topic ->
                MechanismSection(topic)
            }
        }
    }
)
