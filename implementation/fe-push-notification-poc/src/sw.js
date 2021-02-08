importScripts('./ngsw-worker.js');

console.log('Service worker registered');

self.addEventListener('push', event => {
    return handlePushEvent(event, self.registration)
});

const handlePushEvent = (notification, swRegistration) => {
    const msg = notification.data.json();

    
    const options = {
        body: msg.body,
        silent: msg.silent,
        vibrate: msg.vibrate,
        actions: msg.actions
    }

    

    console.log(msg);

    return swRegistration.showNotification(msg.title, options);
}