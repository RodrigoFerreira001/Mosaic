package dev.catbit.mosaic.core.serialization

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.InlineEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnBottomSheetDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataSentEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDialogDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardDoneEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardGoEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardNextEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardPreviousEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnKeyboardSendEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLeadingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLoadTilesSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationBarItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationRailItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPullEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryClearedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSelectChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSelectEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesWipedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTrailingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUnselectEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.BoxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ColumnTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ShimmerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.IconTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.SwitchTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

class MosaicSerializer(
    tileSerializers: Map<KClass<out TileSchema>, KSerializer<out TileSchema>> = mapOf(),
    eventSerializers: Map<KClass<out EventSchema>, KSerializer<out EventSchema>> = mapOf(),
    eventTriggerSerializers: Map<KClass<out EventTrigger>, KSerializer<out EventTrigger>> = mapOf()
) {

    @Suppress("UNCHECKED_CAST")
    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(TileSchema::class) {
                (defaultTileSerializers + tileSerializers).forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<TileSchema>,
                        serializer as KSerializer<TileSchema>
                    )
                }
            }

            polymorphic(EventSchema::class) {
                (defaultEventSerializers + eventSerializers).forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<EventSchema>,
                        serializer as KSerializer<EventSchema>
                    )
                }
            }

            polymorphic(EventTrigger::class) {
                (defaultEventTriggerSerializers + eventTriggerSerializers).forEach { (kClass, serializer) ->
                    subclass(
                        kClass as KClass<EventTrigger>,
                        serializer as KSerializer<EventTrigger>
                    )
                }
            }
        }
        explicitNulls = false
        encodeDefaults = true
    }

    fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T
    ): String = json.encodeToString(serializer, value)

    fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String
    ): T = json.decodeFromString(deserializer, string)

    fun <T> encodeToJsonElement(
        serializer: SerializationStrategy<T>,
        value: T
    ): JsonElement = json.encodeToJsonElement(serializer, value)

    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    fun encodeTileToJsonElement(
        tile: TileSchema
    ) = json.encodeToJsonElement(
        serializer = tile::class.serializer() as KSerializer<TileSchema>,
        value = tile
    )

    fun decodeTileFromJsonElement(
        jsonElement: JsonElement
    ): TileSchema = json.decodeFromJsonElement(
        deserializer = PolymorphicSerializer(TileSchema::class),
        element = jsonElement
    )

    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    fun encodeEventToJsonElement(
        event: EventSchema
    ) = json.encodeToJsonElement(
        serializer = event::class.serializer() as KSerializer<EventSchema>,
        value = event
    )

    fun decodeEventFromJsonElement(
        jsonElement: JsonElement
    ): EventSchema = json.decodeFromJsonElement(
        deserializer = PolymorphicSerializer(EventSchema::class),
        element = jsonElement
    )

    fun <T> decodeFromJsonElement(
        deserializer: DeserializationStrategy<T>,
        element: JsonElement
    ): T = json.decodeFromJsonElement(deserializer, element)

    fun parseToJsonElement(
        string: String
    ): JsonElement = json.parseToJsonElement(string)

    inline fun <reified T> encodeToString(
        value: T
    ): String = encodeToString(serializer(), value)

    inline fun <reified T> decodeFromString(
        string: String
    ): T = decodeFromString(serializer(), string)

    private val defaultEventTriggerSerializers
        get() = mapOf(
            InlineEventTrigger::class to InlineEventTrigger.serializer(),
            OnAsyncImageLoadFailureEventTrigger::class to OnAsyncImageLoadFailureEventTrigger.serializer(),
            OnAsyncImageLoadStartEventTrigger::class to OnAsyncImageLoadStartEventTrigger.serializer(),
            OnAsyncImageLoadSuccessEventTrigger::class to OnAsyncImageLoadSuccessEventTrigger.serializer(),
            OnBottomSheetDismissedEventTrigger::class to OnBottomSheetDismissedEventTrigger.serializer(),
            OnClickEventTrigger::class to OnClickEventTrigger.serializer(),
            OnCheckChangedEventTrigger::class to OnCheckChangedEventTrigger.serializer(),
            OnCheckEventTrigger::class to OnCheckEventTrigger.serializer(),
            OnCountdownTimerTickEventTrigger::class to OnCountdownTimerTickEventTrigger.serializer(),
            OnCountdownTimerFinishEventTrigger::class to OnCountdownTimerFinishEventTrigger.serializer(),
            OnDisplayEventTrigger::class to OnDisplayEventTrigger.serializer(),
            OnDataReceivedEventTrigger::class to OnDataReceivedEventTrigger.serializer(),
            OnDataRemovedEventTrigger::class to OnDataRemovedEventTrigger.serializer(),
            OnDataSentEventTrigger::class to OnDataSentEventTrigger.serializer(),
            OnDataUpdatedEventTrigger::class to OnDataUpdatedEventTrigger.serializer(),
            OnDialogDismissedEventTrigger::class to OnDialogDismissedEventTrigger.serializer(),
            OnDownloadFailureEventTrigger::class to OnDownloadFailureEventTrigger.serializer(),
            OnDownloadFinishEventTrigger::class to OnDownloadFinishEventTrigger.serializer(),
            OnDownloadProgressEventTrigger::class to OnDownloadProgressEventTrigger.serializer(),
            OnFailureEventTrigger::class to OnFailureEventTrigger.serializer(),
            OnKeyboardDoneEventTrigger::class to OnKeyboardDoneEventTrigger.serializer(),
            OnKeyboardGoEventTrigger::class to OnKeyboardGoEventTrigger.serializer(),
            OnKeyboardNextEventTrigger::class to OnKeyboardNextEventTrigger.serializer(),
            OnKeyboardPreviousEventTrigger::class to OnKeyboardPreviousEventTrigger.serializer(),
            OnKeyboardSearchEventTrigger::class to OnKeyboardSearchEventTrigger.serializer(),
            OnKeyboardSendEventTrigger::class to OnKeyboardSendEventTrigger.serializer(),
            OnLeadingIconClickEventTrigger::class to OnLeadingIconClickEventTrigger.serializer(),
            OnLoadTilesFailureEventTrigger::class to OnLoadTilesFailureEventTrigger.serializer(),
            OnLoadTilesStartEventTrigger::class to OnLoadTilesStartEventTrigger.serializer(),
            OnLoadTilesSuccessEventTrigger::class to OnLoadTilesSuccessEventTrigger.serializer(),
            OnLongPressEventTrigger::class to OnLongPressEventTrigger.serializer(),
            OnMenuItemClickEventTrigger::class to OnMenuItemClickEventTrigger.serializer(),
            OnNavigationBarItemClickEventTrigger::class to OnNavigationBarItemClickEventTrigger.serializer(),
            OnNavigationDrawerDismissedEventTrigger::class to OnNavigationDrawerDismissedEventTrigger.serializer(),
            OnNavigationEntryChangedEventTrigger::class to OnNavigationEntryChangedEventTrigger.serializer(),
            OnNavigationEntrySetEventTrigger::class to OnNavigationEntrySetEventTrigger.serializer(),
            OnNavigationEventTrigger::class to OnNavigationEventTrigger.serializer(),
            OnNavigationRailItemClickEventTrigger::class to OnNavigationRailItemClickEventTrigger.serializer(),
            OnNetworkResponseTrigger::class to OnNetworkResponseTrigger.serializer(),
            OnPermissionsAcquiredEventTrigger::class to OnPermissionsAcquiredEventTrigger.serializer(),
            OnPermissionsDeniedEventTrigger::class to OnPermissionsDeniedEventTrigger.serializer(),
            OnPullEventTrigger::class to OnPullEventTrigger.serializer(),
            OnQueryChangedEventTrigger::class to OnQueryChangedEventTrigger.serializer(),
            OnQueryClearedEventTrigger::class to OnQueryClearedEventTrigger.serializer(),
            OnScrolledEventTrigger::class to OnScrolledEventTrigger.serializer(),
            OnSearchEventTrigger::class to OnSearchEventTrigger.serializer(),
            OnSelectChangedEventTrigger::class to OnSelectChangedEventTrigger.serializer(),
            OnSelectEventTrigger::class to OnSelectEventTrigger.serializer(),
            OnStartEventTrigger::class to OnStartEventTrigger.serializer(),
            OnSuccessEventTrigger::class to OnSuccessEventTrigger.serializer(),
            OnTabItemClickEventTrigger::class to OnTabItemClickEventTrigger.serializer(),
            OnTextChangedEventTrigger::class to OnTextChangedEventTrigger.serializer(),
            OnTilesAddedEventTrigger::class to OnTilesAddedEventTrigger.serializer(),
            OnTilesRemovedEventTrigger::class to OnTilesRemovedEventTrigger.serializer(),
            OnTilesReplacedEventTrigger::class to OnTilesReplacedEventTrigger.serializer(),
            OnTilesUpdatedEventTrigger::class to OnTilesUpdatedEventTrigger.serializer(),
            OnTilesWipedEventTrigger::class to OnTilesWipedEventTrigger.serializer(),
            OnTrailingIconClickEventTrigger::class to OnTrailingIconClickEventTrigger.serializer(),
            OnUncheckEventTrigger::class to OnUncheckEventTrigger.serializer(),
            OnUnselectEventTrigger::class to OnUnselectEventTrigger.serializer(),
        )

    private val defaultTileSerializers
        get() = mapOf(
            ButtonTileSchema::class to ButtonTileSchema.serializer(),
            BoxTileSchema::class to BoxTileSchema.serializer(),
            CardTileSchema::class to CardTileSchema.serializer(),
            CarouselTileSchema::class to CarouselTileSchema.serializer(),
            ColumnTileSchema::class to ColumnTileSchema.serializer(),
            GridTileSchema::class to GridTileSchema.serializer(),
            PagerTileSchema::class to PagerTileSchema.serializer(),
            PullToRefreshTileSchema::class to PullToRefreshTileSchema.serializer(),
            RowTileSchema::class to RowTileSchema.serializer(),
            ShimmerTileSchema::class to ShimmerTileSchema.serializer(),
            TextFieldTileSchema::class to TextFieldTileSchema.serializer(),
            MenuTileSchema::class to MenuTileSchema.serializer(),
            SimpleTextTileSchema::class to SimpleTextTileSchema.serializer(),
            TopAppBarTileSchema::class to TopAppBarTileSchema.serializer(),
            BottomAppBarTileSchema::class to BottomAppBarTileSchema.serializer(),
            BadgeTileSchema::class to BadgeTileSchema.serializer(),
            FloatingActionButtonTileSchema::class to FloatingActionButtonTileSchema.serializer(),
            IconButtonTileSchema::class to IconButtonTileSchema.serializer(),
            CheckboxTileSchema::class to CheckboxTileSchema.serializer(),
            SuggestionChipTileSchema::class to SuggestionChipTileSchema.serializer(),
            NavigationBarTileSchema::class to NavigationBarTileSchema.serializer(),
            NavigationRailTileSchema::class to NavigationRailTileSchema.serializer(),
            CircularProgressIndicatorTileSchema::class to CircularProgressIndicatorTileSchema.serializer(),
            LinearProgressIndicatorTileSchema::class to LinearProgressIndicatorTileSchema.serializer(),
            SearchBarTileSchema::class to SearchBarTileSchema.serializer(),
            SwitchTileSchema::class to SwitchTileSchema.serializer(),
            TabsTileSchema::class to TabsTileSchema.serializer(),
            RadioButtonTileSchema::class to RadioButtonTileSchema.serializer(),
            AsyncImageTileSchema::class to AsyncImageTileSchema.serializer(),
            IconTileSchema::class to IconTileSchema.serializer(),
            NestedNavigationGraphTileSchema::class to NestedNavigationGraphTileSchema.serializer(),
            LazyTilesTileSchema::class to LazyTilesTileSchema.serializer()
        )

    private val defaultEventSerializers
        get() = mapOf(
            CheckForReceivedDataEventSchema::class to CheckForReceivedDataEventSchema.serializer(),
            EvaluateDataEventSchema::class to EvaluateDataEventSchema.serializer(),
            GetDataEventSchema::class to GetDataEventSchema.serializer(),
            ProcessDataEventSchema::class to ProcessDataEventSchema.serializer(),
            RemoveDataEventSchema::class to RemoveDataEventSchema.serializer(),
            SendDataEventSchema::class to SendDataEventSchema.serializer(),
            TransformDataEventSchema::class to TransformDataEventSchema.serializer(),
            UpdateDataEventSchema::class to UpdateDataEventSchema.serializer(),
            TriggerEventEventSchema::class to TriggerEventEventSchema.serializer(),
            UpdateEventsEventSchema::class to UpdateEventsEventSchema.serializer(),
            DeleteFileEventSchema::class to DeleteFileEventSchema.serializer(),
            GetFileEventSchema::class to GetFileEventSchema.serializer(),
            SaveFileEventSchema::class to SaveFileEventSchema.serializer(),
            ToggleMenuEventSchema::class to ToggleMenuEventSchema.serializer(),
            NavigateEventSchema::class to NavigateEventSchema.serializer(),
            NavigateUpEventSchema::class to NavigateUpEventSchema.serializer(),
            DownloadFileEventSchema::class to DownloadFileEventSchema.serializer(),
            SendNetworkRequestEventSchema::class to SendNetworkRequestEventSchema.serializer(),
            DismissBottomSheetEventSchema::class to DismissBottomSheetEventSchema.serializer(),
            DisplayBottomSheetEventSchema::class to DisplayBottomSheetEventSchema.serializer(),
            DismissDialogEventSchema::class to DismissDialogEventSchema.serializer(),
            DisplayDialogEventSchema::class to DisplayDialogEventSchema.serializer(),
            DismissNavigationDrawerEventSchema::class to DismissNavigationDrawerEventSchema.serializer(),
            DisplayNavigationDrawerEventSchema::class to DisplayNavigationDrawerEventSchema.serializer(),
            StopRefreshingEventSchema::class to StopRefreshingEventSchema.serializer(),
            ChangeScreenStateEventSchema::class to ChangeScreenStateEventSchema.serializer(),
            GetScreenEventSchema::class to GetScreenEventSchema.serializer(),
            RefreshScreenEventSchema::class to RefreshScreenEventSchema.serializer(),
            ScrollColumnTileEventSchema::class to ScrollColumnTileEventSchema.serializer(),
            ScrollPagerTileEventSchema::class to ScrollPagerTileEventSchema.serializer(),
            ScrollRowTileEventSchema::class to ScrollRowTileEventSchema.serializer(),
            RequestPermissionEventSchema::class to RequestPermissionEventSchema.serializer(),
            CheckIfHasInternetConnectionEventSchema::class to CheckIfHasInternetConnectionEventSchema.serializer(),
            AddTilesEventSchema::class to AddTilesEventSchema.serializer(),
            RemoveTilesEventSchema::class to RemoveTilesEventSchema.serializer(),
            ReplaceTilesEventSchema::class to ReplaceTilesEventSchema.serializer(),
            UpdateTilesEventSchema::class to UpdateTilesEventSchema.serializer(),
            WipeTilesEventSchema::class to WipeTilesEventSchema.serializer(),
            ReloadLazyTilesEventSchema::class to ReloadLazyTilesEventSchema.serializer()
        )
}