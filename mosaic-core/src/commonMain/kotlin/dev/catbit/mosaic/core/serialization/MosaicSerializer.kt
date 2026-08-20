package dev.catbit.mosaic.core.serialization

import dev.catbit.mosaic.core.data.schemas.event.EventSchema
import dev.catbit.mosaic.core.data.schemas.event.data.AccessModeSchema
import dev.catbit.mosaic.core.data.schemas.event.data.DataSourceSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.CheckForReceivedDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.EvaluateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.GetDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.ProcessDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.RemoveDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.SendDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.TransformDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.data.UpdateDataEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.CancelEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunCancellableEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.RunEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.TriggerEventEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.event.UpdateEventsEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.DeleteFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.GetFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.OpenFilePickerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.file.SaveFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.image.CompressionScheme
import dev.catbit.mosaic.core.data.schemas.event.events.image.GetImageFromGalleryEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.image.TakePictureEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.menu.ToggleMenuEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateClearingStackEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.navigation.NavigateUpEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToDiskEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.DownloadFileToMemoryEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SendNetworkRequestEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderBodyEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderHeadersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.networking.SetIncomingDataToNetworkParamsHolderUrlEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.popup.TogglePopupEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DismissBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.bottom_sheet.DisplayBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DismissModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.modal_bottom_sheet.DisplayModalBottomSheetEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DismissDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.dialog.DisplayDialogEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DismissNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.navigation_drawer.DisplayNavigationDrawerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DismissSnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.overlays.snackbar.DisplaySnackbarEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.pull_to_refresh.StopRefreshingEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.ChangeScreenStateEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.GetScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.screen.RefreshScreenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.column.ScrollColumnTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.pager.ScrollPagerTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.scroll.row.ScrollRowTileEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.security.RequestPermissionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.system.BroadcastToSystemEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.system.CheckIfHasInternetConnectionEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.system.DropCachesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.system.OpenExternalLinkEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.theme.ResetThemeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.theme.SetThemeEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.AddTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.CheckIfTileContainsChildrenEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.GetTileChildrenCountEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReloadLazyTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.RemoveTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.ReplaceTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.UpdateTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.tiles.WipeTilesEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartCountdownTimerEventSchema
import dev.catbit.mosaic.core.data.schemas.event.events.time.StartTimeLoopEventSchema
import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.InlineEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnAsyncImageLoadSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCancelledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnCountdownTimerTickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDataReceivedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDisplayEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDatePickerCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDatePickerOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDateSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDownloadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListItemSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnDropdownListOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnHeightBreakpointNotSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnHeightBreakpointSatisfiedEventTrigger
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
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationEntrySetEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNavigationRailItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkFailureEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnNetworkResponseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPageChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsAcquiredEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPermissionsDeniedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPullEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnPauseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnResumeEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnQueryClearedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScreenStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScreenStopEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrollThresholdReachedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnScrolledEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSearchEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSelectEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarActionEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSnackbarDismissedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnStartEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSuccessEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnSystemBroadcastEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTabItemClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTextChangedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeFinishEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeLoopEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimePickerCloseEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimePickerOpenEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTimeSelectedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnTrailingIconClickEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUncheckEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnUploadProgressEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnWidthBreakpointNotSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.event.trigger.triggers.OnWidthBreakpointSatisfiedEventTrigger
import dev.catbit.mosaic.core.data.schemas.tile.TileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.BottomAppBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.app_bars.TopAppBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.badges.BadgeTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.ButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.FloatingActionButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.buttons.IconButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.AssistChipTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.FilterChipTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.InputChipTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.chips.SuggestionChipTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.AdaptiveVisibilityTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.BoxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.SelectionContainerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CardTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.CarouselTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ColumnTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlexBoxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.FlowRowTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.GridTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyColumnTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyRowTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.LazyTilesTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PagerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.PullToRefreshTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.RowTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.grouping.ShimmerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.AsyncImageTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.IconTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.image.ImageTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.CheckboxTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DatePickerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.DropdownListTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.RadioButtonTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.SwitchTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema.KeyboardOptions
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TextFieldTileSchema.VisualTransformation
import dev.catbit.mosaic.core.data.schemas.tile.tiles.inputs.TimePickerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.menu.MenuTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NavigationRailTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.NestedNavigationGraphTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.navigation.TabsTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.popup.PopupTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.CircularProgressIndicatorTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.progress.LinearProgressIndicatorTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.search.SearchBarTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.system.SystemBroadcastListenerTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.text.SimpleTextTileSchema
import dev.catbit.mosaic.core.data.schemas.tile.tiles.tooltip.TooltipTileSchema
import dev.catbit.mosaic.core.serialization.serializers.ImmutableListSerializer
import kotlin.reflect.KClass
import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.serializer

