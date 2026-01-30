package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.application.MosaicApplicationStateHolder
import dev.catbit.mosaic.client.domain.graph.GetInitialGraphUseCase
import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
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
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss.DismissBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display.DisplayBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss.DismissDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display.DisplayDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss.DismissNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display.DisplayNavigationDrawerEventDefinition
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
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data.CheckForReceivedDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data.GetDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data.ProcessDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data.RemoveDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data.SendDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data.UpdateDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event.TriggerEventEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission.RequestPermissionEventDefinition
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

class MosaicModules(
    tileDefinitions: List<TileDefinition<out TileSchema>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventSchema>> = emptyList()
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
        DisplayNavigationDrawerEventDefinition,
        CheckForReceivedDataEventDefinition,
        GetDataEventDefinition,
        ProcessDataEventDefinition,
        RemoveDataEventDefinition,
        SendDataEventDefinition,
        UpdateDataEventDefinition,
        TriggerEventEventDefinition,
        RequestPermissionEventDefinition
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
                    def.tileSchemaClass to def.tileSchemaClass.serializer()
                },
                eventSerializers = (baseEventsDefinitions + eventDefinitions).associate { def ->
                    def.eventSchemaClass to def.eventSchemaClass.serializer()
                }
            )
        }
    }

    private val renderingModule = module {
        single {
            TileRendererManager(
                tileRenderers = (baseTilesDefinitions + tileDefinitions).associate { definition ->
                    definition.tileSchemaClass to definition.tileRenderer
                }
            )
        }

        single {
            TileHolderBuilderManager(
                builders = (baseTilesDefinitions + tileDefinitions).associate { definition ->
                    definition.tileSchemaClass to definition.tileHolderBuilder
                }
            )
        }
    }

    private val eventModule = module {
        single {
            EventRunnerManager(
                eventRunners = (baseEventsDefinitions + eventDefinitions).associate { definition ->
                    definition.eventSchemaClass to definition.eventRunner
                }
            )
        }

        single {
            EventHolderBuilderManager(
                builders = (baseEventsDefinitions + eventDefinitions).associate { definition ->
                    definition.eventSchemaClass to definition.eventHolderBuilder
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