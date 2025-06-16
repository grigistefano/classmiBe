import dayjs from 'dayjs/esm';
import { IAuthor } from 'app/entities/author/author.model';

export interface IBook {
  id: string;
  title?: string | null;
  titleSearch?: string | null;
  isbn?: string | null;
  publisher?: string | null;
  publishedYear?: number | null;
  isVerified?: boolean | null;
  verifyUrl?: string | null;
  viewAuthors?: string | null;
  frontImageLink?: string | null;
  backImageLink?: string | null;
  pagesNumber?: number | null;
  language?: string | null;
  description?: string | null;
  createdBy?: string | null;
  createdDate?: dayjs.Dayjs | null;
  lastModifiedBy?: string | null;
  lastModifiedDate?: dayjs.Dayjs | null;
  authors?: Pick<IAuthor, 'id' | 'name'>[] | null;
}

export type NewBook = Omit<IBook, 'id'> & { id: null };
