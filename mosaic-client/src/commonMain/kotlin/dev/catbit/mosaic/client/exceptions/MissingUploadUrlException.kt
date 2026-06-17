package dev.catbit.mosaic.client.exceptions

class MissingUploadUrlException : Throwable(
    "No upload URL available: the schema url is null and no url was set in the NetworkParametersHolder"
)
