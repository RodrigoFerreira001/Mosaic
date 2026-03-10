package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.application.MosaicApplicationStateHolder
import dev.catbit.mosaic.client.data.data_sources.database.MosaicDatabase
import dev.catbit.mosaic.client.data.data_sources.database.MosaicDatabaseImpl
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystemImpl
import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetwork
import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetworkImpl
import dev.catbit.mosaic.client.data.data_sources.object_storage.MosaicObjectStorage
import dev.catbit.mosaic.client.data.data_sources.object_storage.MosaicObjectStorageImpl
import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.client.data.repository.MosaicRepositoryImpl
import dev.catbit.mosaic.client.domain.download.DownloadFileUseCase
import dev.catbit.mosaic.client.domain.graph.GetInitialGraphUseCase
import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.domain.send_request.SendNetworkRequestUseCase
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunnerManager
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenExtrasHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data.CheckForReceivedDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data.GetDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data.EvaluateDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data.ProcessDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data.RemoveDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data.SendDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data.UpdateDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event.TriggerEventEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu.ToggleMenuEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate.NavigateEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up.NavigateUpEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file.DownloadFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_network_request.SendNetworkRequestEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss.DismissBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display.DisplayBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss.DismissDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display.DisplayDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss.DismissNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display.DisplayNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state.ChangeScreenStateEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen.GetScreenEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen.RefreshScreenEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column.ScrollTileColumnEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row.ScrollRowTileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission.RequestPermissionEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles.AddTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles.RemoveTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles.ReplaceTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles.UpdateTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles.WipeTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection.CheckIfHasInternetConnectionEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_file.DeleteFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file.GetFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file.SaveFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer.StartCountdownTimerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events.UpdateEventsEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button.ButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.box.BoxTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.card.CardTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.carousel.CarouselTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.grid.GridTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pager.PagerTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.pull_to_refresh.PullToRefreshTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.containers.shimmer.ShimmerTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row.RowTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field.TextFieldTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu.MenuTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.text.TextTileDefinition
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import dev.catbit.mosaic.client.MosaicDatabase as MosaicSQDelightDatabase

internal class MosaicModules(
    applicationId: String,
    baseUrl: String,
    additionalModule: Module = module { },
    tileDefinitions: List<TileDefinition<out TileSchema>> = emptyList(),
    eventDefinitions: List<EventDefinition<out EventSchema>> = emptyList()
) {

    val modules by lazy {
        listOf(
            additionalModule,
            applicationModule,
            dataModule,
            serializerModule,
            renderingModule,
            eventModule,
            stateHolder,
            useCaseModule,
            platformModule
        )
    }

    private val applicationModule = module {
        single(named("APPLICATION_ID")) { applicationId }
        single { ScreenExtrasHolder() }
    }

    private val dataModule = module {

        single<MosaicObjectStorage> {
            MosaicObjectStorageImpl(
                dataChest = get(),
                serializer = get()
            )
        }

        single<MosaicNetwork> {
            MosaicNetworkImpl(
                baseUrl = baseUrl,
                httpClient = get<HttpClient>().config {
                    install(HttpTimeout) {
                        requestTimeoutMillis = 20.seconds.inWholeMilliseconds
                    }
                    install(ContentNegotiation) {
                        json(get<MosaicSerializer>().json)
                    }
                }
            )
        }

        single<MosaicDatabase> {
            MosaicDatabaseImpl(
                MosaicSQDelightDatabase(
                    driver = get()
                ),
                serializer = get()
            )
        }

        single<MosaicFileSystem> {
            MosaicFileSystemImpl()
        }

        single<MosaicRepository> {
            MosaicRepositoryImpl(
                network = get(),
                database = get(),
                objectStorage = get(),
                fileSystem = get()
            )
        }
    }

    @OptIn(InternalSerializationApi::class)
    private val serializerModule = module {

        single {
            MosaicSerializer(
                tileSerializers = tileDefinitions.associate { def ->
                    def.tileSchemaClass to def.tileSchemaClass.serializer()
                },
                eventSerializers = eventDefinitions.associate { def ->
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
                getInitialGraphUseCase = get(),
                screenExtrasHolder = get()
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
                screenId = screenId,
                eventRunnerManager = get(),
                koinScope = this
            )

            eventManager.apply {
                attachTilesEditor(tilesUIStateManager)
                attachTilesOverlaysEditor(tilesUIStateManager)
                attachTilesEventHolder(tilesUIStateManager)
                attachTilesEventDispatcher(tilesUIStateManager)
            }

            val screenExtras = get<ScreenExtrasHolder>().getExtra(screenId)

            MosaicScreenStateHolder(
                initialTiles = screenExtras.initialTiles,
                initialEvents = screenExtras.initialEvents,
                failureTiles = screenExtras.failureTiles,
                failureEvents = screenExtras.failureEvents,
                navigationData = navigationData,
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
        single { GetInitialGraphUseCase(get()) }
        factory { GetScreenUseCase(get()) }
        factory { DownloadFileUseCase(get()) }
        factory { SendNetworkRequestUseCase(get()) }
    }

    private val baseTilesDefinitions = listOf(
        ScreenTileDefinition,
        ColumnTileDefinition,
        RowTileDefinition,
        ButtonTileDefinition,
        BoxTileDefinition,
        CardTileDefinition,
        CarouselTileDefinition,
        GridTileDefinition,
        PagerTileDefinition,
        PullToRefreshTileDefinition,
        ShimmerTileDefinition,
        TextFieldTileDefinition,
        TextTileDefinition,
        MenuTileDefinition
    )

    private val baseEventsDefinitions = listOf(
        DownloadFileEventDefinition,
        SendNetworkRequestEventDefinition,
        NavigateEventDefinition,
        NavigateUpEventDefinition,
        ToggleMenuEventDefinition,
        ScrollTileColumnEventDefinition,
        ScrollRowTileEventDefinition,
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
        EvaluateDataEventDefinition,
        RemoveDataEventDefinition,
        SendDataEventDefinition,
        UpdateDataEventDefinition,
        TriggerEventEventDefinition,
        RequestPermissionEventDefinition,
        ChangeScreenStateEventDefinition,
        GetScreenEventDefinition,
        RefreshScreenEventDefinition,
        CheckIfHasInternetConnectionEventDefinition,
        DeleteFileEventDefinition,
        GetFileEventDefinition,
        SaveFileEventDefinition,
        StartCountdownTimerEventDefinition,
        UpdateEventsEventDefinition
    )
}

internal expect val platformModule: Module
