import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../book-sell.test-samples';

import { BookSellFormService } from './book-sell-form.service';

describe('BookSell Form Service', () => {
  let service: BookSellFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookSellFormService);
  });

  describe('Service methods', () => {
    describe('createBookSellFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookSellFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            date: expect.any(Object),
            country: expect.any(Object),
            bookState: expect.any(Object),
            quantity: expect.any(Object),
            price: expect.any(Object),
            note: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });

      it('passing IBookSell should create a new form with FormGroup', () => {
        const formGroup = service.createBookSellFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            username: expect.any(Object),
            date: expect.any(Object),
            country: expect.any(Object),
            bookState: expect.any(Object),
            quantity: expect.any(Object),
            price: expect.any(Object),
            note: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            book: expect.any(Object),
          }),
        );
      });
    });

    describe('getBookSell', () => {
      it('should return NewBookSell for default BookSell initial value', () => {
        const formGroup = service.createBookSellFormGroup(sampleWithNewData);

        const bookSell = service.getBookSell(formGroup) as any;

        expect(bookSell).toMatchObject(sampleWithNewData);
      });

      it('should return NewBookSell for empty BookSell initial value', () => {
        const formGroup = service.createBookSellFormGroup();

        const bookSell = service.getBookSell(formGroup) as any;

        expect(bookSell).toMatchObject({});
      });

      it('should return IBookSell', () => {
        const formGroup = service.createBookSellFormGroup(sampleWithRequiredData);

        const bookSell = service.getBookSell(formGroup) as any;

        expect(bookSell).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBookSell should not enable id FormControl', () => {
        const formGroup = service.createBookSellFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBookSell should disable id FormControl', () => {
        const formGroup = service.createBookSellFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
