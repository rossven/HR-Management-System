import { Component, OnInit, Input, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalController } from '@ionic/angular';
import { MilitaryStatus } from '../../models/candidate.interface';
import { CandidateService } from '../../services/candidate.service';
import { LoadingController, ToastController } from '@ionic/angular';

@Component({
  selector: 'app-candidate-modal',
  templateUrl: './candidate-modal.component.html',
  styleUrls: ['./candidate-modal.component.scss']
})
export class CandidateModalComponent implements OnInit {
  @Input() candidate: any;
  candidateForm: FormGroup;
  hasNoticePeriod: boolean = false;
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private modalCtrl: ModalController,
    private candidateService: CandidateService,
    private loadingCtrl: LoadingController,
    private toastCtrl: ToastController
  ) {
    this.candidateForm = this.createForm();

    this.candidateForm.get('hasNoticePeriod')?.valueChanges.subscribe(hasNotice => {
      const noticePeriodGroup = this.candidateForm.get('noticePeriod');
      if (hasNotice) {
        noticePeriodGroup?.enable();
      } else {
        noticePeriodGroup?.disable();
        noticePeriodGroup?.patchValue({ months: 0, days: 0 });
      }
    });
  }

  ngOnInit() {
    if (this.candidate) {
      this.hasNoticePeriod = !!(this.candidate.noticePeriodMonths || this.candidate.noticePeriodDays);
      this.candidateForm.patchValue({
        firstName: this.candidate.firstName,
        lastName: this.candidate.lastName,
        position: this.candidate.position,
        militaryStatus: this.candidate.militaryStatus,
        hasNoticePeriod: this.hasNoticePeriod,
        noticePeriod: {
          months: this.candidate.noticePeriodMonths || 0,
          days: this.candidate.noticePeriodDays || 0
        },
        phone: this.candidate.phone,
        email: this.candidate.email
      });
    }
  }

  createForm(): FormGroup {
    return this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      position: ['', Validators.required],
      militaryStatus: ['', Validators.required],
      hasNoticePeriod: [false],
      noticePeriod: this.fb.group({
        months: [0],
        days: [0]
      }),
      phone: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  cancel() {
    return this.modalCtrl.dismiss(null, 'cancel');
  }

  onFileSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    }
  }

  async downloadCV() {
    if (!this.candidate?.id) return;

    try {
      const loading = await this.loadingCtrl.create({
        message: 'Downloading CV...'
      });
      await loading.present();

      const response = await this.candidateService.downloadCV(this.candidate.id).toPromise();
      if (!response) {
        throw new Error('No data received');
      }
      
      const url = window.URL.createObjectURL(response);
      const link = document.createElement('a');
      link.href = url;
      link.download = this.candidate.cvFileName || `candidate-${this.candidate.id}-cv.pdf`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
      loading.dismiss();
    } catch (error) {
      const toast = await this.toastCtrl.create({
        message: 'Error downloading CV',
        duration: 3000,
        color: 'danger'
      });
      toast.present();
    }
  }

  save() {
    if (!this.candidateForm.valid) {
      return this.modalCtrl.dismiss(null, 'invalid');
    }

    if (!this.candidate && !this.selectedFile) {
      this.toastCtrl.create({
        message: 'Please select a CV file',
        duration: 3000,
        color: 'danger'
      }).then(toast => toast.present());
      return;
    }

    const formValue = this.candidateForm.value;
    const candidateData = {
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      position: formValue.position,
      militaryStatus: formValue.militaryStatus,
      noticePeriodMonths: formValue.hasNoticePeriod ? formValue.noticePeriod.months : 0,
      noticePeriodDays: formValue.hasNoticePeriod ? formValue.noticePeriod.days : 0,
      phone: formValue.phone,
      email: formValue.email
    };

    if (this.candidate) {
      if (this.selectedFile) {
        return this.modalCtrl.dismiss({
          ...candidateData,
          cvFile: this.selectedFile
        }, 'save-with-cv');
      } else {
        return this.modalCtrl.dismiss(candidateData, 'save');
      }
    } else {
      if (!this.selectedFile) {
        return this.modalCtrl.dismiss(null, 'invalid');
      }
      return this.modalCtrl.dismiss({
        ...candidateData,
        cvFile: this.selectedFile
      }, 'save');
    }
  }

  getDaysArray(): number[] {
    return Array.from({length: 32}, (_, i) => i);
  }

  clearSelectedFile() {
    this.selectedFile = null;
    const fileInput = document.querySelector('input[type="file"]') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  @HostListener('dragover', ['$event'])
  onDragOver(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    const uploadArea = document.querySelector('.upload-area');
    if (uploadArea) {
      uploadArea.classList.add('dragover');
    }
  }

  @HostListener('dragleave', ['$event'])
  onDragLeave(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    const uploadArea = document.querySelector('.upload-area');
    if (uploadArea) {
      uploadArea.classList.remove('dragover');
    }
  }

  @HostListener('drop', ['$event'])
  onDrop(event: DragEvent) {
    event.preventDefault();
    event.stopPropagation();
    const uploadArea = document.querySelector('.upload-area');
    if (uploadArea) {
      uploadArea.classList.remove('dragover');
    }
    
    const files = event.dataTransfer?.files;
    if (files && files.length > 0) {
      const file = files[0];
      if (file.type === 'application/pdf') {
        this.selectedFile = file;
      } else {
        this.toastCtrl.create({
          message: 'Please upload only PDF files',
          duration: 3000,
          color: 'danger'
        }).then(toast => toast.present());
      }
    }
  }
} 