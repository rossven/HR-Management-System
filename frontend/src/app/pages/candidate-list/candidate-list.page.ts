import { Component, OnInit } from '@angular/core';
import { CandidateService } from '../../services/candidate.service';
import { Candidate, MilitaryStatus } from '../../models/candidate.interface';
import { LoadingController, ToastController, ModalController } from '@ionic/angular';
import { CandidateModalComponent } from '../../components/candidate-modal/candidate-modal.component';
import { ConfirmationModalComponent } from '../../components/confirmation-modal/confirmation-modal.component';

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
    name: '',
    position: '',
    militaryStatus: '',
    noticePeriod: {
      months: -1,
      days: -1
    },
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
      message: 'Loading...'
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
          message: typeof error === 'string' ? error : 'Error loading data',
          duration: 3000,
          color: 'danger'
        });
        toast.present();
      }
    );
  }

  applyFilters() {
    const months = this.filters.noticePeriod.months >= 0 ? this.filters.noticePeriod.months : 0;
    const days = this.filters.noticePeriod.days >= 0 ? this.filters.noticePeriod.days : 0;

    const totalNoticePeriodDays = (months * 30) + days;

    this.filteredCandidates = this.candidates.filter(candidate => {

      const nameMatch = !this.filters.name || 
                          (candidate.firstName + ' ' + candidate.lastName).toLowerCase().includes(this.filters.name.toLowerCase());

      const positionMatch = !this.filters.position || 
                          candidate.position.toLowerCase().includes(this.filters.position.toLowerCase());
      
      const militaryMatch = !this.filters.militaryStatus || 
                          candidate.militaryStatus === this.filters.militaryStatus;

      const candidateNoticePeriodMonths = candidate.noticePeriodMonths || 0;
      const candidateNoticePeriodDays = candidate.noticePeriodDays || 0;

      const noticePeriodMatch = totalNoticePeriodDays === 0 || 
                               (candidateNoticePeriodMonths * 30 + candidateNoticePeriodDays <= totalNoticePeriodDays);

      return nameMatch && positionMatch && militaryMatch && noticePeriodMatch;
    });
  }

  async presentConfirmationModal(candidateId: number) {
    const modal = await this.modalCtrl.create({
      component: ConfirmationModalComponent,
      cssClass: 'small-modal',
      backdropDismiss: true,
      componentProps: {
        message: 'Are you sure you want to delete this candidate?',
      },
    });

    modal.onDidDismiss().then((data) => {
      if (data.role === 'confirm') {
        this.confirmDelete(candidateId);
      } else {
        this.cancelDelete();
      }
    });

    return await modal.present();
  }

  confirmDelete(candidateId: number) {
    this.candidateService.deleteCandidate(candidateId).subscribe(
      () => {
        console.log('Candidate deleted successfully');
        this.loadCandidates();
      },
      (error) => {
        console.error('Error deleting candidate:', error);
        this.toastCtrl.create({
          message: 'Error deleting candidate',
          duration: 3000,
          color: 'danger'
        }).then(toast => toast.present());
      }
    );
  }

  cancelDelete() {
    console.log('Delete action cancelled');
  }

  async openNewCandidateModal() {
    const modal = await this.modalCtrl.create({
      component: CandidateModalComponent,
      cssClass: 'candidate-modal'
    });

    await modal.present();

    const { data, role } = await modal.onWillDismiss();

    if (role === 'save' && data) {
      console.log('Creating candidate with data:', data);
      
      this.candidateService.createCandidate(data, data.cvFile).subscribe(
        (response) => {
          console.log('Candidate created successfully:', response);
          this.loadCandidates();
        },
        (error) => {
          console.error('Error creating candidate:', error);
        }
      );
    }
  }

  getDaysArray(): number[] {
    return Array.from({length: 32}, (_, i) => i);
  }

  async downloadCV(id: number) {
    try {
      const loading = await this.loadingCtrl.create({
        message: 'Downloading CV...'
      });
      await loading.present();

      const response = await this.candidateService.downloadCV(id).toPromise();
      if (!response) {
        throw new Error('No data received');
      }
      
      const url = window.URL.createObjectURL(response);
      const link = document.createElement('a');
      link.href = url;
      link.download = `candidate-${id}-cv.pdf`;
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

  async openCandidateModal(candidate?: Candidate) {
    const modal = await this.modalCtrl.create({
      component: CandidateModalComponent,
      cssClass: 'candidate-modal',
      componentProps: {
        candidate: candidate
      }
    });

    await modal.present();

    const { data, role } = await modal.onWillDismiss();

    if (data && (role === 'save' || role === 'save-with-cv')) {
      if (candidate?.id) {
        if (role === 'save-with-cv') {
          this.candidateService.updateCandidateWithCV(candidate.id, data, data.cvFile).subscribe(
            (response) => {
              console.log('Candidate updated with CV successfully:', response);
              this.loadCandidates();
            },
            (error) => {
              console.error('Error updating candidate with CV:', error);
            }
          );
        } else {
          this.candidateService.updateCandidate(candidate.id, data).subscribe(
            (response) => {
              console.log('Candidate updated successfully:', response);
              this.loadCandidates();
            },
            (error) => {
              console.error('Error updating candidate:', error);
            }
          );
        }
      } else {
        this.candidateService.createCandidate(data, data.cvFile).subscribe(
          (response) => {
            console.log('Candidate created successfully:', response);
            this.loadCandidates();
          },
          (error) => {
            console.error('Error creating candidate:', error);
          }
        );
      }
    }
  }
} 