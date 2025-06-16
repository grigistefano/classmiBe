import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBookSell } from '../book-sell.model';
import { BookSellService } from '../service/book-sell.service';

const bookSellResolve = (route: ActivatedRouteSnapshot): Observable<null | IBookSell> => {
  const id = route.params.id;
  if (id) {
    return inject(BookSellService)
      .find(id)
      .pipe(
        mergeMap((bookSell: HttpResponse<IBookSell>) => {
          if (bookSell.body) {
            return of(bookSell.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bookSellResolve;
