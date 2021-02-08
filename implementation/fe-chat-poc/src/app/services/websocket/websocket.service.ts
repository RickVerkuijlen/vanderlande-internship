import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import { WsMessage } from 'src/app/domain/wsMessage';
import { Topic } from 'src/app/domain/topic';
import { ReplaySubject, Subject } from 'rxjs';
import { MessageType } from 'src/app/domain/messageType';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {
  private stompClient: Stomp.Client;
  private allTopicsSubject = new ReplaySubject<Topic[]>(1);
  private currentTopic = new Subject<Topic>();
  public allTopics$ = this.allTopicsSubject.asObservable();
  public topic$ = this.currentTopic.asObservable();

  connect(): void {
    const ws = new SockJS(environment.webSocketURL);
    this.stompClient = Stomp.over(ws);

    // This construction makes sure that 'this' is known within the method.
    this.stompClient.connect(
      {},
      () => this.onJoined(),
      () => this.onError()
    );
  }

  private onJoined(): void {
    this.getAllTopics();
    this.stompClient.subscribe(
      '/topics',
      (res) => this.processTopics(res),
      () => this.onError()
    );
  }

  private onError(): void {
    console.log('error');
  }

  private processTopics(payload: any): void {
    const wsMessage: WsMessage = JSON.parse(payload.body);

    console.log('aaaaa');
    console.log(JSON.parse(wsMessage.content) as Topic[]);

    if (wsMessage.messageType !== MessageType.ERROR) {
      const topics = JSON.parse(wsMessage.content) as Topic[];

      this.allTopicsSubject.next(topics);
    } else {
      alert(wsMessage.content);
    }
  }

  sendMessage(message: WsMessage, topicId: string): void {
    this.stompClient.send(
      '/app/' + topicId + '/sendMessage',
      {},
      JSON.stringify(message)
    );
  }

  createTopic(topicName: WsMessage): void {
    this.stompClient.send('/app/createTopic', {}, JSON.stringify(topicName));
  }

  addUser(user: WsMessage, topicId: string): void {
    this.stompClient.subscribe(
      '/topics/' + topicId,
      (res) => this.processMessages(res),
      () => this.onError()
    );
    this.stompClient.send(
      '/app/' + topicId + '/addUser',
      {},
      JSON.stringify(user)
    );
    this.currentTopic.next();
  }

  private processMessages(payload: any): void {
    const wsMessage: WsMessage = JSON.parse(payload.body);
    if (
      wsMessage.messageType === MessageType.CHAT ||
      wsMessage.messageType === MessageType.INFO
    ) {
      const topic = JSON.parse(wsMessage.content) as Topic;
      this.currentTopic.next(topic);
    }
  }

  private getAllTopics(): void {
    this.stompClient.send('/app/allTopics');
  }
}
