import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ServiceWorkerModule, SwPush } from '@angular/service-worker';
import { AppComponent } from './app.component';
import { NotificationService } from './service/notification/notification.service';

describe('AppComponent', () => {
  let fixture;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [ServiceWorkerModule.register('sw.js', { enabled: false }), RouterTestingModule],
      declarations: [AppComponent],
      providers: [
        {provide: SwPush, useValue: {fetchThings: jest.fn() }},
        { provide: NotificationService, useValue: { fetchThings: jest.fn() } }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
  });

  it('should create the app', () => {
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'fe-push-notification-poc'`, () => {
    const app = fixture.componentInstance;
    expect(app.title).toEqual('fe-push-notification-poc');
  });
});
