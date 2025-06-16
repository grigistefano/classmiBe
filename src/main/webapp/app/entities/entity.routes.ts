import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'classmiBeApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'book',
    data: { pageTitle: 'classmiBeApp.book.home.title' },
    loadChildren: () => import('./book/book.routes'),
  },
  {
    path: 'author',
    data: { pageTitle: 'classmiBeApp.author.home.title' },
    loadChildren: () => import('./author/author.routes'),
  },
  {
    path: 'book-sell',
    data: { pageTitle: 'classmiBeApp.bookSell.home.title' },
    loadChildren: () => import('./book-sell/book-sell.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
