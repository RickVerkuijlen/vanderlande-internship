import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PushnotificationService {
  public permission: Permission;

  constructor() {
    this.permission = this.hasNotificationSupport() ? 'default' : 'denied';
   }

   private hasNotificationSupport(): boolean {
     return 'Notification' in window;
   }

   public requestPermission(): void {
     if (this.hasNotificationSupport()) {
       Notification.requestPermission().then((status) => { // Observable?
         this.permission = status;
       });
     }
   }

   public generateNotification(source: Array<any>): void { // Create type for notifications
    source.forEach((item) => {
      const options = {
        body: item.alertContent,
        actions: item.actions,
        icon: 'https://media-exp1.licdn.com/dms/image/C560BAQHwnLgnVhMJww/company-logo_100_100/0?e=1607558400&v=beta&t=Vpvo1PT97Vy56ZiFkYwKnOL21rYV8935TwmYZlMTWxQ',
        vibrate: [300, 100, 400],
        data: item.data
      };

      navigator.serviceWorker.getRegistration()
      .then(reg => {
        console.log(reg);
        reg.showNotification(item.title, options)
        .then(res => {
          console.log(res);
        })
        .catch(err => {
          console.error('Notification error: ', err);
        });
      })
      .catch(err => {
        console.error('Service worker error: ', err);
      });
    });
   }
}

export declare type Permission = 'denied' | 'granted' | 'default';
