import { TestBed } from '@angular/core/testing';
import { PushnotificationService } from './pushnotification.service';


describe('PushnotificationService', () => {
  let service: PushnotificationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PushnotificationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  // it('should send a notification', (done) => {
  //   const data: Array<any> = [];

  //   data.push({
  //     title: 'VIBES UX',
  //     alertContent: 'The conveyer belt is stuck!',
  //     actions: [{
  //       action: 'acknowledge',
  //       title: 'Acknowledge alert'
  //     }, {
  //       action: 'close',
  //       title: 'Close alert'
  //     }],
  //     data: {
  //       priority: 5
  //     }
  //   },
  //   {
  //     title: 'VIBES UX',
  //     alertContent: 'Baggage is missing.',
  //     actions: [{
  //       action: 'acknowledge',
  //       title: 'Acknowledge alert'
  //     }, {
  //       action: 'close',
  //       title: 'Close alert'
  //     }],
  //     data: {
  //       priority: 5
  //     }
  //   });

  //   expect(service.generateNotification(data)).toHaveBeenCalled();
  // });
});
