export enum MilitaryStatus {
  COMPLETED = 'COMPLETED',
  EXEMPT = 'EXEMPT',
  POSTPONED = 'POSTPONED'
}

export interface NoticePeriod {
  days: number;
  months: number;
}

export interface Candidate {
  id?: number;
  firstName: string;
  lastName: string;
  position: string;
  militaryStatus: MilitaryStatus;
  noticePeriod?: NoticePeriod;
  phone: string;
  email: string;
} 