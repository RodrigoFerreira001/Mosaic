package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.domain.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunnerManager
import dev.catbit.mosaic.client.ui.sdui.foundation.mappings.BaseUIMappings
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.manager.TilesUIStateManager
import dev.catbit.mosaic.client.ui.sdui.foundation.state.producer.builder.UIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.foundation.state.tile.TileUIState
import dev.catbit.mosaic.client.ui.sdui.foundation.tile_renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigate.NavigateEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column.ScrollTileColumnEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request.SendNetworkRequestEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.style.StyleUIStateProducerBuilder
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button.ButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field.TextFieldTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text.TextTileDefinition
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.data.tile.style.StyleModel
import dev.catbit.mosaic.core.mapping.Mapper
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import kotlin.reflect.KClass

class MosaicModules(
    tileDefinitions: List<TileDefinition<out TileModel, out TileUIState>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventModel>> = emptyList()
) {

    private val baseTilesDefinitions = listOf(
        ColumnTileDefinition,
        ButtonTileDefinition,
        TextFieldTileDefinition,
        TextTileDefinition
    )

    private val baseEventsDefinitions = listOf(
        SendNetworkRequestEventDefinition,
        NavigateEventDefinition,
        ScrollTileColumnEventDefinition
    )

    val modules by lazy {
        listOf(
            serializerModule,
            renderingModule,
            eventModule,
            mappingModule,
            stateModule,
            stateHolder,
            useCaseModule
        )
    }

    @OptIn(InternalSerializationApi::class)
    private val serializerModule = module {

        single {
            MosaicSerializer(
                tileSerializers = (baseTilesDefinitions + tileDefinitions).associate { def ->
                    def.tileModelClass to def.tileModelClass.serializer()
                },
                eventSerializers = (baseEventsDefinitions + eventDefinitions).associate { def ->
                    def.eventModelClass to def.eventModelClass.serializer()
                }
            )
        }
    }

    private val renderingModule = module {
        single {
            TileRendererManager(
                tileRenderers = (baseTilesDefinitions + tileDefinitions).associate { definition ->
                    definition.tileUIStateClass to definition.tileRenderer
                }
            )
        }
    }

    private val eventModule = module {
        single {
            EventRunnerManager(
                eventRunners = (baseEventsDefinitions + eventDefinitions).associate { definition ->
                    definition.eventModelClass to definition.eventRunner
                }
            )
        }
    }

    private val mappingModule = module {
        single { Mapper(BaseUIMappings.mappings) }
    }

    private val stateModule = module {
        single<Map<KClass<*>, UIStateProducerBuilder<*, *>>> {
            mutableMapOf<KClass<*>, UIStateProducerBuilder<*, *>>().apply {
                putAll(
                    (baseTilesDefinitions + tileDefinitions).associate { definition ->
                        definition.tileModelClass to definition.tileUIStateProducerBuilder
                    }
                )
                put(StyleModel::class, StyleUIStateProducerBuilder)
            }
        }
    }

    private val stateHolder = module {
        viewModel { (screenId: String, navigationData: Map<String, Any>?) ->

            val tilesUIStateManager = TilesUIStateManager(
                uiStateProducerBuilders = get(),
                mapper = get(),
                serializer = get(),
                koinScope = this,
            )

            val eventManager = EventManager(
                eventRunnerManager = get(),
                koinScope = this
            )

            tilesUIStateManager.attachEventRegister(eventManager)
            eventManager.attachTilesEditor(tilesUIStateManager)

            MosaicScreenStateHolder(
                screenId = screenId,
                navigationData = navigationData,
                getScreenUseCase = get(),
                tilesUIStateManager = tilesUIStateManager,
                eventManager = eventManager,
                tileRendererManager = get(),
            ).also {
                eventManager.attachScreenBehaviors(it)
            }
        }
    }

    private val useCaseModule = module {
        factory { GetScreenUseCase() }
    }
}