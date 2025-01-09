
# HR Management System API Documentation

---

## Overview

This API documentation serves as a reference for the backend services of the HR Management System. The API includes all endpoints necessary for candidate management.

---

## Base URL

`http://localhost:8080/api`

---

## Authentication

Currently, the API does not require authentication. The CORS configuration allows the following origins:

- `http://localhost:8100`
- `http://localhost:8101`

---

## Endpoints

### 1. Get All Candidates

Retrieve a list of all candidates in the system.

**Endpoint**: `GET /candidates`

#### Response

```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "position": "Software Engineer",
    "militaryStatus": "COMPLETED",
    "noticePeriodMonths": 1,
    "noticePeriodDays": 0,
    "phone": "5550123456",
    "email": "john.doe@email.com",
    "cvFileName": "abc123.pdf"
  }
]
```

---

### 2. Update Candidate

Update an existing candidate's information (excluding the CV).

**Endpoint**: `PUT /candidates/{id}`

#### Request Body

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "position": "Senior Software Engineer",
  "militaryStatus": "COMPLETED",
  "noticePeriodMonths": 2,
  "noticePeriodDays": 15,
  "phone": "5550123456",
  "email": "john.doe@email.com"
}
```

---

### 3. Update Candidate with CV

Update an existing candidate's information along with their CV.

**Endpoint**: `PUT /candidates/{id}/with-cv`

#### Request

- **Content-Type**: `multipart/form-data`
- **Parts**:
  - `data`: JSON string (candidate information)
  - `cv`: PDF file

#### Example Request Using Fetch

```javascript
const formData = new FormData();

formData.append('data', JSON.stringify({
  firstName: "John",
  lastName: "Doe",
  position: "Software Engineer",
  militaryStatus: "COMPLETED",
  noticePeriodMonths: 1,
  noticePeriodDays: 0,
  phone: "5550123456",
  email: "john.doe@email.com"
}));

formData.append('cv', cvFile); // PDF file

fetch('http://localhost:8080/api/candidates/1/with-cv', {
  method: 'PUT',
  body: formData
});
```

---

### 4. Delete Candidate

Delete a candidate and their associated CV file.

**Endpoint**: `DELETE /candidates/{id}`

---

### 5. Download CV

Download a candidate's CV.

**Endpoint**: `GET /candidates/{id}/cv`

#### Response

- **Content-Type**: `application/pdf`
- **Content-Disposition**: `attachment; filename="candidate-cv.pdf"`

---

## Models

### Candidate Model

```typescript
interface Candidate {
  id?: number;
  firstName: string;
  lastName: string;
  position: string;
  militaryStatus: MilitaryStatus;
  noticePeriodMonths?: number;
  noticePeriodDays?: number;
  phone: string;
  email: string;
  cvFileName?: string;
  cvFilePath?: string;
  cvContentType?: string;
  createdAt?: Date;
  updatedAt?: Date;
}

enum MilitaryStatus {
  COMPLETED = 'COMPLETED',
  EXEMPT = 'EXEMPT',
  POSTPONED = 'POSTPONED'
}
```

---

## Response Codes

| Status Code | Description        |
|-------------|--------------------|
| 200         | Success            |
| 400         | Invalid Request    |
| 404         | Resource Not Found |
| 500         | Server Error       |

---

## Constraints & Notes

### 1. File Upload

- Maximum file size: 10MB
- Allowed file types: PDF only
- Files are stored in the `./uploads/cvs` directory

### 2. Validations

- Email address must be unique
- Phone number must be 10 digits
- `firstName`, `lastName`, `position`, and `email` fields are required

### 3. Date Fields

- `createdAt` and `updatedAt` fields are automatically managed by the server
- All dates are returned in ISO 8601 format

---

## Frontend Usage Example

```typescript
// candidate.service.ts
export class CandidateService {
  private apiUrl = 'http://localhost:8080/api/candidates';

  constructor(private http: HttpClient) {}

  getAllCandidates(): Observable<Candidate[]> {
    return this.http.get<Candidate[]>(this.apiUrl);
  }

  updateCandidate(id: number, candidate: Candidate): Observable<Candidate> {
    return this.http.put<Candidate>(\`\${this.apiUrl}/\${id}\`, candidate);
  }

  updateCandidateWithCV(id: number, data: any, cvFile: File): Observable<Candidate> {
    const formData = new FormData();
    formData.append('data', JSON.stringify(data));
    formData.append('cv', cvFile);
    return this.http.put<Candidate>(\`\${this.apiUrl}/\${id}/with-cv\`, formData);
  }

  deleteCandidate(id: number): Observable<void> {
    return this.http.delete<void>(\`\${this.apiUrl}/\${id}\`);
  }

  downloadCV(id: number): Observable<Blob> {
    return this.http.get(\`\${this.apiUrl}/\${id}/cv\`, {
      responseType: 'blob'
    });
  }
}
```
