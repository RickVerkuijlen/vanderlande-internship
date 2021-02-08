import { Component, OnInit } from '@angular/core';
import { PushnotificationService } from './services/pushnotification/pushnotification.service';
import { SwPush } from '@angular/service-worker';
import { from, Observable } from 'rxjs';
import { map, share } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'browser-notification-poc';
  // permission: string;
  permissions$: Observable<string>;

  result: string;
  notification: string;
  priority: number;

  constructor(private swPush: SwPush, private notificationService: PushnotificationService) {}

  ngOnInit(): void {
    this.notificationService.requestPermission();

    // navigator.permissions.query({name: 'notifications'}) // Consider observable
    // .then(result => {
    //   this.permission = result.state === 'granted' ? 'granted' : 'blocked';
    // });

    this.permissions$ = from(navigator.permissions.query({name: 'notifications'})).pipe(
      map(result => result.state),
      share()
    );

    this.swPush.notificationClicks.subscribe(alert => { // Map to own model
      this.result = alert.action;
      this.notification = alert.notification.body;
      this.priority = alert.notification.data.priority;
    });
  }

  notify(): void {
    const data: Array<any> = []; // Create type for notifications

    data.push({
      title: 'VIBES UX',
      alertContent: 'The conveyer belt is stuck!',
      actions: [{
        action: 'acknowledge',
        title: 'Acknowledge alert'
      }, {
        action: 'close',
        title: 'Close alert'
      }],
      data: {
        priority: 5
      }
    },
    {
      title: 'VIBES UX',
      alertContent: 'Baggage is missing.',
      actions: [{
        action: 'acknowledge',
        title: 'Acknowledge alert'
      }, {
        action: 'close',
        title: 'Close alert'
      }],
      data: {
        priority: 5
      }
    });

    this.notificationService.generateNotification(data);
  }
}
