package dev.catbit.mosaic.client.di

import dev.catbit.mosaic.client.application.MosaicApplicationStateHolder
import dev.catbit.mosaic.client.data.data_sources.database.MosaicDatabase
import dev.catbit.mosaic.client.data.data_sources.database.MosaicDatabaseImpl
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystem
import dev.catbit.mosaic.client.data.data_sources.file_system.MosaicFileSystemImpl
import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetwork
import dev.catbit.mosaic.client.data.data_sources.network.MosaicNetworkImpl
import dev.catbit.mosaic.client.data.data_sources.network.NetworkParametersHolder
import dev.catbit.mosaic.client.data.data_sources.network.plugins.MosaicHeadersPlugin
import dev.catbit.mosaic.client.data.data_sources.object_storage.MosaicObjectStorage
import dev.catbit.mosaic.client.data.data_sources.object_storage.MosaicObjectStorageImpl
import dev.catbit.mosaic.client.data.repository.MosaicRepository
import dev.catbit.mosaic.client.data.repository.MosaicRepositoryImpl
import dev.catbit.mosaic.client.domain.data.plain.GetAllPlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.GetPlainDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.plain.GetPlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.plain.RemovePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.UpdatePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.plain.WipePlainDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetAllSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetSegmentedDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.segmented.GetSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataByIdsUseCase
import dev.catbit.mosaic.client.domain.data.segmented.RemoveSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.UpdateSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.data.segmented.WipeSegmentedDataUseCase
import dev.catbit.mosaic.client.domain.download.DownloadFileUseCase
import dev.catbit.mosaic.client.domain.graph.GetInitialGraphUseCase
import dev.catbit.mosaic.client.domain.screen.GetScreenUseCase
import dev.catbit.mosaic.client.domain.send_request.SendNetworkRequestUseCase
import dev.catbit.mosaic.client.domain.upload.UploadFileUseCase
import dev.catbit.mosaic.client.logger.MosaicLogger
import dev.catbit.mosaic.client.ui.sdui.foundation.data_mailer.DataMailer
import dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.DataProcessor
import dev.catbit.mosaic.client.ui.sdui.foundation.data_processor.processors.EventRunnerDataProcessor
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.EventTriggerDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.definitions.TileDefinition
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventManager
import dev.catbit.mosaic.client.ui.sdui.foundation.events.EventRunnerManager
import dev.catbit.mosaic.client.ui.sdui.foundation.graph.ScreenNavKey
import dev.catbit.mosaic.client.ui.sdui.foundation.navigation.NavigatorsHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.resources.DrawableResourcesHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.MosaicScreenStateHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.screen.ScreenExtrasHolder
import dev.catbit.mosaic.client.ui.sdui.foundation.system_broadcast.SystemBroadcastChannel
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.event.EventHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.holder.tile.TileHolderBuilderManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.manager.TilesManager
import dev.catbit.mosaic.client.ui.sdui.foundation.tiles.renderer.TileRendererManager
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.check_for_received_data.CheckForReceivedDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.evaluate_data.EvaluateDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.get_data.GetDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.process_data.ProcessDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.remove_data.RemoveDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.send_data.SendDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.transform_data.TransformDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.data.update_data.UpdateDataEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.run_events.RunEventsEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.trigger_event.TriggerEventEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.event.update_events.UpdateEventsEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.delete_file.DeleteFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.get_file.GetFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.file.save_file.SaveFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.menu.menu.ToggleMenuEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate.NavigateEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.navigation.navigate_up.NavigateUpEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.download_file.DownloadFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_file.SendFileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.send_network_request.SendNetworkRequestEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_body.SetIncomingDataToNetworkParamsHolderBodyEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_headers.SetIncomingDataToNetworkParamsHolderHeadersEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.networking.set_incoming_data_to_network_params_holder_url.SetIncomingDataToNetworkParamsHolderUrlEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.dismiss.DismissBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.bottom_sheet.display.DisplayBottomSheetEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.dismiss.DismissDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.dialog.display.DisplayDialogEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.dismiss.DismissNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.navigation_drawer.display.DisplayNavigationDrawerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.dismiss.DismissSnackbarEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.overlays.snackbar.display.DisplaySnackbarEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.pull_to_refresh.StopRefreshingEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.change_screen_state.ChangeScreenStateEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.get_screen.GetScreenEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.screen.refresh_screen.RefreshScreenEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.column.ScrollTileColumnEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.pager.ScrollTilePagerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.scroll.row.ScrollRowTileEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.security.request_permission.RequestPermissionEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.broadcast_to_system.BroadcastToSystemEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.system.check_if_has_internet_connection.CheckIfHasInternetConnectionEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.add_tiles.AddTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.check_if_tile_contains_children.CheckIfTileContainsChildrenEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.get_tile_children_count.GetTileChildrenCountEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.reload_lazy_tiles.ReloadLazyTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.remove_tiles.RemoveTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.replace_tiles.ReplaceTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.update_tiles.UpdateTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.tiles.wipe_tiles.WipeTilesEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.event.events.time.start_countdown_timer.StartCountdownTimerEventDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.bottom_app_bar.BottomAppBarTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.app_bars.top_app_bar.TopAppBarTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.badges.badge.BadgeTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.button.ButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.fab.FloatingActionButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.buttons.icon_button.IconButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.assist_chip.AssistChipTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.filter_chip.FilterChipTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.input_chip.InputChipTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.chips.suggestion_chip.SuggestionChipTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.adaptive_visibility.AdaptiveVisibilityTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.box.BoxTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.card.CardTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.carousel.CarouselTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.column.ColumnTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.grid.GridTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_column.LazyColumnTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_row.LazyRowTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.lazy_tiles.LazyTilesTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pager.PagerTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.pull_to_refresh.PullToRefreshTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.row.RowTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.grouping.shimmer.ShimmerTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.async_image.AsyncImageTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.icon.IconTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.image.image.ImageTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.checkbox.CheckboxTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.radio_button.RadioButtonTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.switch.SwitchTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.inputs.text_field.TextFieldTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.internal.screen.ScreenTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.menu.MenuTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_bar.NavigationBarTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.navigation_rail.NavigationRailTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.nested_navigation_graph.NestedNavigationGraphTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.navigation.tabs.TabsTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.circular_progress.CircularProgressIndicatorTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.progress.linear_progress.LinearProgressIndicatorTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.search.search_bar.SearchBarTileDefinition
import dev.catbit.mosaic.client.ui.sdui.implementations.tile.tiles.text.simple_text.SimpleTextTileDefinition
import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.serialization.MosaicSerializer
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlin.time.Duration.Companion.seconds
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import org.jetbrains.compose.resources.DrawableResource
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

