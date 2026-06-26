package dev.catbit.mosaic.client.exceptions

class GetRequestWithBodyException(url: String) : Throwable(
    "GET request to '$url' was sent with a non-null body. GET requests must not carry a body — " +
        "use query parameters instead, or change the HTTP method to POST/PUT/PATCH."
)
