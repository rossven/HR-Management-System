import { Component, OnInit } from '@angular/core';
import { CandidateService } from '../../services/candidate.service';
import { Candidate, MilitaryStatus } from '../../models/candidate.interface';
import { LoadingController, ToastController, ModalController } from '@ionic/angular';
import { CandidateModalComponent } from '../../components/candidate-modal/candidate-modal.component';

@Component({
  selector: 'app-candidate-list',
  templateUrl: './candidate-list.page.html',
  styleUrls: ['./candidate-list.page.scss'],
})
export class CandidateListPage implements OnInit {
  candidates: Candidate[] = [];
  filteredCandidates: Candidate[] = [];
  isLoading = false;
  showFilters = false;
  militaryStatus = MilitaryStatus;

  filters = {
    position: '',
    militaryStatus: '',
    noticePeriod: {
      months: -1,
      days: -1
    }
  };

  constructor(
    private candidateService: CandidateService,
    private loadingCtrl: LoadingController,
    private toastCtrl: ToastController,
    private modalCtrl: ModalController
  ) {}

  ngOnInit() {
    this.loadCandidates();
  }

  toggleFilters() {
    this.showFilters = !this.showFilters;
    if (this.showFilters) {
      document.body.style.overflow = 'hidden';
    } else {
      document.body.style.overflow = '';
    }
  }

  async loadCandidates() {
    const loading = await this.loadingCtrl.create({
      message: 'Yükleniyor...'
    });
    await loading.present();
    this.isLoading = true;

    this.candidateService.getAllCandidates().subscribe(
      (data) => {
        console.log('Received candidates:', data);
        this.candidates = data;
        this.applyFilters();
        loading.dismiss();
        this.isLoading = false;
      },
      async (error) => {
        console.error('Error loading candidates:', error);
        loading.dismiss();
        this.isLoading = false;
        
        const toast = await this.toastCtrl.create({
          message: typeof error === 'string' ? error : 'Veriler yüklenirken bir hata oluştu',
          duration: 3000,
          color: 'danger'
        });
        toast.present();
      }
    );
  }

  applyFilters() {
    this.filteredCandidates = this.candidates.filter(candidate => {
      const positionMatch = !this.filters.position || 
                          candidate.position.toLowerCase().includes(this.filters.position.toLowerCase());
      
      const militaryMatch = !this.filters.militaryStatus || 
                          candidate.militaryStatus === this.filters.militaryStatus;
      
      const noticePeriodMatch = this.filters.noticePeriod.months === -1 || 
                               (candidate.noticePeriodMonths === this.filters.noticePeriod.months &&
                                candidate.noticePeriodDays === this.filters.noticePeriod.days);

      return positionMatch && militaryMatch && noticePeriodMatch;
    });
  }

  async deleteCandidate(id: number | undefined) {
    if (id === undefined) {
      const toast = await this.toastCtrl.create({
        message: 'Geçersiz kayıt ID\'si',
        duration: 3000,
        color: 'danger'
      });
      toast.present();
      return;
    }

    const loading = await this.loadingCtrl.create({
      message: 'Siliniyor...'
    });
    await loading.present();

    this.candidateService.deleteCandidate(id).subscribe(
      () => {
        loading.dismiss();
        this.loadCandidates();
      },
      async (error) => {
        loading.dismiss();
        const toast = await this.toastCtrl.create({
          message: error,
          duration: 3000,
          color: 'danger'
        });
        toast.present();
      }
    );
  }

  async openNewCandidateModal() {
    const modal = await this.modalCtrl.create({
      component: CandidateModalComponent,
      cssClass: 'candidate-modal'
    });

    await modal.present();

    const { data, role } = await modal.onWillDismiss();

    if (role === 'save' && data) {
      this.candidateService.createCandidate(data).subscribe(() => {
        this.loadCandidates();
      });
    }
  }

  getDaysArray(): number[] {
    return Array.from({length: 32}, (_, i) => i);
  }
} 