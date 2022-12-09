import { TestBed } from '@angular/core/testing';

import { ProductsServicesService } from './products.service';

describe('ProductsServicesService', () => {
  let service: ProductsServicesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductsServicesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
