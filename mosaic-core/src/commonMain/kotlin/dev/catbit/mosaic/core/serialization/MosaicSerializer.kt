package dev.catbit.mosaic.core.serialization

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema
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
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnBottomSheetDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataSentEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDialogDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnLongPressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnMenuItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationDrawerDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPullEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesAddedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesRemovedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesReplacedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesUpdatedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTilesWipedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.BoxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CardTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.ColumnTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.GridTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PagerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.PullToRefreshTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.RowTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.containers.ShimmerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.TextTileSchema
import kotlin.reflect.KClass
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
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
            OnBottomSheetDismissedEventTrigger::class to OnBottomSheetDismissedEventTrigger.serializer(),
            OnClickEventTrigger::class to OnClickEventTrigger.serializer(),
            OnDataReceivedEventTrigger::class to OnDataReceivedEventTrigger.serializer(),
            OnDataRemovedEventTrigger::class to OnDataRemovedEventTrigger.serializer(),
            OnDataSentEventTrigger::class to OnDataSentEventTrigger.serializer(),
            OnDataUpdatedEventTrigger::class to OnDataUpdatedEventTrigger.serializer(),
            OnDialogDismissedEventTrigger::class to OnDialogDismissedEventTrigger.serializer(),
            OnDownloadFailureEventTrigger::class to OnDownloadFailureEventTrigger.serializer(),
            OnDownloadFinishEventTrigger::class to OnDownloadFinishEventTrigger.serializer(),
            OnDownloadProgressEventTrigger::class to OnDownloadProgressEventTrigger.serializer(),
            OnFailureEventTrigger::class to OnFailureEventTrigger.serializer(),
            OnLongPressEventTrigger::class to OnLongPressEventTrigger.serializer(),
            OnMenuItemClickEventTrigger::class to OnMenuItemClickEventTrigger.serializer(),
            OnNavigationDrawerDismissedEventTrigger::class to OnNavigationDrawerDismissedEventTrigger.serializer(),
            OnNavigationEventTrigger::class to OnNavigationEventTrigger.serializer(),
            OnNetworkResponseTrigger::class to OnNetworkResponseTrigger.serializer(),
            OnPermissionsAcquiredEventTrigger::class to OnPermissionsAcquiredEventTrigger.serializer(),
            OnPermissionsDeniedEventTrigger::class to OnPermissionsDeniedEventTrigger.serializer(),
            OnPullEventTrigger::class to OnPullEventTrigger.serializer(),
            OnScrolledEventTrigger::class to OnScrolledEventTrigger.serializer(),
            OnStartEventTrigger::class to OnStartEventTrigger.serializer(),
            OnSuccessEventTrigger::class to OnSuccessEventTrigger.serializer(),
            OnTextChangedEventTrigger::class to OnTextChangedEventTrigger.serializer(),
            OnTilesAddedEventTrigger::class to OnTilesAddedEventTrigger.serializer(),
            OnTilesRemovedEventTrigger::class to OnTilesRemovedEventTrigger.serializer(),
            OnTilesReplacedEventTrigger::class to OnTilesReplacedEventTrigger.serializer(),
            OnTilesUpdatedEventTrigger::class to OnTilesUpdatedEventTrigger.serializer(),
            OnTilesWipedEventTrigger::class to OnTilesWipedEventTrigger.serializer()
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
            TextTileSchema::class to TextTileSchema.serializer(),
        )

    private val defaultEventSerializers
        get() = mapOf(
            CheckForReceivedDataEventSchema::class to CheckForReceivedDataEventSchema.serializer(),
            GetDataEventSchema::class to GetDataEventSchema.serializer(),
            ProcessDataEventSchema::class to ProcessDataEventSchema.serializer(),
            RemoveDataEventSchema::class to RemoveDataEventSchema.serializer(),
            SendDataEventSchema::class to SendDataEventSchema.serializer(),
            UpdateDataEventSchema::class to UpdateDataEventSchema.serializer(),
            TriggerEventEventSchema::class to TriggerEventEventSchema.serializer(),
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
            ChangeScreenStateEventSchema::class to ChangeScreenStateEventSchema.serializer(),
            GetScreenEventSchema::class to GetScreenEventSchema.serializer(),
            ScrollColumnTileEventSchema::class to ScrollColumnTileEventSchema.serializer(),
            ScrollRowTileEventSchema::class to ScrollRowTileEventSchema.serializer(),
            RequestPermissionEventSchema::class to RequestPermissionEventSchema.serializer(),
            AddTilesEventSchema::class to AddTilesEventSchema.serializer(),
            RemoveTilesEventSchema::class to RemoveTilesEventSchema.serializer(),
            ReplaceTilesEventSchema::class to ReplaceTilesEventSchema.serializer(),
            UpdateTilesEventSchema::class to UpdateTilesEventSchema.serializer(),
            WipeTilesEventSchema::class to WipeTilesEventSchema.serializer()
        )
}