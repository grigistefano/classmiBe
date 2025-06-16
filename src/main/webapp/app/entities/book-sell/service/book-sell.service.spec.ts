import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IBookSell } from '../book-sell.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../book-sell.test-samples';

import { BookSellService, RestBookSell } from './book-sell.service';

const requireRestSample: RestBookSell = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
  createdDate: sampleWithRequiredData.createdDate?.toJSON(),
  lastModifiedDate: sampleWithRequiredData.lastModifiedDate?.toJSON(),
};

describe('BookSell Service', () => {
  let service: BookSellService;
  let httpMock: HttpTestingController;
  let expectedResult: IBookSell | IBookSell[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(BookSellService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a BookSell', () => {
      const bookSell = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(bookSell).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BookSell', () => {
      const bookSell = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(bookSell).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BookSell', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BookSell', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a BookSell', () => {
      const expected = true;

      service.delete('9fec3727-3421-4967-b213-ba36557ca194').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addBookSellToCollectionIfMissing', () => {
      it('should add a BookSell to an empty array', () => {
        const bookSell: IBookSell = sampleWithRequiredData;
        expectedResult = service.addBookSellToCollectionIfMissing([], bookSell);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookSell);
      });

      it('should not add a BookSell to an array that contains it', () => {
        const bookSell: IBookSell = sampleWithRequiredData;
        const bookSellCollection: IBookSell[] = [
          {
            ...bookSell,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addBookSellToCollectionIfMissing(bookSellCollection, bookSell);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BookSell to an array that doesn't contain it", () => {
        const bookSell: IBookSell = sampleWithRequiredData;
        const bookSellCollection: IBookSell[] = [sampleWithPartialData];
        expectedResult = service.addBookSellToCollectionIfMissing(bookSellCollection, bookSell);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookSell);
      });

      it('should add only unique BookSell to an array', () => {
        const bookSellArray: IBookSell[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const bookSellCollection: IBookSell[] = [sampleWithRequiredData];
        expectedResult = service.addBookSellToCollectionIfMissing(bookSellCollection, ...bookSellArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bookSell: IBookSell = sampleWithRequiredData;
        const bookSell2: IBookSell = sampleWithPartialData;
        expectedResult = service.addBookSellToCollectionIfMissing([], bookSell, bookSell2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bookSell);
        expect(expectedResult).toContain(bookSell2);
      });

      it('should accept null and undefined values', () => {
        const bookSell: IBookSell = sampleWithRequiredData;
        expectedResult = service.addBookSellToCollectionIfMissing([], null, bookSell, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bookSell);
      });

      it('should return initial array if no BookSell is added', () => {
        const bookSellCollection: IBookSell[] = [sampleWithRequiredData];
        expectedResult = service.addBookSellToCollectionIfMissing(bookSellCollection, undefined, null);
        expect(expectedResult).toEqual(bookSellCollection);
      });
    });

    describe('compareBookSell', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareBookSell(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };
        const entity2 = null;

        const compareResult1 = service.compareBookSell(entity1, entity2);
        const compareResult2 = service.compareBookSell(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };
        const entity2 = { id: 'dd7ebac8-0444-4a21-949e-70bf3fe14c26' };

        const compareResult1 = service.compareBookSell(entity1, entity2);
        const compareResult2 = service.compareBookSell(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };
        const entity2 = { id: '988b4cd0-eb0e-4ea2-b4c3-463d8da02d40' };

        const compareResult1 = service.compareBookSell(entity1, entity2);
        const compareResult2 = service.compareBookSell(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
