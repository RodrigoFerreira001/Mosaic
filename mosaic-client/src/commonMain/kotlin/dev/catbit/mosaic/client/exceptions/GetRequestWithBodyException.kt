package dev.catbit.mosaic.client.exceptions

/**
 * Thrown by `MosaicNetworkImpl` when an HTTP `GET` request would be sent with a non-null body — a
 * real validation Mosaic's own network layer performs that isn't documented anywhere on the DSL side
 * (`SendNetworkRequest`/`GetScreen`/`RefreshScreen` all accept a `body` regardless of `method`, and
 * this only surfaces at request time). Any event issuing a GET with a non-null `body` fails with this
 * as its `onFailure` cause.
 *
 * @param url the request URL that triggered the error, included in the message.
 */
class GetRequestWithBodyException(url: String) : Throwable(
    "GET request to '$url' was sent with a non-null body. GET requests must not carry a body — " +
        "use query parameters instead, or change the HTTP method to POST/PUT/PATCH."
)
