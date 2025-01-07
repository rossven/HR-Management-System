import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Candidate } from '../models/candidate.interface';

@Injectable({
  providedIn: 'root'
})
export class CandidateService {
  private apiUrl = 'http://localhost:8080/api/candidates';

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

  createCandidate(candidate: Candidate): Observable<Candidate> {
    return this.http.post<Candidate>(this.apiUrl, candidate)
      .pipe(catchError(this.handleError));
  }

  updateCandidate(id: number, candidate: Candidate): Observable<Candidate> {
    return this.http.put<Candidate>(`${this.apiUrl}/${id}`, candidate)
      .pipe(catchError(this.handleError));
  }

  deleteCandidate(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Bir hata oluştu';
    
    if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = error.error.message;
    } else {
      // Server-side error
      if (error.status === 404) {
        errorMessage = 'Kayıt bulunamadı';
      } else if (error.status === 400 && error.error) {
        errorMessage = Object.values(error.error).join('\n');
      }
    }
    
    return throwError(errorMessage);
  }
} 