internal class MosaicModules(
    applicationId: String,
    baseUrl: String,
    additionalModule: Module,
    logger: MosaicLogger,
    tileDefinitions: List<TileDefinition<out TileSchema>>,
    eventDefinitions: List<EventDefinition<out EventSchema>>,
    eventTriggerDefinitions: List<EventTriggerDefinition<out EventTrigger>>,
    drawableResources: Map<String, DrawableResource>,
    additionalSerializersModule: SerializersModule,
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
            dataProcessorsModule,
            platformModule
        )
    }

    private val applicationModule = module {
        single(named("APPLICATION_ID")) { applicationId }
        single { ScreenExtrasHolder() }
        single { NavigatorsHolder() }
        single { DataMailer() }
        single<MosaicLogger> { logger }
        single { DrawableResourcesHolder(drawableResources) }
        single { SystemBroadcastChannel() }
    }

    private val dataModule = module {

        single<MosaicObjectStorage> {
            MosaicObjectStorageImpl(
                dataChest = get(),
                serializer = get()
            )
        }

        single { NetworkParametersHolder() }

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
                    install(MosaicHeadersPlugin)
                },
                networkParametersHolder = get()
            )
        }

        single<MosaicDatabase> {
            MosaicDatabaseImpl(
                db = get(),
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
                },
                eventTriggerSerializers = eventTriggerDefinitions.associate { def ->
                    def.eventTriggerClass to def.eventTriggerClass.serializer()
                },
                additionalSerializersModule = additionalSerializersModule
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

        viewModel { (screenId: String, navigationData: ScreenNavKey.NavigationData, parent: TilesManager?) ->

            val tilesUIStateManager = TilesManager(
                tileHolderBuilderManager = get(),
                eventHolderBuilderManager = get(),
                serializer = get(),
                koinScope = this,
                parent = parent
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
                attachTilesValueProducer(tilesUIStateManager)
            }

            val screenExtras = get<ScreenExtrasHolder>().getExtra(screenId)

            MosaicScreenStateHolder(
                initialTiles = screenExtras.initialTiles,
                initialEvents = screenExtras.initialEvents,
                failureTiles = screenExtras.failureTiles,
                failureEvents = screenExtras.failureEvents,
                navigationData = navigationData,
                tilesManager = tilesUIStateManager,
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
        factory { UploadFileUseCase(get()) }
        factory { GetAllPlainDataUseCase(get()) }
        factory { GetPlainDataUseCase(get()) }
        factory { GetPlainDataByIdsUseCase(get()) }
        factory { UpdatePlainDataUseCase(get()) }
        factory { RemovePlainDataUseCase(get()) }
        factory { RemovePlainDataByIdsUseCase(get()) }
        factory { WipePlainDataUseCase(get()) }
        factory { GetAllSegmentedDataUseCase(get()) }
        factory { GetSegmentedDataUseCase(get()) }
        factory { GetSegmentedDataByIdsUseCase(get()) }
        factory { UpdateSegmentedDataUseCase(get()) }
        factory { RemoveSegmentedDataUseCase(get()) }
        factory { RemoveSegmentedDataByIdsUseCase(get()) }
        factory { WipeSegmentedDataUseCase(get()) }
    }

    private val baseTilesDefinitions = listOf(
        ScreenTileDefinition,
        AdaptiveVisibilityTileDefinition,
        ColumnTileDefinition,
        LazyColumnTileDefinition,
        RowTileDefinition,
        LazyRowTileDefinition,
        ButtonTileDefinition,
        FloatingActionButtonTileDefinition,
        BoxTileDefinition,
        CardTileDefinition,
        CarouselTileDefinition,
        GridTileDefinition,
        PagerTileDefinition,
        PullToRefreshTileDefinition,
        ShimmerTileDefinition,
        TextFieldTileDefinition,
        SimpleTextTileDefinition,
        MenuTileDefinition,
        TopAppBarTileDefinition,
        BottomAppBarTileDefinition,
        BadgeTileDefinition,
        IconButtonTileDefinition,
        CheckboxTileDefinition,
        AssistChipTileDefinition,
        FilterChipTileDefinition,
        InputChipTileDefinition,
        SuggestionChipTileDefinition,
        NavigationBarTileDefinition,
        NavigationRailTileDefinition,
        CircularProgressIndicatorTileDefinition,
        LinearProgressIndicatorTileDefinition,
        SearchBarTileDefinition,
        SwitchTileDefinition,
        TabsTileDefinition,
        RadioButtonTileDefinition,
        AsyncImageTileDefinition,
        ImageTileDefinition,
        IconTileDefinition,
        NestedNavigationGraphTileDefinition,
        LazyTilesTileDefinition
    )

    private val baseEventsDefinitions = listOf(
        DownloadFileEventDefinition,
        SendFileEventDefinition,
        SendNetworkRequestEventDefinition,
        SetIncomingDataToNetworkParamsHolderBodyEventDefinition,
        SetIncomingDataToNetworkParamsHolderHeadersEventDefinition,
        SetIncomingDataToNetworkParamsHolderUrlEventDefinition,
        NavigateEventDefinition,
        NavigateUpEventDefinition,
        ToggleMenuEventDefinition,
        ScrollTileColumnEventDefinition,
        ScrollTilePagerEventDefinition,
        ScrollRowTileEventDefinition,
        AddTilesEventDefinition,
        CheckIfTileContainsChildrenEventDefinition,
        GetTileChildrenCountEventDefinition,
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
        DisplaySnackbarEventDefinition,
        DismissSnackbarEventDefinition,
        CheckForReceivedDataEventDefinition,
        GetDataEventDefinition,
        ProcessDataEventDefinition,
        EvaluateDataEventDefinition,
        RemoveDataEventDefinition,
        SendDataEventDefinition,
        TransformDataEventDefinition,
        UpdateDataEventDefinition,
        RunEventsEventDefinition,
        TriggerEventEventDefinition,
        RequestPermissionEventDefinition,
        StopRefreshingEventDefinition,
        ChangeScreenStateEventDefinition,
        GetScreenEventDefinition,
        RefreshScreenEventDefinition,
        BroadcastToSystemEventDefinition,
        CheckIfHasInternetConnectionEventDefinition,
        DeleteFileEventDefinition,
        GetFileEventDefinition,
        SaveFileEventDefinition,
        StartCountdownTimerEventDefinition,
        UpdateEventsEventDefinition,
        ReloadLazyTilesEventDefinition
    )

    private val dataProcessorsModule = module {
        single { EventRunnerDataProcessor } bind DataProcessor::class
    }
}

internal expect val platformModule: Module
