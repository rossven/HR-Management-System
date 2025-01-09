export enum MilitaryStatus {
  COMPLETED = 'COMPLETED',
  EXEMPT = 'EXEMPT',
  POSTPONED = 'POSTPONED'
}

export interface Candidate {
  id?: number;
  firstName: string;
  lastName: string;
  position: string;
  militaryStatus: MilitaryStatus;
  noticePeriodMonths?: number;
  noticePeriodDays?: number;
  phone: string;
  email: string;
  createdAt?: string;
  updatedAt?: string;
  cvFileName?: string;
  cvFilePath?: string;
  cvContentType?: string;
} 