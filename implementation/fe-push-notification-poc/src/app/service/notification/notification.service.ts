import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'src/app/domain/subscription';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  baseUrl: string;
  result: Subscription[];

  constructor(private http: HttpClient) {
    this.baseUrl = environment.base_url + 'subscribe';
  }

  subscribe(subscription: Subscription): void {
    const value = JSON.stringify(subscription);
    console.log('NotificationService: ', subscription);
    console.log(this.baseUrl);

    this.http
      .post(this.baseUrl, subscription)
      .pipe(tap(() => console.log(this.getAllSubscribers())))
      .subscribe();
  }

  unsubscribe(subscription: Subscription): Observable<unknown> {
    return this.http.delete(this.baseUrl + '/unsubscribe/' + subscription.subscriptionKeys.auth);
  }

  getPublicSigningKey(): Observable<string> {
    return this.http.get<string>(this.baseUrl + '/publicSigningKeyBase64');
  }

  getAllSubscribers(): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(this.baseUrl);
  }
}
