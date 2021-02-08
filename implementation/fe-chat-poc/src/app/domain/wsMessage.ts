import { MessageType } from './messageType';

export interface WsMessage {
    content: string;
    messageType: MessageType;
    timestamp: Date;
}
