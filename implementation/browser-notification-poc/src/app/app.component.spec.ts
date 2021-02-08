import { async, TestBed, } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ServiceWorkerModule, SwPush } from '@angular/service-worker';
import { AppComponent } from './app.component';
import { PushnotificationService } from './services/pushnotification/pushnotification.service';

const mockNav = navigator as any;

describe('AppComponent', () => {
  let fixture;
  beforeEach(async () => {

    mockNav.permissions = {query: jest.fn(() => Promise.resolve({state: 'granted'})) } as any;

    TestBed.configureTestingModule({
      imports: [
        ServiceWorkerModule.register('my-service-worker.js', {enabled: false}),
        RouterTestingModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        PushnotificationService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
  });

  it('should create the app', async () => {
    const app = fixture.componentInstance;
    app.ngOnInit();
    expect(app).toBeTruthy();
  });

  it('should request notifications permissions', async () => {
    const app = fixture.componentInstance;
    app.ngOnInit();
    expect(mockNav.permissions.query).toHaveBeenCalledWith({name: 'notifications'});
  });

  it(`should have as title 'browser-notification-poc'`, async () => {
    const app = fixture.componentInstance;
    expect(app.title).toEqual('browser-notification-poc');
  });

  it('should render title', async () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.header .title h1').textContent).toContain('Webbrowser Notifications');
  });
});
