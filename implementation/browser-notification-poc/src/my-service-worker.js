importScripts('./ngsw-worker.js');

console.log("Service worker registered");

self.addEventListener("notificationclick", (event) => {
    console.log(event);
})