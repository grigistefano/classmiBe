import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../book.test-samples';

import { BookFormService } from './book-form.service';

describe('Book Form Service', () => {
  let service: BookFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookFormService);
  });

  describe('Service methods', () => {
    describe('createBookFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createBookFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            titleSearch: expect.any(Object),
            isbn: expect.any(Object),
            publisher: expect.any(Object),
            publishedYear: expect.any(Object),
            isVerified: expect.any(Object),
            verifyUrl: expect.any(Object),
            viewAuthors: expect.any(Object),
            frontImageLink: expect.any(Object),
            backImageLink: expect.any(Object),
            pagesNumber: expect.any(Object),
            language: expect.any(Object),
            description: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            authors: expect.any(Object),
          }),
        );
      });

      it('passing IBook should create a new form with FormGroup', () => {
        const formGroup = service.createBookFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            titleSearch: expect.any(Object),
            isbn: expect.any(Object),
            publisher: expect.any(Object),
            publishedYear: expect.any(Object),
            isVerified: expect.any(Object),
            verifyUrl: expect.any(Object),
            viewAuthors: expect.any(Object),
            frontImageLink: expect.any(Object),
            backImageLink: expect.any(Object),
            pagesNumber: expect.any(Object),
            language: expect.any(Object),
            description: expect.any(Object),
            createdBy: expect.any(Object),
            createdDate: expect.any(Object),
            lastModifiedBy: expect.any(Object),
            lastModifiedDate: expect.any(Object),
            authors: expect.any(Object),
          }),
        );
      });
    });

    describe('getBook', () => {
      it('should return NewBook for default Book initial value', () => {
        const formGroup = service.createBookFormGroup(sampleWithNewData);

        const book = service.getBook(formGroup) as any;

        expect(book).toMatchObject(sampleWithNewData);
      });

      it('should return NewBook for empty Book initial value', () => {
        const formGroup = service.createBookFormGroup();

        const book = service.getBook(formGroup) as any;

        expect(book).toMatchObject({});
      });

      it('should return IBook', () => {
        const formGroup = service.createBookFormGroup(sampleWithRequiredData);

        const book = service.getBook(formGroup) as any;

        expect(book).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IBook should not enable id FormControl', () => {
        const formGroup = service.createBookFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewBook should disable id FormControl', () => {
        const formGroup = service.createBookFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
