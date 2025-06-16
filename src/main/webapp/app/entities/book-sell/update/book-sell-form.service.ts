import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBookSell, NewBookSell } from '../book-sell.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBookSell for edit and NewBookSellFormGroupInput for create.
 */
type BookSellFormGroupInput = IBookSell | PartialWithRequiredKeyOf<NewBookSell>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IBookSell | NewBookSell> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

type BookSellFormRawValue = FormValueOf<IBookSell>;

type NewBookSellFormRawValue = FormValueOf<NewBookSell>;

type BookSellFormDefaults = Pick<NewBookSell, 'id' | 'date' | 'createdDate' | 'lastModifiedDate'>;

type BookSellFormGroupContent = {
  id: FormControl<BookSellFormRawValue['id'] | NewBookSell['id']>;
  username: FormControl<BookSellFormRawValue['username']>;
  date: FormControl<BookSellFormRawValue['date']>;
  country: FormControl<BookSellFormRawValue['country']>;
  bookState: FormControl<BookSellFormRawValue['bookState']>;
  quantity: FormControl<BookSellFormRawValue['quantity']>;
  price: FormControl<BookSellFormRawValue['price']>;
  note: FormControl<BookSellFormRawValue['note']>;
  createdBy: FormControl<BookSellFormRawValue['createdBy']>;
  createdDate: FormControl<BookSellFormRawValue['createdDate']>;
  lastModifiedBy: FormControl<BookSellFormRawValue['lastModifiedBy']>;
  lastModifiedDate: FormControl<BookSellFormRawValue['lastModifiedDate']>;
  book: FormControl<BookSellFormRawValue['book']>;
};

export type BookSellFormGroup = FormGroup<BookSellFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BookSellFormService {
  createBookSellFormGroup(bookSell: BookSellFormGroupInput = { id: null }): BookSellFormGroup {
    const bookSellRawValue = this.convertBookSellToBookSellRawValue({
      ...this.getFormDefaults(),
      ...bookSell,
    });
    return new FormGroup<BookSellFormGroupContent>({
      id: new FormControl(
        { value: bookSellRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      username: new FormControl(bookSellRawValue.username, {
        validators: [Validators.required],
      }),
      date: new FormControl(bookSellRawValue.date),
      country: new FormControl(bookSellRawValue.country, {
        validators: [Validators.required],
      }),
      bookState: new FormControl(bookSellRawValue.bookState),
      quantity: new FormControl(bookSellRawValue.quantity),
      price: new FormControl(bookSellRawValue.price),
      note: new FormControl(bookSellRawValue.note),
      createdBy: new FormControl(bookSellRawValue.createdBy),
      createdDate: new FormControl(bookSellRawValue.createdDate),
      lastModifiedBy: new FormControl(bookSellRawValue.lastModifiedBy),
      lastModifiedDate: new FormControl(bookSellRawValue.lastModifiedDate),
      book: new FormControl(bookSellRawValue.book),
    });
  }

  getBookSell(form: BookSellFormGroup): IBookSell | NewBookSell {
    return this.convertBookSellRawValueToBookSell(form.getRawValue() as BookSellFormRawValue | NewBookSellFormRawValue);
  }

  resetForm(form: BookSellFormGroup, bookSell: BookSellFormGroupInput): void {
    const bookSellRawValue = this.convertBookSellToBookSellRawValue({ ...this.getFormDefaults(), ...bookSell });
    form.reset(
      {
        ...bookSellRawValue,
        id: { value: bookSellRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BookSellFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      createdDate: currentTime,
      lastModifiedDate: currentTime,
    };
  }

  private convertBookSellRawValueToBookSell(rawBookSell: BookSellFormRawValue | NewBookSellFormRawValue): IBookSell | NewBookSell {
    return {
      ...rawBookSell,
      date: dayjs(rawBookSell.date, DATE_TIME_FORMAT),
      createdDate: dayjs(rawBookSell.createdDate, DATE_TIME_FORMAT),
      lastModifiedDate: dayjs(rawBookSell.lastModifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertBookSellToBookSellRawValue(
    bookSell: IBookSell | (Partial<NewBookSell> & BookSellFormDefaults),
  ): BookSellFormRawValue | PartialWithRequiredKeyOf<NewBookSellFormRawValue> {
    return {
      ...bookSell,
      date: bookSell.date ? bookSell.date.format(DATE_TIME_FORMAT) : undefined,
      createdDate: bookSell.createdDate ? bookSell.createdDate.format(DATE_TIME_FORMAT) : undefined,
      lastModifiedDate: bookSell.lastModifiedDate ? bookSell.lastModifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
