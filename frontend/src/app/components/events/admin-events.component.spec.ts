import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEventsComponent } from './admin-events.component';

describe('EventsComponent', () => {
  let component: AdminEventsComponent;
  let fixture: ComponentFixture<AdminEventsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminEventsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEventsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
