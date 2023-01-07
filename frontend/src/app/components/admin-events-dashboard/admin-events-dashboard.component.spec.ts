import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminEventsDashboardComponent } from './admin-events-dashboard.component';

describe('EventsComponent', () => {
  let component: AdminEventsDashboardComponent;
  let fixture: ComponentFixture<AdminEventsDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminEventsDashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminEventsDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
