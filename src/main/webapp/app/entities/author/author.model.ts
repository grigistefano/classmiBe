import dayjs from 'dayjs/esm';
import { IBook } from 'app/entities/book/book.model';

export interface IAuthor {
  id: string;
  name?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  books?: Pick<IBook, 'id' | 'title'>[] | null;
}

export type NewAuthor = Omit<IAuthor, 'id'> & { id: null };
