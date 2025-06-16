import dayjs from 'dayjs/esm';

import { IBook, NewBook } from './book.model';

export const sampleWithRequiredData: IBook = {
  id: '167c8759-1da4-48b9-baa4-93bbe88d69fb',
  title: 'for as',
};

export const sampleWithPartialData: IBook = {
  id: '85602cc3-eb77-4062-a684-44dd44d5e58b',
  title: 'snuggle humidity near',
  verifyUrl: 'angelic waterlogged recount',
  viewAuthors: 'deduce dress especially',
  pagesNumber: 11295,
  createdBy: 'pish before',
  createdDate: dayjs('2025-06-16T16:41'),
  lastModifiedDate: dayjs('2025-06-15T20:41'),
};

export const sampleWithFullData: IBook = {
  id: '42683458-d94a-4a50-a94c-daac2ab10762',
  title: 'repeatedly instead part',
  titleSearch: 'shipper',
  isbn: 'impact blacken',
  publisher: 'politely icy calculating',
  publishedYear: 18091,
  isVerified: false,
  verifyUrl: 'anti dimly',
  viewAuthors: 'marten defiantly',
  frontImageLink: 'conceal atop',
  backImageLink: 'adviser',
  pagesNumber: 27636,
  language: 'clamor suffocate',
  description: 'amount bravely',
  createdBy: 'early',
  createdDate: dayjs('2025-06-16T10:11'),
  lastModifiedBy: 'or',
  lastModifiedDate: dayjs('2025-06-16T02:49'),
};

export const sampleWithNewData: NewBook = {
  title: 'taxicab nor',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
