import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalController } from '@ionic/angular';
import { MilitaryStatus, NoticePeriod } from '../../models/candidate.model';

@Component({
  selector: 'app-candidate-modal',
  templateUrl: './candidate-modal.component.html',
  styleUrls: ['./candidate-modal.component.scss']
})
export class CandidateModalComponent implements OnInit {
  candidateForm: FormGroup;
  hasNoticePeriod: boolean = false;

  constructor(
    private fb: FormBuilder,
    private modalCtrl: ModalController
  ) {
    this.candidateForm = this.fb.group({
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

    // Notice Period değişikliklerini dinle
    this.candidateForm.get('hasNoticePeriod')?.valueChanges.subscribe(hasNotice => {
      const noticePeriodGroup = this.candidateForm.get('noticePeriod');
      if (hasNotice) {
        noticePeriodGroup?.enable();
      } else {
        noticePeriodGroup?.disable();
      }
    });
  }

  ngOnInit() {}

  cancel() {
    return this.modalCtrl.dismiss(null, 'cancel');
  }

  save() {
    if (this.candidateForm.valid) {
      const formValue = this.candidateForm.value;
      const result = {
        ...formValue,
        noticePeriod: formValue.hasNoticePeriod ? formValue.noticePeriod : null
      };
      return this.modalCtrl.dismiss(result, 'save');
    }
    return this.modalCtrl.dismiss(null, 'invalid');
  }

  getDaysArray(): number[] {
    return Array.from({length: 32}, (_, i) => i);
  }
} 