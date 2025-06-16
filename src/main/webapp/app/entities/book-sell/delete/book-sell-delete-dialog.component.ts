import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBookSell } from '../book-sell.model';
import { BookSellService } from '../service/book-sell.service';

@Component({
  templateUrl: './book-sell-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BookSellDeleteDialogComponent {
  bookSell?: IBookSell;

  protected bookSellService = inject(BookSellService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.bookSellService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
