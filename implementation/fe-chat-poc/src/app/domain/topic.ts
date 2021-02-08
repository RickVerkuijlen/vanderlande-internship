import { ChatMessage } from './chatMessage';
import { User } from './user';

export interface Topic {
    id: string;
    joinedUsers: User[];
    chatMessages: ChatMessage[];
}
