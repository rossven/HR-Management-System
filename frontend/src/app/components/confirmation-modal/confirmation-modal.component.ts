import { Component, Input } from '@angular/core';
import { ModalController } from '@ionic/angular';

@Component({
  selector: 'app-confirmation-modal',
  template: `
    <div class="confirm-modal">
      <p>{{ message }}</p>
      <ion-button fill="clear" color="medium" (click)="cancel()">Cancel</ion-button>
      <ion-button fill="clear" color="danger" (click)="confirm()">Delete</ion-button>
    </div>
  `,
  styles: [`
    .confirm-modal {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 16px;
    }
    p {
      margin-bottom: 16px;
      color: var(--ion-color-medium);
    }
  `]
})
export class ConfirmationModalComponent {
  @Input() message: string = 'Are you sure you want to delete this candidate?';

  constructor(private modalCtrl: ModalController) {}

  cancel() {
    return this.modalCtrl.dismiss(null, 'cancel');
  }

  confirm() {
    return this.modalCtrl.dismiss(null, 'confirm');
  }
}