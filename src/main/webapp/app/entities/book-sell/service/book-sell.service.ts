import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBookSell, NewBookSell } from '../book-sell.model';

export type PartialUpdateBookSell = Partial<IBookSell> & Pick<IBookSell, 'id'>;

type RestOf<T extends IBookSell | NewBookSell> = Omit<T, 'date' | 'createdDate' | 'lastModifiedDate'> & {
  date?: string | null;
  createdDate?: string | null;
  lastModifiedDate?: string | null;
};

export type RestBookSell = RestOf<IBookSell>;

export type NewRestBookSell = RestOf<NewBookSell>;

export type PartialUpdateRestBookSell = RestOf<PartialUpdateBookSell>;

export type EntityResponseType = HttpResponse<IBookSell>;
export type EntityArrayResponseType = HttpResponse<IBookSell[]>;

@Injectable({ providedIn: 'root' })
export class BookSellService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/book-sells');

  create(bookSell: NewBookSell): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookSell);
    return this.http
      .post<RestBookSell>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bookSell: IBookSell): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookSell);
    return this.http
      .put<RestBookSell>(`${this.resourceUrl}/${this.getBookSellIdentifier(bookSell)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bookSell: PartialUpdateBookSell): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bookSell);
    return this.http
      .patch<RestBookSell>(`${this.resourceUrl}/${this.getBookSellIdentifier(bookSell)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestBookSell>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBookSell[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBookSellIdentifier(bookSell: Pick<IBookSell, 'id'>): string {
    return bookSell.id;
  }

  compareBookSell(o1: Pick<IBookSell, 'id'> | null, o2: Pick<IBookSell, 'id'> | null): boolean {
    return o1 && o2 ? this.getBookSellIdentifier(o1) === this.getBookSellIdentifier(o2) : o1 === o2;
  }

  addBookSellToCollectionIfMissing<Type extends Pick<IBookSell, 'id'>>(
    bookSellCollection: Type[],
    ...bookSellsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bookSells: Type[] = bookSellsToCheck.filter(isPresent);
    if (bookSells.length > 0) {
      const bookSellCollectionIdentifiers = bookSellCollection.map(bookSellItem => this.getBookSellIdentifier(bookSellItem));
      const bookSellsToAdd = bookSells.filter(bookSellItem => {
        const bookSellIdentifier = this.getBookSellIdentifier(bookSellItem);
        if (bookSellCollectionIdentifiers.includes(bookSellIdentifier)) {
          return false;
        }
        bookSellCollectionIdentifiers.push(bookSellIdentifier);
        return true;
      });
      return [...bookSellsToAdd, ...bookSellCollection];
    }
    return bookSellCollection;
  }

  protected convertDateFromClient<T extends IBookSell | NewBookSell | PartialUpdateBookSell>(bookSell: T): RestOf<T> {
    return {
      ...bookSell,
      date: bookSell.date?.toJSON() ?? null,
      createdDate: bookSell.createdDate?.toJSON() ?? null,
      lastModifiedDate: bookSell.lastModifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restBookSell: RestBookSell): IBookSell {
    return {
      ...restBookSell,
      date: restBookSell.date ? dayjs(restBookSell.date) : undefined,
      createdDate: restBookSell.createdDate ? dayjs(restBookSell.createdDate) : undefined,
      lastModifiedDate: restBookSell.lastModifiedDate ? dayjs(restBookSell.lastModifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBookSell>): HttpResponse<IBookSell> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBookSell[]>): HttpResponse<IBookSell[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