/**
 * The polymorphic JSON serializer shared by `mosaic-server` and `mosaic-client` — the single source
 * of truth for how a [TileSchema]/[EventSchema]/[EventTrigger] subtype resolves to and from JSON,
 * and the mechanism that lets a wire payload carry a mix of built-in and third-party-registered
 * types indistinguishably.
 *
 * Every polymorphic hierarchy is registered by `@SerialName` (each schema's own class carries one),
 * not by any custom discriminator logic — this class only decides *which* classes participate. The
 * built-in catalog is always registered (via the private `defaultTileSerializers`/
 * `defaultEventSerializers`/`defaultEventTriggerSerializers` maps at the bottom of this file, one
 * entry per shipped [TileSchema]/[EventSchema]/[EventTrigger]); the [tileSerializers]/
 * [eventSerializers]/[eventTriggerSerializers] constructor parameters add third-party ones on top —
 * this is exactly what `mosaic-client`'s `MosaicModules` builds from a consuming app's
 * `MosaicDependencyInjectionConfig.tileDefinitions`/`eventDefinitions`/`eventTriggerDefinition`. A
 * class present in both a default map and one of these parameters would collide; the framework never
 * does this itself, since the built-in classes and any third-party ones are necessarily distinct
 * types.
 *
 * Beyond the top-level [TileSchema]/[EventSchema]/[EventTrigger] hierarchies, the `json` builder also
 * registers every *nested* sealed polymorphic type a schema declares internally (e.g.
 * `TextFieldTileSchema.VisualTransformation`, `EvaluateDataEventSchema.Expression` and its whole
 * operation tree, `AddTilesEventSchema.InsertionPosition`, the various `Where`/`Direction`/`FileType`
 * sealed types) — those aren't extensible by third parties the way the 3 top-level hierarchies are,
 * since they're closed `sealed` types owned entirely by their enclosing built-in schema.
 *
 * `Json` is configured with `explicitNulls = false` (an omitted field decodes as `null` rather than
 * failing), `encodeDefaults = true` (default-valued fields are still written to the wire, so a
 * client decoding an older/newer schema version still sees every field explicitly), and
 * `ignoreUnknownKeys = true` (an unrecognized field is silently dropped rather than failing decode —
 * the mechanism that keeps server and client tolerant of small version skew).
 *
 * @param tileSerializers third-party [TileSchema] registrations, merged with the built-in catalog.
 * @param eventSerializers third-party [EventSchema] registrations, merged with the built-in catalog.
 * @param eventTriggerSerializers third-party [EventTrigger] registrations, merged with the built-in
 * catalog.
 * @param additionalSerializersModule extra polymorphic serializers unrelated to the 3 hierarchies
 * above — for a field type that itself needs its own serializer registration, independent of
 * tile/event/trigger registration.
 */
