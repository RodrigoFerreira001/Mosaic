package dev.catbit.mosaic.client.exceptions

/**
 * Thrown by `MosaicNetworkImpl.downloadFileToDisk` in place of the original write error when the
 * failure was caused by the device running out of storage — the signal `DownloadFileToDiskEventRunner`
 * uses to fire `onInsufficientDiskSpace()` instead of `onDownloadFailure()`/`onFailure()`.
 *
 * @param cause the platform error that reported the lack of space.
 */
class InsufficientDiskSpaceException(cause: Throwable) : Throwable(
    "Not enough free disk space to complete the download: ${cause.message}",
    cause
)
