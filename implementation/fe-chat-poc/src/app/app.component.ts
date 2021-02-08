import { Component, Input, OnInit } from '@angular/core';
import { WebsocketService } from './services/websocket/websocket.service';
import { WsMessage } from './domain/wsMessage';
import { MessageType } from './domain/messageType';
import { Event } from '@angular/router';
import { Topic } from './domain/topic';
import { Observable, of, pipe, Subject } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { User } from './domain/user';
import { ChatMessage } from './domain/chatMessage';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'fe-chat-poc';

  topicName: string;
  userName: string;
  allTopics$: Observable<Topic[]>;
  currentChat$: Observable<Topic>;

  constructor(private webSocketService: WebsocketService) {}

  ngOnInit(): void {
    this.webSocketService.connect();
    this.allTopics$ = this.webSocketService.allTopics$;
  }

  updateTopicName(updatedValue: any): void {
    this.topicName = updatedValue.target.value;
  }

  updateUsername(updatedValue: any): void {
    this.userName = updatedValue.target.value;
  }

  sendMessage(text: any, topicId: string): void {
    const chat: ChatMessage = {
      sender: this.userName,
      message: text.target.value,
      timestamp: new Date(),
    };

    const message: WsMessage = {
      content: JSON.stringify(chat),
      messageType: MessageType.CHAT,
      timestamp: new Date(),
    };

    this.webSocketService.sendMessage(message, topicId);

    text.target.value = '';
    this.toBottom();
  }

  createTopic(): void {
    const topic: Topic = {
      id: this.topicName,
      joinedUsers: [],
      chatMessages: [],
    };

    const message: WsMessage = {
      content: JSON.stringify(topic),
      messageType: MessageType.INFO,
      timestamp: new Date(),
    };
    this.webSocketService.createTopic(message);
  }

  joinTopic(topicId: string): void {
    if (this.userName) {
      const user: User = {
        id: null,
        name: this.userName,
      };

      const message: WsMessage = {
        content: JSON.stringify(user),
        messageType: MessageType.INFO,
        timestamp: new Date(),
      };
      this.webSocketService.addUser(message, topicId);
      console.log('aaaaaaaaaaaaaaaaaaa');
      this.enableChat(this.webSocketService.topic$);
    } else {
      alert('Vul een gebruikersnaam in!');
    }
  }

  private enableChat(topic: Observable<Topic>): void {
    console.log('Enable chat');
    this.currentChat$ = topic;
    this.currentChat$.subscribe();
    console.log(this.currentChat$);
    this.toBottom();
  }

  toBottom(): void {
    const messageBody = document.querySelector('#chatmessages');
    messageBody.scrollTop = messageBody.scrollHeight - 10;
  }
}
