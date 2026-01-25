package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.application.MosaicApplicationStateHolder
import dev.catbit.mosaic.client.domain.GetInitialGraphUseCase
import dev.catbit.mosaic.client.domain.GetScreenUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunnerManager
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu.ToggleMenuEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate.NavigateEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up.NavigateUpEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.DismissBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.DisplayBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.DismissDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.DisplayDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column.ScrollTileColumnEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.send_network_request.SendNetworkRequestEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles.AddTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles.RemoveTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles.ReplaceTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles.UpdateTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles.WipeTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button.ButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row.RowTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field.TextFieldTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu.MenuTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text.TextTileDefinition
import dev.catbit.mosaic.core.data.event.EventModel
import dev.catbit.mosaic.core.data.tile.TileModel
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class MosaicModules(
    tileDefinitions: List<TileDefinition<out TileModel>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventModel>> = emptyList()
) {

    private val baseTilesDefinitions = listOf(
        ScreenTileDefinition,
        ColumnTileDefinition,
        RowTileDefinition,
        ButtonTileDefinition,
        TextFieldTileDefinition,
        TextTileDefinition,
        MenuTileDefinition
    )

    private val baseEventsDefinitions = listOf(
        SendNetworkRequestEventDefinition,
        NavigateEventDefinition,
        NavigateUpEventDefinition,
        ToggleMenuEventDefinition,
        ScrollTileColumnEventDefinition,
        AddTilesEventDefinition,
        RemoveTilesEventDefinition,
        ReplaceTilesEventDefinition,
        UpdateTilesEventDefinition,
        WipeTilesEventDefinition,
        DisplayBottomSheetEventDefinition,
        DismissBottomSheetEventDefinition,
        DismissDialogEventDefinition,
        DisplayDialogEventDefinition,
        DismissNavigationDrawerEventDefinition,
        DisplayNavigationDrawerEventDefinition
    )

    val modules by lazy {
        listOf(
            serializerModule,
            renderingModule,
            eventModule,
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
                    definition.tileModelClass to definition.tileRenderer
                }
            )
        }

        single {
            TileHolderBuilderManager(
                builders = (baseTilesDefinitions + tileDefinitions).associate { definition ->
                    definition.tileModelClass to definition.tileHolderBuilder
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

        single {
            EventHolderBuilderManager(
                builders = (baseEventsDefinitions + eventDefinitions).associate { definition ->
                    definition.eventModelClass to definition.eventHolderBuilder
                }
            )
        }
    }

    private val stateHolder = module {

        viewModel {
            MosaicApplicationStateHolder(
                getInitialGraphUseCase = get()
            )
        }

        viewModel { (screenId: String, navigationData: Map<String, Any>?) ->

            val tilesUIStateManager = TilesManager(
                tileHolderBuilderManager = get(),
                eventHolderBuilderManager = get(),
                serializer = get(),
                koinScope = this,
            )

            val eventManager = EventManager(
                eventRunnerManager = get(),
                koinScope = this
            )

            eventManager.apply {
                attachTilesEditor(tilesUIStateManager)
                attachTilesOverlaysEditor(tilesUIStateManager)
                attachTilesEventHolder(tilesUIStateManager)
                attachTilesEventDispatcher(tilesUIStateManager)
            }

            MosaicScreenStateHolder(
                screenId = screenId,
                navigationData = navigationData,
                getScreenUseCase = get(),
                tilesUIStateManager = tilesUIStateManager,
                eventManager = eventManager,
                tileRendererManager = get(),
            ).also {
                eventManager.apply {
                    attachDataHolder(it)
                    attachScreenBehaviors(it)
                }
            }
        }
    }

    private val useCaseModule = module {
        factory { GetScreenUseCase() }
        single { GetInitialGraphUseCase() }
    }
}