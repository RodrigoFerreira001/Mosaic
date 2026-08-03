package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomNote
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope

object ImageTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "Image"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Images and Icons",
                description = "Imagem estática a partir de um drawable já empacotado no app, resolvido por resourceName via DrawableResourcesHolder."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "Use pra imagens que já vêm dentro do app (splash, logo, ilustrações) — diferente de " +
                    "AsyncImage, não faz nenhuma requisição de rede. resourceName é só uma chave: quem " +
                    "mapeia essa chave pro recurso de verdade é o client, registrando um " +
                    "DrawableResourcesHolder(mapOf(\"app_logo\" to Res.drawable.app_logo, ...))."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("resourceName", "String", "Obrigatório. Chave registrada no DrawableResourcesHolder do client."),
                    ShowroomParam("contentDescription", "String?", "Acessibilidade."),
                    ShowroomParam("contentScale", "ContentScale", "FIT (padrão), CROP, FILL_WIDTH, etc."),
                    ShowroomParam("alpha", "Float", "Padrão 1.0."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                Image(
                    id = "logo",
                    resourceName = "app_logo",
                    contentScale = ImageTileSchema.ContentScale.FIT,
                    contentDescription = "App logo"
                )
                """
            )

            ShowroomNote(
                "Este sample-server não registra nenhum resourceName real no DrawableResourcesHolder " +
                    "do client — pedir um nome desconhecido só faz o Image renderizar vazio, silenciosamente " +
                    "(comportamento documentado, não é bug). Por isso não há uma demo ao vivo aqui: o " +
                    "código acima já é exatamente como se usa em produção, uma vez que o client registre " +
                    "os drawables. Pra ver uma imagem de verdade renderizando, veja AsyncImage."
            )

            ShowroomRelated(
                names = listOf("AsyncImage", "Icon"),
                destination = "tileDetails"
            )
        }
    }
}
