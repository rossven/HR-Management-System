import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CandidateService } from '../../services/candidate.service';
import { Candidate, MilitaryStatus } from '../../models/candidate.model';

@Component({
  selector: 'app-candidate-form',
  templateUrl: './candidate-form.page.html',
  styleUrls: ['./candidate-form.page.scss'],
})
export class CandidateFormPage implements OnInit {
  candidateForm: FormGroup = this.createForm();
  isEdit = false;
  candidateId = 0;

  constructor(
    private fb: FormBuilder,
    private candidateService: CandidateService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.candidateId = +id;
      this.isEdit = true;
      this.loadCandidate();
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      position: ['', Validators.required],
      militaryStatus: ['', Validators.required],
      noticePeriod: [''],
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  loadCandidate() {
    this.candidateService.getCandidate(this.candidateId).subscribe(candidate => {
      this.candidateForm.patchValue(candidate);
    });
  }

  onSubmit() {
    if (this.candidateForm.valid) {
      const candidateData = this.candidateForm.value;
      
      // Boş bir PDF dosyası oluştur (geçici çözüm)
      const emptyPdfBlob = new Blob([''], { type: 'application/pdf' });
      const emptyPdfFile = new File([emptyPdfBlob], 'empty.pdf', { type: 'application/pdf' });
      
      if (this.isEdit) {
        this.candidateService.updateCandidate(this.candidateId, candidateData)
          .subscribe(() => this.router.navigate(['/candidate-list']));
      } else {
        this.candidateService.createCandidate(candidateData, emptyPdfFile)
          .subscribe(
            (response) => {
              this.router.navigate(['/candidate-list']);
            },
            (error) => {
              // error handling
            }
          );
      }
    }
  }
} 