package dev.catbit.mosaic.client.exceptions

/**
 * Thrown when a directory is looked up in the client's own file storage and doesn't exist — the
 * `onFailure()` cause of `ListFiles` for an unknown `directory`.
 *
 * @param directory the requested directory, included in the message.
 */
class DirNotFoundException(directory: String) : Throwable(
    "Directory '$directory' not found in the client's file storage."
)
