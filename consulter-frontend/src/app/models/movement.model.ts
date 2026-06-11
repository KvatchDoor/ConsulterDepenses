export type MovementType = 'DEBIT' | 'CREDIT';

export interface Movement {
  id: string;
  accountId: string;
  createdBy: string;
  categoryId?: string | null;
  type: MovementType;
  amount: number;
  description?: string | null;
  movementDate: string;
}

export interface CreateMovementRequest {
  accountId: string;
  createdBy: string;
  categoryId?: string;
  type: MovementType;
  amount: number;
  description?: string;
  movementDate: string;
}

export interface MovementGroup {
  date: string;
  dateLabel: string;
  movements: Movement[];
}
