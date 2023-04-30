import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { ServiceData } from './service-data';

@Injectable({
  providedIn: 'root'
})
export class ServiceDataService {
  private serviceDataUrl = '/service-data'; // URL to Spring Boot backend
  private serviceDataSubject = new BehaviorSubject<ServiceData[]>([]);
  public serviceData$ = this.serviceDataSubject.asObservable();

  constructor(private http: HttpClient) { 
    this.loadServiceData();
  }

  private loadServiceData() {
    this.http.get<ServiceData[]>(this.serviceDataUrl).subscribe(data => {
      this.serviceDataSubject.next(data);
      console.log(data);
    });
  }

  public updateServiceData(serviceData: ServiceData) {
    this.http.post<ServiceData>(this.serviceDataUrl, serviceData).subscribe(() => {
      this.loadServiceData();
    });
  }
}

