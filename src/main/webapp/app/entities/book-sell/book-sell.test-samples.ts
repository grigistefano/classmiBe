import dayjs from 'dayjs/esm';

import { IBookSell, NewBookSell } from './book-sell.model';

export const sampleWithRequiredData: IBookSell = {
  id: 'e3b9fb8f-cdc0-41d4-97ac-830a74b175c0',
  username: 'towards restaurant utterly',
  country: 'Guinea-Bissau',
};

export const sampleWithPartialData: IBookSell = {
  id: '41159295-d153-4c3e-8624-4f82de7e6311',
  username: 'including',
  date: dayjs('2025-06-16T18:41'),
  country: 'Venezuela',
  bookState: 'PESSIMO',
  lastModifiedBy: 'highly',
  lastModifiedDate: dayjs('2025-06-16T06:15'),
};

export const sampleWithFullData: IBookSell = {
  id: '0bd2e47c-7a2e-47d9-85ff-a38ff8dccb84',
  username: 'subexpression youthfully phew',
  date: dayjs('2025-06-16T19:16'),
  country: 'Australia',
  bookState: 'PESSIMO',
  quantity: 4324,
  price: 15232.75,
  note: '../fake-data/blob/hipster.txt',
  createdBy: 'distorted nightlife',
  createdDate: dayjs('2025-06-16T01:34'),
  lastModifiedBy: 'difficult',
  lastModifiedDate: dayjs('2025-06-16T14:13'),
};

export const sampleWithNewData: NewBookSell = {
  username: 'barring like linseed',
  country: 'Georgia del sud e South Sandwich Islands',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
