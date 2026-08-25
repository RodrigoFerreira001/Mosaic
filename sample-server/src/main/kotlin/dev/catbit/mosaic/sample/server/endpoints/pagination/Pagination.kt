package dev.catbit.mosaic.sample.server.endpoints.pagination

import dev.catbit.mosaic.core.data.schemas.event.trigger.EventTriggers
import dev.catbit.mosaic.sample.server.BuildConfig
import dev.catbit.mosaic.server.builder.event.EventList
import dev.catbit.mosaic.server.builder.event.builders.event.UpdateEvents
import dev.catbit.mosaic.server.builder.event.builders.tiles.AddTiles
import dev.catbit.mosaic.server.builder.event.builders.tiles.RemoveTiles
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Card
import dev.catbit.mosaic.server.builder.tile.builders.grouping.Row
import dev.catbit.mosaic.server.builder.tile.builders.text.SimpleText
import io.ktor.http.CacheControl
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.cacheControl
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.util.getOrFail
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

private const val PAGINATION_SAMPLE_LIST_ID = "PAGINATION_SAMPLE_LIST"
private const val PAGINATION_EVENT_ID = "PAGINATION_EVENT"
private const val ITEMS_PER_PAGE = 20

fun paginationUrl(mode: String, page: Int) = "${BuildConfig.BASE_URL}/pagination?page=$page&mode=$mode"

// "recover" needs page 2's first attempt to fail but a retry of that same page (the url is only
// advanced by a successful response, so a retry re-requests page 2 again) to succeed — this counts
// attempts per (mode, page) to tell those two requests apart.
private val attemptCounts = ConcurrentHashMap<Pair<String, Int>, Int>()

/**
 * Backs the PaginationSample showroom screen: one endpoint that decides, per `(mode, page)`,
 * whether a page succeeds (200 + an `EventList` swapping the loading placeholder for the next
 * batch of items, then rewriting `PAGINATION_EVENT`'s own url to fetch the page after this one) or
 * fails (a plain server error, so the client's `SendNetworkRequest` falls to its `onFailure`
 * branch — the exact loading-placeholder-for-error-tile swap that used to permanently stall
 * `Int.ThresholdReachedEffect`'s re-pagination guard).
 *
 * `mode`:
 * - `always_fail` — every page fails, including the first.
 * - `fail_second` — page 1 succeeds, every page from 2 onward fails.
 * - `recover` — page 1 succeeds, page 2's first attempt fails and every retry of it succeeds
 *   (page 2 never advances the url on failure, so a retry re-requests page 2 itself), page 3
 *   onward always succeeds — the scenario that exercises the `ThresholdReachedEffect` fix.
 * - anything else (`success`) — every page succeeds.
 */
fun Route.pagination() {
    get("pagination") {
        call.response.cacheControl(CacheControl.NoStore(visibility = null))

        val page = call.request.queryParameters.getOrFail<Int>("page")
        val mode = call.request.queryParameters["mode"] ?: "success"

        if (page == 1) {
            attemptCounts.keys.removeAll { (keyMode, _) -> keyMode == mode }
        }

        delay(1.seconds)

        val attempt = attemptCounts.merge(mode to page, 1, Int::plus) ?: 1
        val shouldFail = when (mode) {
            "always_fail" -> true
            "fail_second" -> page >= 2
            "recover" -> page == 2 && attempt == 1
            else -> false
        }

        if (shouldFail) {
            call.respond(HttpStatusCode.InternalServerError)
            return@get
        }

        call.respond(
            EventList {
                RemoveTiles(
                    trigger = EventTriggers.inline(),
                    groupingTileId = PAGINATION_SAMPLE_LIST_ID,
                    tileIds = listOf("LOADING")
                )
                AddTiles(
                    trigger = EventTriggers.inline(),
                    groupingTileId = PAGINATION_SAMPLE_LIST_ID
                ) {
                    val firstIndex = (page - 1) * ITEMS_PER_PAGE
                    for (index in firstIndex until firstIndex + ITEMS_PER_PAGE) {
                        Card(
                            style = { margin(top = 8) }
                        ) {
                            Row(
                                style = { padding(horizontal = 16, vertical = 8) }
                            ) {
                                SimpleText("Item $index (page $page, mode $mode)")
                            }
                        }
                    }
                }
                UpdateEvents(
                    trigger = EventTriggers.inline(),
                    updates = {
                        update(
                            eventId = PAGINATION_EVENT_ID,
                            data = mapOf("url" to paginationUrl(mode = mode, page = page + 1))
                        )
                    }
                )
            }
        )
    }
}
