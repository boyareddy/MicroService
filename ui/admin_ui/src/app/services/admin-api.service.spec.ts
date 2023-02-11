import { TestBed, inject } from '@angular/core/testing';
import { AdminApiService } from './admin-api.service';
import { HttpClient, HttpHandler } from '@angular/common/http';

describe('AdminApiServiceService', () => {
beforeEach(() => {
TestBed.configureTestingModule({
providers: [AdminApiService, HttpClient, HttpHandler]
});
});

it('should be created', inject([AdminApiService], (service: AdminApiService) => {
expect(service).toBeTruthy();
}));
});

