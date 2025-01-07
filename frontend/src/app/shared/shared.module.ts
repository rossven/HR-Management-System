import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { CandidateModalComponent } from '../components/candidate-modal/candidate-modal.component';

@NgModule({
  declarations: [
    CandidateModalComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule
  ],
  exports: [
    CandidateModalComponent
  ]
})
export class SharedModule { } 