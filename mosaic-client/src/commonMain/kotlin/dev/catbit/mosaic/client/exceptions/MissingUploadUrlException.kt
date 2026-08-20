package dev.catbit.mosaic.client.exceptions

/**
 * Thrown by `UploadFileEventRunner` when [dev.catbit.mosaic.core.data.schemas.event.events.networking.UploadFileEventSchema.url]
 * is `null` and `NetworkParametersHolder` has no staged URL to fall back to either — the case
 * `SetIncomingDataToNetworkParamsHolderUrl` was supposed to cover but either wasn't chained, or
 * staged a value some other network event already consumed first.
 */
class MissingUploadUrlException : Throwable(
    "No upload URL available: the schema url is null and no url was set in the NetworkParametersHolder"
)
