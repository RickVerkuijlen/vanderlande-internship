import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { SwPush } from '@angular/service-worker';
import { from, Subject } from 'rxjs';
import { map, mapTo, shareReplay, switchMap, takeUntil, withLatestFrom } from 'rxjs/operators';
import { Subscription } from './domain/subscription';
import { NotificationService } from './service/notification/notification.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'fe-push-notification-poc';

  private subscribersSubject = new Subject();
  subscribers$ = this.subscribersSubject.asObservable();

  private subscribeSubject = new Subject();
  private unsubscribeSubject = new Subject();
  private destroySubject = new Subject();

  subscribeDisabled = false;
  unsubscribeDisabled = true;

  constructor(private swPush: SwPush, private notificationService: NotificationService) {}

  ngOnInit(): void {

    let publicKey: string;

    const publicSigningKeyContext$ = this.notificationService.getPublicSigningKey();

    publicSigningKeyContext$.pipe(takeUntil(this.destroySubject)).subscribe((result) => {
      publicKey = result;
    });

    const subscriptionContext$ = this.subscribeSubject.pipe(
      switchMap(() => from(this.swPush.requestSubscription({ serverPublicKey: publicKey }))),
      map((pushSubscription) => ({ pushSubscription, subscriptionData: pushSubscription.toJSON() })),
      shareReplay(1)
    );

    subscriptionContext$.pipe(takeUntil(this.destroySubject)).subscribe(({ subscriptionData }) => {
      this.notificationService.subscribe(this.convertToSubscription(subscriptionData));
      this.subscribers$ = this.notificationService.getAllSubscribers();
      this.subscribeDisabled = true;
      this.unsubscribeDisabled = false;
    });

    this.unsubscribeSubject
      .pipe(
        withLatestFrom(subscriptionContext$),
        switchMap(([_, context]) => from(context.pushSubscription.unsubscribe()).pipe(mapTo(context))),
        switchMap((context) => this.notificationService.unsubscribe(this.convertToSubscription(context.subscriptionData))
        .pipe(mapTo(true))),
        takeUntil(this.destroySubject)
      )
      .subscribe((success) => {
        this.subscribers$ = this.notificationService.getAllSubscribers();
        this.subscribeDisabled = false;
        this.unsubscribeDisabled = true;
      });

  }

  subscribeToNotifications(): void {
    console.log('Subscribing...');
    this.subscribeSubject.next();
  }

  unsubscribe(): void {
    this.unsubscribeSubject.next();
  }

  private convertToSubscription(data: PushSubscriptionJSON): Subscription {
    return {
      endpoint: data.endpoint,
      expirationTime: data.expirationTime,
      subscriptionKeys: data.keys,
    };
  }

  ngOnDestroy(): void {
    this.destroySubject.next();
    this.destroySubject.complete();
  }
}
