import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBook, NewBook } from '../book.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBook for edit and NewBookFormGroupInput for create.
 */
type BookFormGroupInput = IBook | PartialWithRequiredKeyOf<NewBook>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBook | NewBook> = Omit<T, 'createdDate' | 'lastModifiedDate'> & {
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BookFormRawValue = FormValueOf<IBook>;

type NewBookFormRawValue = FormValueOf<NewBook>;

type BookFormDefaults = Pick<NewBook, 'id' | 'isVerified' | 'createdDate' | 'lastModifiedDate' | 'authors'>;

type BookFormGroupContent = {
  id: FormControl<BookFormRawValue['id'] | NewBook['id']>;
  title: FormControl<BookFormRawValue['title']>;
  titleSearch: FormControl<BookFormRawValue['titleSearch']>;
  isbn: FormControl<BookFormRawValue['isbn']>;
  publisher: FormControl<BookFormRawValue['publisher']>;
  publishedYear: FormControl<BookFormRawValue['publishedYear']>;
  isVerified: FormControl<BookFormRawValue['isVerified']>;
  verifyUrl: FormControl<BookFormRawValue['verifyUrl']>;
  viewAuthors: FormControl<BookFormRawValue['viewAuthors']>;
  frontImageLink: FormControl<BookFormRawValue['frontImageLink']>;
  backImageLink: FormControl<BookFormRawValue['backImageLink']>;
  pagesNumber: FormControl<BookFormRawValue['pagesNumber']>;
  language: FormControl<BookFormRawValue['language']>;
  description: FormControl<BookFormRawValue['description']>;
  createdBy: FormControl<BookFormRawValue['createdBy']>;
  createdDate: FormControl<BookFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BookFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BookFormRawValue['lastModifiedDate']>;
  authors: FormControl<BookFormRawValue['authors']>;
};

export type BookFormGroup = FormGroup<BookFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookFormService {
  createBookFormGroup(book: BookFormGroupInput = { id: null }): BookFormGroup {
    const bookRawValue = this.convertBookToBookRawValue({
      ...this.getFormDefaults(),
      ...book,
    });
    return new FormGroup<BookFormGroupContent>({
      id: new FormControl(
        { value: bookRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      title: new FormControl(bookRawValue.title, {
        validators: [Validators.required],
      }),
      titleSearch: new FormControl(bookRawValue.titleSearch),
      isbn: new FormControl(bookRawValue.isbn),
      publisher: new FormControl(bookRawValue.publisher),
      publishedYear: new FormControl(bookRawValue.publishedYear),
      isVerified: new FormControl(bookRawValue.isVerified),
      verifyUrl: new FormControl(bookRawValue.verifyUrl),
      viewAuthors: new FormControl(bookRawValue.viewAuthors),
      frontImageLink: new FormControl(bookRawValue.frontImageLink),
      backImageLink: new FormControl(bookRawValue.backImageLink),
      pagesNumber: new FormControl(bookRawValue.pagesNumber),
      language: new FormControl(bookRawValue.language),
      description: new FormControl(bookRawValue.description),
      createdBy: new FormControl(bookRawValue.createdBy),
      createdDate: new FormControl(bookRawValue.createdDate),
      lastModifiedBy: new FormControl(bookRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(bookRawValue.lastModifiedDate),
      authors: new FormControl(bookRawValue.authors ?? []),
    });
  }

  getBook(form: BookFormGroup): IBook | NewBook {
    return this.convertBookRawValueToBook(form.getRawValue() as BookFormRawValue | NewBookFormRawValue);
  }

  resetForm(form: BookFormGroup, book: BookFormGroupInput): void {
    const bookRawValue = this.convertBookToBookRawValue({ ...this.getFormDefaults(), ...book });
    form.reset(
      {
        ...bookRawValue,
        id: { value: bookRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      isVerified: false,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
      authors: [],
    };
  }

  private convertBookRawValueToBook(rawBook: BookFormRawValue | NewBookFormRawValue): IBook | NewBook {
    return {
      ...rawBook,
      createdDate: dayjs(rawBook.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBook.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBookToBookRawValue(
    book: IBook | (Partial<NewBook> & BookFormDefaults),
  ): BookFormRawValue | PartialWithRequiredKeyOf<NewBookFormRawValue> {
    return {
      ...book,
      createdDate: book.createdDate ? book.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: book.lastModifiedDate ? book.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
      authors: book.authors ?? [],
    };
  }
}
