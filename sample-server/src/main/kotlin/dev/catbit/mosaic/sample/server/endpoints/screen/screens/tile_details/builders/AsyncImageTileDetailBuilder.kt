package dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.builders

import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomCode
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomDemoCard
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomHero
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParagraph
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParam
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomParamsTable
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomRelated
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomScaffold
import dev.catbit.mosaic.sample.server.dsl.tiles.showroom.ShowroomSectionTitle
import dev.catbit.mosaic.sample.server.endpoints.screen.screens.tile_details.TileDetailBuilder
import dev.catbit.mosaic.server.builder.tile.TileSchemaBuilderScope
import dev.catbit.mosaic.server.builder.tile.builders.image.AsyncImage
import dev.catbit.mosaic.server.builder.tile.builders.image.cropContentScale

object AsyncImageTileDetailBuilder : TileDetailBuilder {

    override fun canBuild(tileName: String) = tileName == "AsyncImage"

    override fun TileSchemaBuilderScope.buildDetail(tileName: String) {
        ShowroomScaffold {
            ShowroomHero(
                category = "Images and Icons",
                description = "Imagem carregada via Coil 3 a partir de uma URL, bytes brutos ou base64 — para avatares, fotos de produto, banners."
            )

            ShowroomSectionTitle("Visão geral")
            ShowroomParagraph(
                "model aceita 3 formas: Model.Url(url) pra imagens remotas, Model.ArrayOfBytes(bytes) " +
                    "e Model.Base64(string) pra imagens já disponíveis no servidor. Dispara " +
                    "OnAsyncImageLoadStart/Success/Failure — dá pra reagir com um Shimmer enquanto carrega."
            )

            ShowroomSectionTitle("Parâmetros")
            ShowroomParamsTable(
                listOf(
                    ShowroomParam("model", "Model", "Obrigatório. Url(url) / ArrayOfBytes(bytes) / Base64(string)."),
                    ShowroomParam("contentDescription", "String?", "Acessibilidade."),
                    ShowroomParam("contentScale", "ContentScale", "FIT (padrão), cropContentScale(), fillWidthContentScale(), etc."),
                    ShowroomParam("alpha", "Float", "Padrão 1.0."),
                    ShowroomParam("clipToBounds", "Boolean", "Padrão true."),
                )
            )

            ShowroomSectionTitle("Exemplo de código")
            ShowroomCode(
                """
                AsyncImage(
                    id = "avatar",
                    model = AsyncImageTileSchema.Model.Url(user.avatarUrl),
                    contentScale = cropContentScale(),
                    contentDescription = "${'$'}{user.name} avatar",
                    style = {
                        size(width = fixedHorizontally(48), height = fixedVertically(48))
                        clip(roundedCornerShape(all = 24))
                    }
                )
                """
            )

            ShowroomSectionTitle("Demo interativa")
            ShowroomDemoCard(title = "Imagem real carregada de uma URL pública") {
                AsyncImage(
                    id = "async_image_demo",
                    model = AsyncImageTileSchema.Model.Url(
                        "https://picsum.photos/seed/mosaic-showroom/480/270"
                    ),
                    contentDescription = "Imagem de demonstração do AsyncImage",
                    contentScale = cropContentScale(),
                    style = {
                        size(width = fillHorizontally(max = 480), height = fixedVertically(200))
                        clip(roundedCornerShape(all = 16))
                    }
                )
            }

            ShowroomSectionTitle("Placeholder com Shimmer durante o carregamento")
            ShowroomCode(
                """
                Box {
                    Shimmer { CircularProgressIndicator() } // ver evento OnAsyncImageLoadStart/Success
                    AsyncImage(
                        model = AsyncImageTileSchema.Model.Url(url),
                        events = {
                            UpdateTiles(
                                trigger = EventTriggers.onAsyncImageLoadSuccess(),
                                updates = { update("shimmer", inlineTileUpdateData("visibility" to "GONE")) }
                            )
                        }
                    )
                }
                """,
                id = "async_image_shimmer_snippet"
            )

            ShowroomRelated(
                names = listOf("Image", "Icon", "Shimmer"),
                destination = "tileDetails"
            )
        }
    }
}
