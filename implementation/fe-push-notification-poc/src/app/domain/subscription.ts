export interface Subscription {
  endpoint: string;
  expirationTime: number;
  subscriptionKeys: Record<string, string>;
}
