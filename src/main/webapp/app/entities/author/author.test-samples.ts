import dayjs from 'dayjs/esm';

import { IAuthor, NewAuthor } from './author.model';

export const sampleWithRequiredData: IAuthor = {
  id: 'b96011de-7d95-4985-963f-1c3975479819',
  name: 'dearest inasmuch',
};

export const sampleWithPartialData: IAuthor = {
  id: '5a2ed85f-5539-4268-8d32-77ad0fb61f86',
  name: 'bar daily',
  createdBy: 'consequently simple',
  createdDate: dayjs('2025-06-16T11:05'),
  lastModifiedDate: dayjs('2025-06-16T02:53'),
};

export const sampleWithFullData: IAuthor = {
  id: '77800a5d-a218-4423-b7e7-873dfc7ff5d1',
  name: 'lest',
  createdBy: 'guest incomparable oddball',
  createdDate: dayjs('2025-06-16T14:35'),
  lastModifiedBy: 'acceptable',
  lastModifiedDate: dayjs('2025-06-15T22:52'),
};

export const sampleWithNewData: NewAuthor = {
  name: 'upbeat since bah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
