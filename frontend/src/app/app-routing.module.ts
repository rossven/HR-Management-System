import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'candidate-list',
    pathMatch: 'full'
  },
  {
    path: 'candidate-list',
    loadChildren: () => import('./pages/candidate-list/candidate-list.module').then(m => m.CandidateListPageModule)
  },
  {
    path: 'candidate-form',
    loadChildren: () => import('./pages/candidate-form/candidate-form.module').then(m => m.CandidateFormPageModule)
  },
  {
    path: 'candidate-form/:id',
    loadChildren: () => import('./pages/candidate-form/candidate-form.module').then(m => m.CandidateFormPageModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { } 