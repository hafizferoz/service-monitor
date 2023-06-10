import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { ServiceData } from './service-data';
import { ServiceDetails } from './service-details';

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
    this.http.get<ServiceData[]>(this.serviceDataUrl + '/load').subscribe(data => {
      this.serviceDataSubject.next(data);
      console.log(data);
    });
  }

  public updateServiceData(serviceData: ServiceData) {
    this.http.post<ServiceData>(this.serviceDataUrl + '/update', serviceData).subscribe(() => {
      this.loadServiceData();
    });
  }

  public getServiceDetails(serviceData: ServiceData) {
    return this.http.post<ServiceDetails>(this.serviceDataUrl + '/getdetails', serviceData);
    // .subscribe(data => {
    //   console.log(data);
    //   return data;
    // });
  }
}

