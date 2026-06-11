export interface Account {
  id: string;
  ownerId: string;
  name: string;
  balance: number;
  currency: string;
}

export interface CreateAccountRequest {
  ownerId: string;
  name: string;
  currency: string;
}
