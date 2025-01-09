import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IonicModule } from '@ionic/angular';
import { RouterModule } from '@angular/router';
import { DashboardPage } from './dashboard.page';

@NgModule({
  imports: [
    CommonModule,
    IonicModule,
    RouterModule.forChild([
      {
        path: '',
        component: DashboardPage
      }
    ])
  ],
  declarations: [DashboardPage]
})
export class DashboardPageModule {} 