import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { ServiceData } from './service-data';
import { ServiceDetails } from './service-details';

@Injectable({
  providedIn: 'root'
})
export class ServiceDataService {
 
  private serviceDataUrl = '/servicedata'; // URL to Spring Boot backend
  private serviceDataSubject = new BehaviorSubject<ServiceData[]>([]);
  public serviceData$ = this.serviceDataSubject.asObservable();

  constructor(private http: HttpClient) { 
    this.loadServiceData();
  }

  private loadServiceData() {
    this.http.get<ServiceData[]>(this.serviceDataUrl + '/load').subscribe(data => {
      this.serviceDataSubject.next(data);
      console.log("data:", data);
    }, error => {
      this.serviceDataSubject.next(null);
      console.log("Error loading service data: " + error.message);
    });
  }

  public addServiceData(serviceData: ServiceData) {
    this.http.post<ServiceData>(this.serviceDataUrl + '/add', serviceData).subscribe(() => {
      this.loadServiceData();
    }, error => {
      console.log("Error adding service data: " + error.message);
    });
  }

  public updateServiceData(serviceData: ServiceData) {
    this.http.post<ServiceData>(this.serviceDataUrl + '/update', serviceData).subscribe(() => {
      this.loadServiceData();
    }, error => {
      console.log("Error updating service data: " + error.message);
    });
  }

  public getServiceDetails(serviceData: ServiceData) {
    return this.http.post<ServiceDetails>(this.serviceDataUrl + '/getdetails', serviceData);
    // .subscribe(data => {
    //   console.log(data);
    //   return data;
    // });
  }
  public removeServiceData(serviceData: ServiceData) {
    this.http.post(this.serviceDataUrl + '/remove', serviceData).subscribe(() => {
      console.log("removed:" + serviceData.id);
      this.loadServiceData();
    },
    error => {
      console.log("Error removing service: " + error.message);
    });
  }

  public sendEmail(serviceList: any[]) {
    const downServices = serviceList.filter(service => service.status === 'DOWN');
    
    if (downServices.length > 0) {
      const serviceDataList: ServiceData[] = downServices.map(service => {
        const serviceData:ServiceData = {
          id: service.id,
          name: service.name,
          status: service.status,
          url: service.url,
          startUrl: service.startUrl,
          stopUrl: service.stopUrl,
          startTime: service.startTime,
          stopTime: service.stopTime,
          upTime: service.upTime,
          downTime: service.downTime
        };
        return serviceData;
      });
      this.http.post<void>(`${this.serviceDataUrl}/sendEmail`, serviceDataList).subscribe(() => {
        console.log('Email sent successfully.');
      }, error => {
        console.log('Error sending email:', error);
      });
    }
  }

}

