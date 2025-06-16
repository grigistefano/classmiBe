import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { BookState } from 'app/entities/enumerations/book-state.model';
import { BookSellService } from '../service/book-sell.service';
import { IBookSell } from '../book-sell.model';
import { BookSellFormGroup, BookSellFormService } from './book-sell-form.service';

@Component({
  selector: 'jhi-book-sell-update',
  templateUrl: './book-sell-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BookSellUpdateComponent implements OnInit {
  isSaving = false;
  bookSell: IBookSell | null = null;
  bookStateValues = Object.keys(BookState);

  booksSharedCollection: IBook[] = [];

  protected dataUtils = inject(DataUtils);
  protected eventManager = inject(EventManager);
  protected bookSellService = inject(BookSellService);
  protected bookSellFormService = inject(BookSellFormService);
  protected bookService = inject(BookService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BookSellFormGroup = this.bookSellFormService.createBookSellFormGroup();

  compareBook = (o1: IBook | null, o2: IBook | null): boolean => this.bookService.compareBook(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bookSell }) => {
      this.bookSell = bookSell;
      if (bookSell) {
        this.updateForm(bookSell);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('classmiBeApp.error', { ...err, key: `error.file.${err.key}` })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bookSell = this.bookSellFormService.getBookSell(this.editForm);
    if (bookSell.id !== null) {
      this.subscribeToSaveResponse(this.bookSellService.update(bookSell));
    } else {
      this.subscribeToSaveResponse(this.bookSellService.create(bookSell));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBookSell>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(bookSell: IBookSell): void {
    this.bookSell = bookSell;
    this.bookSellFormService.resetForm(this.editForm, bookSell);

    this.booksSharedCollection = this.bookService.addBookToCollectionIfMissing<IBook>(this.booksSharedCollection, bookSell.book);
  }

  protected loadRelationshipsOptions(): void {
    this.bookService
      .query()
      .pipe(map((res: HttpResponse<IBook[]>) => res.body ?? []))
      .pipe(map((books: IBook[]) => this.bookService.addBookToCollectionIfMissing<IBook>(books, this.bookSell?.book)))
      .subscribe((books: IBook[]) => (this.booksSharedCollection = books));
  }
}
