import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import BookSellResolve from './route/book-sell-routing-resolve.service';

const bookSellRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/book-sell.component').then(m => m.BookSellComponent),
    data: {},
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/book-sell-detail.component').then(m => m.BookSellDetailComponent),
    resolve: {
      bookSell: BookSellResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/book-sell-update.component').then(m => m.BookSellUpdateComponent),
    resolve: {
      bookSell: BookSellResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/book-sell-update.component').then(m => m.BookSellUpdateComponent),
    resolve: {
      bookSell: BookSellResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bookSellRoute;
