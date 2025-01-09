import { NgModule } from '@angular/core';
import { PreloadAllModules, RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'candidates',
    pathMatch: 'full'
  },
  {
    path: 'candidates',
    loadChildren: () => import('./pages/candidate-list/candidate-list.module').then(m => m.CandidateListPageModule)
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, { preloadingStrategy: PreloadAllModules })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { } 