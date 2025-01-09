import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Candidate } from '../models/candidate.interface';
import { environment } from '../../environments/environment';
import { APP_CONSTANTS } from '../core/constants/app.constants';

interface CandidateCreateRequest {
  firstName: string;
  lastName: string;
  position: string;
  militaryStatus: string;
  noticePeriodMonths?: number;
  noticePeriodDays?: number;
  phone: string;
  email: string;
}

interface CandidateUpdateRequest extends CandidateCreateRequest {
  cvFile?: File;
}

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private apiUrl = `${environment.apiUrl}/candidates`;

  constructor(private http: HttpClient) {}

  getAllCandidates(): Observable<Candidate[]> {
    console.log('Fetching candidates...');
    return this.http.get<Candidate[]>(this.apiUrl)
      .pipe(
        tap(response => console.log('Candidates response:', response)),
        catchError(this.handleError)
      );
  }

  getCandidate(id: number): Observable<Candidate> {
    return this.http.get<Candidate>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  createCandidate(candidateData: CandidateCreateRequest, cvFile: File): Observable<Candidate> {
    const formData = new FormData();
    
    const candidateBlob = new Blob([JSON.stringify(candidateData)], {
        type: 'application/json'
    });
    
    formData.append('data', candidateBlob);
    formData.append('cv', cvFile);
    
    return this.http.post<Candidate>(this.apiUrl, formData)
        .pipe(
            catchError(error => {
                console.error('Error creating candidate:', error);
                return throwError(error);
            })
        );
  }

  updateCandidate(id: number, candidate: Candidate): Observable<Candidate> {
    return this.http.put<Candidate>(`${this.apiUrl}/${id}`, candidate)
      .pipe(catchError(this.handleError));
  }

  deleteCandidate(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  downloadCV(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${id}/cv`, {
      responseType: 'blob'
    }).pipe(catchError(this.handleError));
  }

  updateCandidateWithCV(id: number, candidateData: CandidateUpdateRequest, cvFile: File): Observable<Candidate> {
    const formData = new FormData();
    const { cvFile: _, ...candidateDataWithoutFile } = candidateData;
    
    formData.append('data', new Blob([JSON.stringify(candidateDataWithoutFile)], { type: 'application/json' }));
    formData.append('cv', cvFile);
    
    return this.http.put<Candidate>(`${this.apiUrl}/${id}/with-cv`, formData);
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'An error occurred';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = error.error.message;
    } else {
      if (error.status === 404) {
        errorMessage = 'Record not found';
      } else if (error.status === 400 && error.error) {
        errorMessage = Object.values(error.error).join('\n');
      }
    }
    
    return throwError(errorMessage);
  }
} 