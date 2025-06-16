import dayjs from 'dayjs/esm';
import { IBook } from 'app/entities/book/book.model';
import { BookState } from 'app/entities/enumerations/book-state.model';

export interface IBookSell {
  id: string;
  username?: string | null;
  date?: dayjs.Dayjs | null;
  country?: string | null;
  bookState?: keyof typeof BookState | null;
  quantity?: number | null;
  price?: number | null;
  note?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  book?: Pick<IBook, 'id' | 'title'> | null;
}

export type NewBookSell = Omit<IBookSell, 'id'> & { id: null };
