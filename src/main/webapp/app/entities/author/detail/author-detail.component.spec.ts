import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { AuthorDetailComponent } from './author-detail.component';

describe('Author Management Detail Component', () => {
  let comp: AuthorDetailComponent;
  let fixture: ComponentFixture<AuthorDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthorDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./author-detail.component').then(m => m.AuthorDetailComponent),
              resolve: { author: () => of({ id: 'f5171914-f331-4410-8fa1-ec6fb0e306fe' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AuthorDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('should load author on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AuthorDetailComponent);

      // THEN
      expect(instance.author()).toEqual(expect.objectContaining({ id: 'f5171914-f331-4410-8fa1-ec6fb0e306fe' }));
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
