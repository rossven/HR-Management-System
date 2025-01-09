import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { IonicModule } from '@ionic/angular';
import { CandidateListPageRoutingModule } from './candidate-list-routing.module';
import { CandidateListPage } from './candidate-list.page';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    IonicModule,
    CandidateListPageRoutingModule,
    SharedModule
  ],
  declarations: [CandidateListPage]
})
export class CandidateListPageModule {} 