class MosaicSerializer(
    tileSerializers: Map<KClass<out TileSchema>, KSerializer<out TileSchema>> = emptyMap(),
    eventSerializers: Map<KClass<out EventSchema>, KSerializer<out EventSchema>> = emptyMap(),
    eventTriggerSerializers: Map<KClass<out EventTrigger>, KSerializer<out EventTrigger>> = emptyMap(),
    additionalSerializersModule: SerializersModule = EmptySerializersModule(),
) {

    /** The configured [Json] instance every method on this class delegates to. Exposed directly
     * (rather than kept private) for callers that need to hand it to a lower-level API expecting a
     * plain `kotlinx.serialization` `Json` — e.g. Ktor's `ContentNegotiation` plugin
     * (`json(get<MosaicSerializer>().json)`). */
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

            contextual(ImmutableList::class) { args -> ImmutableListSerializer(args[0]) }

            // Tile nested types
            polymorphic(VisualTransformation::class) {
                subclass(VisualTransformation.None::class, VisualTransformation.None.serializer())
                subclass(
                    VisualTransformation.Password::class,
                    VisualTransformation.Password.serializer()
                )
                subclass(
                    VisualTransformation.Custom::class,
                    VisualTransformation.Custom.serializer()
                )
            }

            polymorphic(KeyboardOptions.KeyboardCapitalization::class) {
                subclass(
                    KeyboardOptions.KeyboardCapitalization.Unspecified::class,
                    KeyboardOptions.KeyboardCapitalization.Unspecified.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardCapitalization.None::class,
                    KeyboardOptions.KeyboardCapitalization.None.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardCapitalization.Characters::class,
                    KeyboardOptions.KeyboardCapitalization.Characters.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardCapitalization.Words::class,
                    KeyboardOptions.KeyboardCapitalization.Words.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardCapitalization.Sentences::class,
                    KeyboardOptions.KeyboardCapitalization.Sentences.serializer()
                )
            }

            polymorphic(KeyboardOptions.KeyboardType::class) {
                subclass(
                    KeyboardOptions.KeyboardType.Unspecified::class,
                    KeyboardOptions.KeyboardType.Unspecified.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Text::class,
                    KeyboardOptions.KeyboardType.Text.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Ascii::class,
                    KeyboardOptions.KeyboardType.Ascii.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Number::class,
                    KeyboardOptions.KeyboardType.Number.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Phone::class,
                    KeyboardOptions.KeyboardType.Phone.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Uri::class,
                    KeyboardOptions.KeyboardType.Uri.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Email::class,
                    KeyboardOptions.KeyboardType.Email.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Password::class,
                    KeyboardOptions.KeyboardType.Password.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.NumberPassword::class,
                    KeyboardOptions.KeyboardType.NumberPassword.serializer()
                )
                subclass(
                    KeyboardOptions.KeyboardType.Decimal::class,
                    KeyboardOptions.KeyboardType.Decimal.serializer()
                )
            }

            polymorphic(KeyboardOptions.ImeAction::class) {
                subclass(
                    KeyboardOptions.ImeAction.Unspecified::class,
                    KeyboardOptions.ImeAction.Unspecified.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Default::class,
                    KeyboardOptions.ImeAction.Default.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.None::class,
                    KeyboardOptions.ImeAction.None.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Go::class,
                    KeyboardOptions.ImeAction.Go.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Search::class,
                    KeyboardOptions.ImeAction.Search.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Send::class,
                    KeyboardOptions.ImeAction.Send.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Previous::class,
                    KeyboardOptions.ImeAction.Previous.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Next::class,
                    KeyboardOptions.ImeAction.Next.serializer()
                )
                subclass(
                    KeyboardOptions.ImeAction.Done::class,
                    KeyboardOptions.ImeAction.Done.serializer()
                )
            }

            polymorphic(CarouselTileSchema.CarouselTypeSchema::class) {
                subclass(
                    CarouselTileSchema.CarouselTypeSchema.MultiBrowse::class,
                    CarouselTileSchema.CarouselTypeSchema.MultiBrowse.serializer()
                )
                subclass(
                    CarouselTileSchema.CarouselTypeSchema.Uncontained::class,
                    CarouselTileSchema.CarouselTypeSchema.Uncontained.serializer()
                )
            }

            polymorphic(GridTileSchema.GridTrackSchema::class) {
                subclass(
                    GridTileSchema.GridTrackSchema.Fixed::class,
                    GridTileSchema.GridTrackSchema.Fixed.serializer()
                )
                subclass(
                    GridTileSchema.GridTrackSchema.Fraction::class,
                    GridTileSchema.GridTrackSchema.Fraction.serializer()
                )
                subclass(
                    GridTileSchema.GridTrackSchema.Flexible::class,
                    GridTileSchema.GridTrackSchema.Flexible.serializer()
                )
                subclass(
                    GridTileSchema.GridTrackSchema.Auto::class,
                    GridTileSchema.GridTrackSchema.Auto.serializer()
                )
                subclass(
                    GridTileSchema.GridTrackSchema.MaxContent::class,
                    GridTileSchema.GridTrackSchema.MaxContent.serializer()
                )
                subclass(
                    GridTileSchema.GridTrackSchema.MinContent::class,
                    GridTileSchema.GridTrackSchema.MinContent.serializer()
                )
            }

            polymorphic(PagerTileSchema.PageSizeSchema::class) {
                subclass(
                    PagerTileSchema.PageSizeSchema.Fill::class,
                    PagerTileSchema.PageSizeSchema.Fill.serializer()
                )
                subclass(
                    PagerTileSchema.PageSizeSchema.Fixed::class,
                    PagerTileSchema.PageSizeSchema.Fixed.serializer()
                )
            }

            polymorphic(AsyncImageTileSchema.Model::class) {
                subclass(
                    AsyncImageTileSchema.Model.Url::class,
                    AsyncImageTileSchema.Model.Url.serializer()
                )
                subclass(
                    AsyncImageTileSchema.Model.ArrayOfBytes::class,
                    AsyncImageTileSchema.Model.ArrayOfBytes.serializer()
                )
                subclass(
                    AsyncImageTileSchema.Model.Base64::class,
                    AsyncImageTileSchema.Model.Base64.serializer()
                )
            }

            // Event nested types
            polymorphic(DataSourceSchema::class) {
                subclass(DataSourceSchema.Inline::class, DataSourceSchema.Inline.serializer())
                subclass(
                    DataSourceSchema.SegmentedDataBase::class,
                    DataSourceSchema.SegmentedDataBase.serializer()
                )
                subclass(
                    DataSourceSchema.PlainDataBase::class,
                    DataSourceSchema.PlainDataBase.serializer()
                )
                subclass(
                    DataSourceSchema.ScreenNavigationData::class,
                    DataSourceSchema.ScreenNavigationData.serializer()
                )
                subclass(
                    DataSourceSchema.ScreenPlainData::class,
                    DataSourceSchema.ScreenPlainData.serializer()
                )
                subclass(
                    DataSourceSchema.ScreenSegmentedData::class,
                    DataSourceSchema.ScreenSegmentedData.serializer()
                )
                subclass(DataSourceSchema.Tile::class, DataSourceSchema.Tile.serializer())
            }

            polymorphic(AccessModeSchema::class) {
                subclass(AccessModeSchema.Full::class, AccessModeSchema.Full.serializer())
                subclass(AccessModeSchema.Batch::class, AccessModeSchema.Batch.serializer())
                subclass(AccessModeSchema.Single::class, AccessModeSchema.Single.serializer())
            }

            polymorphic(UpdateDataEventSchema.Update.UpdateDate::class) {
                subclass(
                    UpdateDataEventSchema.Update.UpdateDate.Incoming::class,
                    UpdateDataEventSchema.Update.UpdateDate.Incoming.serializer()
                )
                subclass(
                    UpdateDataEventSchema.Update.UpdateDate.Inline::class,
                    UpdateDataEventSchema.Update.UpdateDate.Inline.serializer()
                )
                subclass(
                    UpdateDataEventSchema.Update.UpdateDate.Explicit::class,
                    UpdateDataEventSchema.Update.UpdateDate.Explicit.serializer()
                )
            }

            polymorphic(UpdateDataEventSchema.Update.UpdateDate.Explicit.ExplicitValue::class) {
                subclass(
                    UpdateDataEventSchema.Update.UpdateDate.Explicit.ExplicitValue.Incoming::class,
                    UpdateDataEventSchema.Update.UpdateDate.Explicit.ExplicitValue.Incoming.serializer()
                )
                subclass(
                    UpdateDataEventSchema.Update.UpdateDate.Explicit.ExplicitValue.Inline::class,
                    UpdateDataEventSchema.Update.UpdateDate.Explicit.ExplicitValue.Inline.serializer()
                )
            }

            polymorphic(UpdateTilesEventSchema.Update.UpdateData::class) {
                subclass(
                    UpdateTilesEventSchema.Update.UpdateData.Incoming::class,
                    UpdateTilesEventSchema.Update.UpdateData.Incoming.serializer()
                )
                subclass(
                    UpdateTilesEventSchema.Update.UpdateData.Inline::class,
                    UpdateTilesEventSchema.Update.UpdateData.Inline.serializer()
                )
            }

            polymorphic(EvaluateDataEventSchema.Expression::class) {
                subclass(
                    EvaluateDataEventSchema.Expression.NotExpression::class,
                    EvaluateDataEventSchema.Expression.NotExpression.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.OrExpression::class,
                    EvaluateDataEventSchema.Expression.OrExpression.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.AndExpression::class,
                    EvaluateDataEventSchema.Expression.AndExpression.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression::class,
                    EvaluateDataEventSchema.Expression.DataExpression.serializer()
                )
            }

            polymorphic(EvaluateDataEventSchema.Expression.DataExpression.Data::class) {
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Data.IncomingData::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Data.IncomingData.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Data.DataSourceData::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Data.DataSourceData.serializer()
                )
            }

            polymorphic(EvaluateDataEventSchema.Expression.DataExpression.Operation::class) {
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.NullOperation.IsNull::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.NullOperation.IsNull.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.NullOperation.IsNotNull::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.NullOperation.IsNotNull.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsLengthBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.MatchesRegex::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.MatchesRegex.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.Contains::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.Contains.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.StartsWith::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.StartsWith.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.EndsWith::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.EndsWith.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.EqualsIgnoreCase::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.EqualsIgnoreCase.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsBlank::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsBlank.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsNotBlank::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.StringOperation.IsNotBlank.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsEven::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsEven.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsOdd::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsOdd.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.IntOperation.IsBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LongOperation.IsBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.FloatOperation.IsBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.DoubleOperation.IsBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.BooleanOperation.IsFalse::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.BooleanOperation.IsFalse.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.BooleanOperation.IsTrue::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.BooleanOperation.IsTrue.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.ContainsKey::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.ContainsKey.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.ContainsValue::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.ContainsValue.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsEmpty::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsEmpty.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsNotEmpty::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsNotEmpty.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.IsSizeBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.ValueAtKeyEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.MapOperation.ValueAtKeyEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.Contains::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.Contains.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.In::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.In.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsEmpty::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsEmpty.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsNotEmpty::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsNotEmpty.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeSmallerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeSmallerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeSmallerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeSmallerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeEqualsTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeEqualsTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeBiggerThan::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeBiggerThan.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeBiggerThanOrEquals::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.IsSizeBiggerThanOrEquals.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.ContainsAll::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.ContainsAll.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.ContainsAny::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.ListOperation.ContainsAny.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsEqualTo::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsEqualTo.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsBefore::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsBefore.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsAfter::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsAfter.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsWeekend::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsWeekend.serializer()
                )
                subclass(
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsWeekday::class,
                    EvaluateDataEventSchema.Expression.DataExpression.Operation.LocalDateTimeOperation.IsWeekday.serializer()
                )
            }

            polymorphic(ChangeScreenStateEventSchema.State::class) {
                subclass(
                    ChangeScreenStateEventSchema.State.Success::class,
                    ChangeScreenStateEventSchema.State.Success.serializer()
                )
                subclass(
                    ChangeScreenStateEventSchema.State.Failure::class,
                    ChangeScreenStateEventSchema.State.Failure.serializer()
                )
                subclass(
                    ChangeScreenStateEventSchema.State.Initial::class,
                    ChangeScreenStateEventSchema.State.Initial.serializer()
                )
            }

            polymorphic(AddTilesEventSchema.InsertionPosition::class) {
                subclass(
                    AddTilesEventSchema.InsertionPosition.Start::class,
                    AddTilesEventSchema.InsertionPosition.Start.serializer()
                )
                subclass(
                    AddTilesEventSchema.InsertionPosition.End::class,
                    AddTilesEventSchema.InsertionPosition.End.serializer()
                )
                subclass(
                    AddTilesEventSchema.InsertionPosition.BeforeTile::class,
                    AddTilesEventSchema.InsertionPosition.BeforeTile.serializer()
                )
                subclass(
                    AddTilesEventSchema.InsertionPosition.AfterTile::class,
                    AddTilesEventSchema.InsertionPosition.AfterTile.serializer()
                )
                subclass(
                    AddTilesEventSchema.InsertionPosition.AtIndex::class,
                    AddTilesEventSchema.InsertionPosition.AtIndex.serializer()
                )
            }

            polymorphic(ScrollColumnTileEventSchema.Where::class) {
                subclass(
                    ScrollColumnTileEventSchema.Where.Top::class,
                    ScrollColumnTileEventSchema.Where.Top.serializer()
                )
                subclass(
                    ScrollColumnTileEventSchema.Where.To::class,
                    ScrollColumnTileEventSchema.Where.To.serializer()
                )
                subclass(
                    ScrollColumnTileEventSchema.Where.Bottom::class,
                    ScrollColumnTileEventSchema.Where.Bottom.serializer()
                )
            }

            polymorphic(ScrollRowTileEventSchema.Where::class) {
                subclass(
                    ScrollRowTileEventSchema.Where.Start::class,
                    ScrollRowTileEventSchema.Where.Start.serializer()
                )
                subclass(
                    ScrollRowTileEventSchema.Where.To::class,
                    ScrollRowTileEventSchema.Where.To.serializer()
                )
                subclass(
                    ScrollRowTileEventSchema.Where.End::class,
                    ScrollRowTileEventSchema.Where.End.serializer()
                )
            }

            polymorphic(ScrollPagerTileEventSchema.Where::class) {
                subclass(
                    ScrollPagerTileEventSchema.Where.Begin::class,
                    ScrollPagerTileEventSchema.Where.Begin.serializer()
                )
                subclass(
                    ScrollPagerTileEventSchema.Where.PreviousPage::class,
                    ScrollPagerTileEventSchema.Where.PreviousPage.serializer()
                )
                subclass(
                    ScrollPagerTileEventSchema.Where.NextPage::class,
                    ScrollPagerTileEventSchema.Where.NextPage.serializer()
                )
                subclass(
                    ScrollPagerTileEventSchema.Where.End::class,
                    ScrollPagerTileEventSchema.Where.End.serializer()
                )
            }

            polymorphic(OnPageChangedEventTrigger.Direction::class) {
                subclass(
                    OnPageChangedEventTrigger.Direction.Start::class,
                    OnPageChangedEventTrigger.Direction.Start.serializer()
                )
                subclass(
                    OnPageChangedEventTrigger.Direction.End::class,
                    OnPageChangedEventTrigger.Direction.End.serializer()
                )
                subclass(
                    OnPageChangedEventTrigger.Direction.Any::class,
                    OnPageChangedEventTrigger.Direction.Any.serializer()
                )
                subclass(
                    OnPageChangedEventTrigger.Direction.Index::class,
                    OnPageChangedEventTrigger.Direction.Index.serializer()
                )
            }

            polymorphic(OpenFilePickerEventSchema.FileType::class) {
                subclass(
                    OpenFilePickerEventSchema.FileType.Image::class,
                    OpenFilePickerEventSchema.FileType.Image.serializer()
                )
                subclass(
                    OpenFilePickerEventSchema.FileType.Video::class,
                    OpenFilePickerEventSchema.FileType.Video.serializer()
                )
                subclass(
                    OpenFilePickerEventSchema.FileType.ImageAndVideo::class,
                    OpenFilePickerEventSchema.FileType.ImageAndVideo.serializer()
                )
                subclass(
                    OpenFilePickerEventSchema.FileType.File::class,
                    OpenFilePickerEventSchema.FileType.File.serializer()
                )
            }

            polymorphic(OpenFilePickerEventSchema.PickMode::class) {
                subclass(
                    OpenFilePickerEventSchema.PickMode.Single::class,
                    OpenFilePickerEventSchema.PickMode.Single.serializer()
                )
            }

            polymorphic(CompressionScheme::class) {
                subclass(
                    CompressionScheme.ByQuality::class,
                    CompressionScheme.ByQuality.serializer()
                )
                subclass(
                    CompressionScheme.ByTargetSize::class,
                    CompressionScheme.ByTargetSize.serializer()
                )
            }

        }.plus(additionalSerializersModule)
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    /** Encodes [value] to a JSON string using the given [serializer] explicitly (rather than one
     * resolved from [T]'s reified type) — the general escape hatch for a caller that already has its
     * own `SerializationStrategy`. */
    fun <T> encodeToString(
        serializer: SerializationStrategy<T>,
        value: T
    ): String = json.encodeToString(serializer, value)

    /** Decodes [string] as [T] using the given [deserializer] explicitly. */
    fun <T> decodeFromString(
        deserializer: DeserializationStrategy<T>,
        string: String
    ): T = json.decodeFromString(deserializer, string)

    /** Encodes [value] to a [JsonElement] tree (not a string) using the given [serializer]. */
    fun <T> encodeToJsonElement(
        serializer: SerializationStrategy<T>,
        value: T
    ): JsonElement = json.encodeToJsonElement(serializer, value)

    /**
     * Encodes [tile] to a [JsonElement], resolving its serializer from its own runtime class — the
     * mechanism `TileHolder.update` uses to encode a tile's current state before merging a JSON
     * patch onto it, without the generic `TileHolder<T>` needing to know `T`'s concrete serializer
     * ahead of time.
     */
    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    fun encodeTileToJsonElement(
        tile: TileSchema
    ) = json.encodeToJsonElement(
        serializer = tile::class.serializer() as KSerializer<TileSchema>,
        value = tile
    )

    /**
     * Decodes [jsonElement] as a [TileSchema], resolving the concrete subtype polymorphically by its
     * own `@SerialName` — the general entry point for decoding one tile out of a JSON tree whose
     * concrete type isn't known ahead of time (e.g. one entry of a `tiles` array).
     */
    fun decodeTileFromJsonElement(
        jsonElement: JsonElement
    ): TileSchema = json.decodeFromJsonElement(
        deserializer = PolymorphicSerializer(TileSchema::class),
        element = jsonElement
    )

    /** Same as [encodeTileToJsonElement], for an [EventSchema] — used by `EventHolder.update`. */
    @OptIn(InternalSerializationApi::class)
    @Suppress("UNCHECKED_CAST")
    fun encodeEventToJsonElement(
        event: EventSchema
    ) = json.encodeToJsonElement(
        serializer = event::class.serializer() as KSerializer<EventSchema>,
        value = event
    )

    /** Same as [decodeTileFromJsonElement], for an [EventSchema] — the mechanism behind
     * `EventRunnerDataProcessor` decoding a broadcast/pushed payload as an event to run inline, and
     * `MosaicRepository`'s own screen-payload decoding. */
    fun decodeEventFromJsonElement(
        jsonElement: JsonElement
    ): EventSchema = json.decodeFromJsonElement(
        deserializer = PolymorphicSerializer(EventSchema::class),
        element = jsonElement
    )

    /** Decodes [element] as [T] using the given [deserializer] explicitly — the general escape hatch
     * for a caller that already has its own `DeserializationStrategy` and a [JsonElement] rather than
     * a raw string. */
    fun <T> decodeFromJsonElement(
        deserializer: DeserializationStrategy<T>,
        element: JsonElement
    ): T = json.decodeFromJsonElement(deserializer, element)

    /** Parses [string] into a raw [JsonElement] tree, without decoding it into any typed model —
     * the first step before a polymorphic decode call that needs the tree to inspect/merge before
     * committing to a concrete type. */
    fun parseToJsonElement(
        string: String
    ): JsonElement = json.parseToJsonElement(string)

    /** Encodes [value] to a JSON string, resolving [T]'s serializer from this serializer's own
     * `serializersModule` via reification — the convenient overload for a caller that has a
     * compile-time type to encode, rather than an explicit `SerializationStrategy`. */
    inline fun <reified T> encodeToString(
        value: T
    ): String = encodeToString(json.serializersModule.serializer(), value)

    /** Decodes [string] as [T], resolving [T]'s serializer from this serializer's own
     * `serializersModule` via reification. */
    inline fun <reified T> decodeFromString(
        string: String
    ): T = decodeFromString(json.serializersModule.serializer(), string)

    /** Every built-in [EventTrigger] subtype's serializer, one entry per shipped trigger — merged
     * with the constructor's [eventTriggerSerializers] parameter when building the `json` block's
     * `EventTrigger` polymorphic registration. */
    private val defaultEventTriggerSerializers
        get() = mapOf(
            InlineEventTrigger::class to InlineEventTrigger.serializer(),
            OnAsyncImageLoadFailureEventTrigger::class to OnAsyncImageLoadFailureEventTrigger.serializer(),
            OnAsyncImageLoadStartEventTrigger::class to OnAsyncImageLoadStartEventTrigger.serializer(),
            OnAsyncImageLoadSuccessEventTrigger::class to OnAsyncImageLoadSuccessEventTrigger.serializer(),
            OnClickEventTrigger::class to OnClickEventTrigger.serializer(),
            OnCheckChangedEventTrigger::class to OnCheckChangedEventTrigger.serializer(),
            OnCheckEventTrigger::class to OnCheckEventTrigger.serializer(),
            OnCountdownTimerTickEventTrigger::class to OnCountdownTimerTickEventTrigger.serializer(),
            OnDisplayEventTrigger::class to OnDisplayEventTrigger.serializer(),
            OnDropdownListCloseEventTrigger::class to OnDropdownListCloseEventTrigger.serializer(),
            OnDropdownListItemSelectedEventTrigger::class to OnDropdownListItemSelectedEventTrigger.serializer(),
            OnDropdownListOpenEventTrigger::class to OnDropdownListOpenEventTrigger.serializer(),
            OnDataReceivedEventTrigger::class to OnDataReceivedEventTrigger.serializer(),
            OnDatePickerOpenEventTrigger::class to OnDatePickerOpenEventTrigger.serializer(),
            OnDatePickerCloseEventTrigger::class to OnDatePickerCloseEventTrigger.serializer(),
            OnDateSelectedEventTrigger::class to OnDateSelectedEventTrigger.serializer(),
            OnDownloadFailureEventTrigger::class to OnDownloadFailureEventTrigger.serializer(),
            OnDownloadFinishEventTrigger::class to OnDownloadFinishEventTrigger.serializer(),
            OnDownloadProgressEventTrigger::class to OnDownloadProgressEventTrigger.serializer(),
            OnFailureEventTrigger::class to OnFailureEventTrigger.serializer(),
            OnHeightBreakpointSatisfiedEventTrigger::class to OnHeightBreakpointSatisfiedEventTrigger.serializer(),
            OnHeightBreakpointNotSatisfiedEventTrigger::class to OnHeightBreakpointNotSatisfiedEventTrigger.serializer(),
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
            OnPageChangedEventTrigger::class to OnPageChangedEventTrigger.serializer(),
            OnNavigationEntrySetEventTrigger::class to OnNavigationEntrySetEventTrigger.serializer(),
            OnNavigationRailItemClickEventTrigger::class to OnNavigationRailItemClickEventTrigger.serializer(),
            OnNetworkFailureEventTrigger::class to OnNetworkFailureEventTrigger.serializer(),
            OnNetworkResponseEventTrigger::class to OnNetworkResponseEventTrigger.serializer(),
            OnPermissionsAcquiredEventTrigger::class to OnPermissionsAcquiredEventTrigger.serializer(),
            OnPermissionsDeniedEventTrigger::class to OnPermissionsDeniedEventTrigger.serializer(),
            OnPauseEventTrigger::class to OnPauseEventTrigger.serializer(),
            OnPullEventTrigger::class to OnPullEventTrigger.serializer(),
            OnQueryChangedEventTrigger::class to OnQueryChangedEventTrigger.serializer(),
            OnQueryClearedEventTrigger::class to OnQueryClearedEventTrigger.serializer(),
            OnResumeEventTrigger::class to OnResumeEventTrigger.serializer(),
            OnScreenStartEventTrigger::class to OnScreenStartEventTrigger.serializer(),
            OnScreenStopEventTrigger::class to OnScreenStopEventTrigger.serializer(),
            OnScrolledEventTrigger::class to OnScrolledEventTrigger.serializer(),
            OnScrollThresholdReachedEventTrigger::class to OnScrollThresholdReachedEventTrigger.serializer(),
            OnSearchEventTrigger::class to OnSearchEventTrigger.serializer(),
            OnSnackbarActionEventTrigger::class to OnSnackbarActionEventTrigger.serializer(),
            OnSnackbarDismissedEventTrigger::class to OnSnackbarDismissedEventTrigger.serializer(),
            OnSelectEventTrigger::class to OnSelectEventTrigger.serializer(),
            OnStartEventTrigger::class to OnStartEventTrigger.serializer(),
            OnSuccessEventTrigger::class to OnSuccessEventTrigger.serializer(),
            OnCancelledEventTrigger::class to OnCancelledEventTrigger.serializer(),
            OnSystemBroadcastEventTrigger::class to OnSystemBroadcastEventTrigger.serializer(),
            OnTabItemClickEventTrigger::class to OnTabItemClickEventTrigger.serializer(),
            OnTextChangedEventTrigger::class to OnTextChangedEventTrigger.serializer(),
            OnTimeFinishEventTrigger::class to OnTimeFinishEventTrigger.serializer(),
            OnTimeLoopEventTrigger::class to OnTimeLoopEventTrigger.serializer(),
            OnTimePickerOpenEventTrigger::class to OnTimePickerOpenEventTrigger.serializer(),
            OnTimePickerCloseEventTrigger::class to OnTimePickerCloseEventTrigger.serializer(),
            OnTimeSelectedEventTrigger::class to OnTimeSelectedEventTrigger.serializer(),
            OnTrailingIconClickEventTrigger::class to OnTrailingIconClickEventTrigger.serializer(),
            OnUncheckEventTrigger::class to OnUncheckEventTrigger.serializer(),
            OnUploadProgressEventTrigger::class to OnUploadProgressEventTrigger.serializer(),
            OnWidthBreakpointSatisfiedEventTrigger::class to OnWidthBreakpointSatisfiedEventTrigger.serializer(),
            OnWidthBreakpointNotSatisfiedEventTrigger::class to OnWidthBreakpointNotSatisfiedEventTrigger.serializer(),
        )

    /** Every built-in [TileSchema] subtype's serializer, one entry per shipped tile — merged with
     * the constructor's [tileSerializers] parameter. */
    private val defaultTileSerializers
        get() = mapOf(
            AdaptiveVisibilityTileSchema::class to AdaptiveVisibilityTileSchema.serializer(),
            ButtonTileSchema::class to ButtonTileSchema.serializer(),
            BoxTileSchema::class to BoxTileSchema.serializer(),
            SelectionContainerTileSchema::class to SelectionContainerTileSchema.serializer(),
            CardTileSchema::class to CardTileSchema.serializer(),
            CarouselTileSchema::class to CarouselTileSchema.serializer(),
            ColumnTileSchema::class to ColumnTileSchema.serializer(),
            LazyColumnTileSchema::class to LazyColumnTileSchema.serializer(),
            LazyRowTileSchema::class to LazyRowTileSchema.serializer(),
            GridTileSchema::class to GridTileSchema.serializer(),
            PagerTileSchema::class to PagerTileSchema.serializer(),
            PullToRefreshTileSchema::class to PullToRefreshTileSchema.serializer(),
            RowTileSchema::class to RowTileSchema.serializer(),
            ShimmerTileSchema::class to ShimmerTileSchema.serializer(),
            TextFieldTileSchema::class to TextFieldTileSchema.serializer(),
            MenuTileSchema::class to MenuTileSchema.serializer(),
            PopupTileSchema::class to PopupTileSchema.serializer(),
            TooltipTileSchema::class to TooltipTileSchema.serializer(),
            SimpleTextTileSchema::class to SimpleTextTileSchema.serializer(),
            SystemBroadcastListenerTileSchema::class to SystemBroadcastListenerTileSchema.serializer(),
            TopAppBarTileSchema::class to TopAppBarTileSchema.serializer(),
            BottomAppBarTileSchema::class to BottomAppBarTileSchema.serializer(),
            BadgeTileSchema::class to BadgeTileSchema.serializer(),
            FloatingActionButtonTileSchema::class to FloatingActionButtonTileSchema.serializer(),
            IconButtonTileSchema::class to IconButtonTileSchema.serializer(),
            CheckboxTileSchema::class to CheckboxTileSchema.serializer(),
            DropdownListTileSchema::class to DropdownListTileSchema.serializer(),
            DatePickerTileSchema::class to DatePickerTileSchema.serializer(),
            TimePickerTileSchema::class to TimePickerTileSchema.serializer(),
            AssistChipTileSchema::class to AssistChipTileSchema.serializer(),
            FilterChipTileSchema::class to FilterChipTileSchema.serializer(),
            InputChipTileSchema::class to InputChipTileSchema.serializer(),
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
            ImageTileSchema::class to ImageTileSchema.serializer(),
            IconTileSchema::class to IconTileSchema.serializer(),
            NestedNavigationGraphTileSchema::class to NestedNavigationGraphTileSchema.serializer(),
            LazyTilesTileSchema::class to LazyTilesTileSchema.serializer(),
            FlowRowTileSchema::class to FlowRowTileSchema.serializer(),
            FlexBoxTileSchema::class to FlexBoxTileSchema.serializer()
        )

    /** Every built-in [EventSchema] subtype's serializer, one entry per shipped event — merged with
     * the constructor's [eventSerializers] parameter. */
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
            RunEventsEventSchema::class to RunEventsEventSchema.serializer(),
            RunCancellableEventsEventSchema::class to RunCancellableEventsEventSchema.serializer(),
            CancelEventsEventSchema::class to CancelEventsEventSchema.serializer(),
            TriggerEventEventSchema::class to TriggerEventEventSchema.serializer(),
            UpdateEventsEventSchema::class to UpdateEventsEventSchema.serializer(),
            DeleteFileEventSchema::class to DeleteFileEventSchema.serializer(),
            GetFileEventSchema::class to GetFileEventSchema.serializer(),
            OpenFilePickerEventSchema::class to OpenFilePickerEventSchema.serializer(),
            SaveFileEventSchema::class to SaveFileEventSchema.serializer(),
            TakePictureEventSchema::class to TakePictureEventSchema.serializer(),
            GetImageFromGalleryEventSchema::class to GetImageFromGalleryEventSchema.serializer(),
            ToggleMenuEventSchema::class to ToggleMenuEventSchema.serializer(),
            TogglePopupEventSchema::class to TogglePopupEventSchema.serializer(),
            NavigateEventSchema::class to NavigateEventSchema.serializer(),
            NavigateUpEventSchema::class to NavigateUpEventSchema.serializer(),
            NavigateClearingStackEventSchema::class to NavigateClearingStackEventSchema.serializer(),
            DownloadFileEventSchema::class to DownloadFileEventSchema.serializer(),
            DownloadFileToMemoryEventSchema::class to DownloadFileToMemoryEventSchema.serializer(),
            DownloadFileToDiskEventSchema::class to DownloadFileToDiskEventSchema.serializer(),
            UploadFileEventSchema::class to UploadFileEventSchema.serializer(),
            SendNetworkRequestEventSchema::class to SendNetworkRequestEventSchema.serializer(),
            SetIncomingDataToNetworkParamsHolderBodyEventSchema::class to SetIncomingDataToNetworkParamsHolderBodyEventSchema.serializer(),
            SetIncomingDataToNetworkParamsHolderUrlEventSchema::class to SetIncomingDataToNetworkParamsHolderUrlEventSchema.serializer(),
            SetIncomingDataToNetworkParamsHolderHeadersEventSchema::class to SetIncomingDataToNetworkParamsHolderHeadersEventSchema.serializer(),
            SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema::class to SetIncomingDataToNetworkParamsHolderQueryParametersEventSchema.serializer(),
            DismissBottomSheetEventSchema::class to DismissBottomSheetEventSchema.serializer(),
            DisplayBottomSheetEventSchema::class to DisplayBottomSheetEventSchema.serializer(),
            DismissModalBottomSheetEventSchema::class to DismissModalBottomSheetEventSchema.serializer(),
            DisplayModalBottomSheetEventSchema::class to DisplayModalBottomSheetEventSchema.serializer(),
            DismissDialogEventSchema::class to DismissDialogEventSchema.serializer(),
            DisplayDialogEventSchema::class to DisplayDialogEventSchema.serializer(),
            DismissNavigationDrawerEventSchema::class to DismissNavigationDrawerEventSchema.serializer(),
            DisplayNavigationDrawerEventSchema::class to DisplayNavigationDrawerEventSchema.serializer(),
            DisplaySnackbarEventSchema::class to DisplaySnackbarEventSchema.serializer(),
            DismissSnackbarEventSchema::class to DismissSnackbarEventSchema.serializer(),
            StopRefreshingEventSchema::class to StopRefreshingEventSchema.serializer(),
            ChangeScreenStateEventSchema::class to ChangeScreenStateEventSchema.serializer(),
            GetScreenEventSchema::class to GetScreenEventSchema.serializer(),
            RefreshScreenEventSchema::class to RefreshScreenEventSchema.serializer(),
            ScrollColumnTileEventSchema::class to ScrollColumnTileEventSchema.serializer(),
            ScrollPagerTileEventSchema::class to ScrollPagerTileEventSchema.serializer(),
            ScrollRowTileEventSchema::class to ScrollRowTileEventSchema.serializer(),
            RequestPermissionEventSchema::class to RequestPermissionEventSchema.serializer(),
            BroadcastToSystemEventSchema::class to BroadcastToSystemEventSchema.serializer(),
            CheckIfHasInternetConnectionEventSchema::class to CheckIfHasInternetConnectionEventSchema.serializer(),
            DropCachesEventSchema::class to DropCachesEventSchema.serializer(),
            OpenExternalLinkEventSchema::class to OpenExternalLinkEventSchema.serializer(),
            AddTilesEventSchema::class to AddTilesEventSchema.serializer(),
            CheckIfTileContainsChildrenEventSchema::class to CheckIfTileContainsChildrenEventSchema.serializer(),
            GetTileChildrenCountEventSchema::class to GetTileChildrenCountEventSchema.serializer(),
            RemoveTilesEventSchema::class to RemoveTilesEventSchema.serializer(),
            ReplaceTilesEventSchema::class to ReplaceTilesEventSchema.serializer(),
            UpdateTilesEventSchema::class to UpdateTilesEventSchema.serializer(),
            WipeTilesEventSchema::class to WipeTilesEventSchema.serializer(),
            StartCountdownTimerEventSchema::class to StartCountdownTimerEventSchema.serializer(),
            StartTimeLoopEventSchema::class to StartTimeLoopEventSchema.serializer(),
            ReloadLazyTilesEventSchema::class to ReloadLazyTilesEventSchema.serializer(),
            ResetThemeEventSchema::class to ResetThemeEventSchema.serializer(),
            SetThemeEventSchema::class to SetThemeEventSchema.serializer()
        )
}