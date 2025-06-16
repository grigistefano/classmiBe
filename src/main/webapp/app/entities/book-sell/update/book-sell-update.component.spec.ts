import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IBook } from 'app/entities/book/book.model';
import { BookService } from 'app/entities/book/service/book.service';
import { BookSellService } from '../service/book-sell.service';
import { IBookSell } from '../book-sell.model';
import { BookSellFormService } from './book-sell-form.service';

import { BookSellUpdateComponent } from './book-sell-update.component';

describe('BookSell Management Update Component', () => {
  let comp: BookSellUpdateComponent;
  let fixture: ComponentFixture<BookSellUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bookSellFormService: BookSellFormService;
  let bookSellService: BookSellService;
  let bookService: BookService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [BookSellUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(BookSellUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BookSellUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bookSellFormService = TestBed.inject(BookSellFormService);
    bookSellService = TestBed.inject(BookSellService);
    bookService = TestBed.inject(BookService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('should call Book query and add missing value', () => {
      const bookSell: IBookSell = { id: 'dd7ebac8-0444-4a21-949e-70bf3fe14c26' };
      const book: IBook = { id: 'f8729558-5388-49c2-b7d6-94b789537deb' };
      bookSell.book = book;

      const bookCollection: IBook[] = [{ id: 'f8729558-5388-49c2-b7d6-94b789537deb' }];
      jest.spyOn(bookService, 'query').mockReturnValue(of(new HttpResponse({ body: bookCollection })));
      const additionalBooks = [book];
      const expectedCollection: IBook[] = [...additionalBooks, ...bookCollection];
      jest.spyOn(bookService, 'addBookToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ bookSell });
      comp.ngOnInit();

      expect(bookService.query).toHaveBeenCalled();
      expect(bookService.addBookToCollectionIfMissing).toHaveBeenCalledWith(
        bookCollection,
        ...additionalBooks.map(expect.objectContaining),
      );
      expect(comp.booksSharedCollection).toEqual(expectedCollection);
    });

    it('should update editForm', () => {
      const bookSell: IBookSell = { id: 'dd7ebac8-0444-4a21-949e-70bf3fe14c26' };
      const book: IBook = { id: 'f8729558-5388-49c2-b7d6-94b789537deb' };
      bookSell.book = book;

      activatedRoute.data = of({ bookSell });
      comp.ngOnInit();

      expect(comp.booksSharedCollection).toContainEqual(book);
      expect(comp.bookSell).toEqual(bookSell);
    });
  });

  describe('save', () => {
    it('should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookSell>>();
      const bookSell = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };
      jest.spyOn(bookSellFormService, 'getBookSell').mockReturnValue(bookSell);
      jest.spyOn(bookSellService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookSell });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookSell }));
      saveSubject.complete();

      // THEN
      expect(bookSellFormService.getBookSell).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(bookSellService.update).toHaveBeenCalledWith(expect.objectContaining(bookSell));
      expect(comp.isSaving).toEqual(false);
    });

    it('should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookSell>>();
      const bookSell = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };
      jest.spyOn(bookSellFormService, 'getBookSell').mockReturnValue({ id: null });
      jest.spyOn(bookSellService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookSell: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bookSell }));
      saveSubject.complete();

      // THEN
      expect(bookSellFormService.getBookSell).toHaveBeenCalled();
      expect(bookSellService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBookSell>>();
      const bookSell = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };
      jest.spyOn(bookSellService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bookSell });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bookSellService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBook', () => {
      it('should forward to bookService', () => {
        const entity = { id: 'f8729558-5388-49c2-b7d6-94b789537deb' };
        const entity2 = { id: '445c6872-bdd7-4b18-bfe7-e3e0b79feccd' };
        jest.spyOn(bookService, 'compareBook');
        comp.compareBook(entity, entity2);
        expect(bookService.compareBook).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
