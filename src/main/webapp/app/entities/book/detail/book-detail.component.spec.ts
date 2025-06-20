import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { BookDetailComponent } from './book-detail.component';

describe('Book Management Detail Component', () => {
  let comp: BookDetailComponent;
  let fixture: ComponentFixture<BookDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BookDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./book-detail.component').then(m => m.BookDetailComponent),
              resolve: { book: () => of({ id: 'f8729558-5388-49c2-b7d6-94b789537deb' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BookDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BookDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load book on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BookDetailComponent);

      // THEN
      expect(instance.book()).toEqual(expect.objectContaining({ id: 'f8729558-5388-49c2-b7d6-94b789537deb' }));
    });
  });

  describe('PreviousState', () => {
    it('should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
