// Cross-Origin Isolation service worker.
//
// Room's wasmJs SQLite driver needs OPFS (sqlite3.oo1.OpfsDb), which in turn needs
// SharedArrayBuffer/Atomics, which browsers only expose on a cross-origin-isolated page — one
// served with Cross-Origin-Opener-Policy: same-origin and Cross-Origin-Embedder-Policy:
// require-corp response headers. GitHub Pages (and most static hosts) can't set custom response
// headers, so this worker intercepts every same-origin fetch this page makes and re-wraps the
// response with those two headers added, achieving the same effect client-side.
self.addEventListener('install', () => self.skipWaiting());
self.addEventListener('activate', (event) => event.waitUntil(self.clients.claim()));

self.addEventListener('fetch', (event) => {
    const request = event.request;
    if (request.cache === 'only-if-cached' && request.mode !== 'same-origin') return;

    event.respondWith(
        fetch(request).then((response) => {
            if (response.status === 0) return response; // opaque cross-origin response, leave as-is
            const headers = new Headers(response.headers);
            headers.set('Cross-Origin-Opener-Policy', 'same-origin');
            headers.set('Cross-Origin-Embedder-Policy', 'require-corp');
            return new Response(response.body, {
                status: response.status,
                statusText: response.statusText,
                headers,
            });
        })
    );
});
