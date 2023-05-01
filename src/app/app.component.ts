import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { interval } from 'rxjs';
import { ServiceDataService } from './service-data.service';
import { ServiceData } from './service-data';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  serviceUrl: string;
  appName: string;
  title = 'angular-control-app';
  services: any = [];
  startUrl: string;
  stopUrl: string;


  constructor(private http: HttpClient, private serviceDataService: ServiceDataService) {
    this.loadServiceData();
  }
  
  private loadServiceData() {
    this.serviceDataService.serviceData$.subscribe(serviceDataList => {
      serviceDataList.forEach(serviceData => {
        this.monitorService(serviceData);
      });
    });
  }

  monitorService(serviceData: ServiceData) {
    const url = serviceData.url;
    const startUrl = serviceData.startUrl;
    const stopUrl = serviceData.stopUrl;
    const name = serviceData.name;
    interval(5000).subscribe(() => {
      this.http.get<any>(`${url}`).subscribe(data => {
        const status = data.status;
        const dateTime = new Date();
        const service = this.services.find(s => s.url === url);
  
        if (!service) {
          this.services.push({
            name,
            url,
            startUrl,
            stopUrl,
            status,
            startTime: dateTime,
            stopTime: null,
            upTime: null,
            downTime: null
          });
        } else {
          if (service.status !== status) {
            if (status === 'UP') {
              if (service.stopTime) {
                service.upTime = this.getTimeDiff(service.startTime, dateTime);
                service.startTime = dateTime;
                service.stopTime = null;
              } else {
                service.upTime = this.getTimeDiff(service.startTime, dateTime);
              }
            } else {
              if (service.stopTime) {
                service.downTime = this.getTimeDiff(service.startTime, dateTime);
              } else {
                service.downTime = this.getTimeDiff(service.startTime, dateTime);
                service.stopTime = dateTime;
              }
            }
          }
          service.status = status;
        }
      },
      error => {
        console.log(`Error monitoring ${name}: ${error.message}`);
        const status = "DOWN";
        const dateTime = new Date();
        const service = this.services.find(service => service.url === url);
  
        if (!service) {
          this.services.push({
            name,
            url,
            startUrl,
            stopUrl,
            status,
            startTime: dateTime,
            stopTime: null,
            upTime: null,
            downTime: null
          });
        } else {
          if (service.status === status) {
            if (service.stopTime) {
              service.downTime = this.getTimeDiff(service.startTime, dateTime);
            } else {
              service.downTime = this.getTimeDiff(service.startTime, dateTime);
              service.stopTime = dateTime;
            }
         }
          service.status = status;
        }
      });
    });
  }
  
  
  getTimeDiff(start: Date, end: Date): string {
    const diffInMs = Math.abs(end.getTime() - start.getTime());
    const diffInHours = Math.ceil(diffInMs / (1000 * 60 * 60));
    return `${diffInHours} hour(s)`;
  }
  

  startService(startUrl: string) {
    this.http.post<any>(`${startUrl}`, {}).subscribe(data => {
      console.log(data);
    });
  }

  stopService(stopUrl: string) {
    this.http.post<any>(`${stopUrl}`, {}).subscribe(data => {
      console.log(data);
    });
  }

  addServiceData() {
    const data: ServiceData = new ServiceData();
    data.url = this.serviceUrl;
    data.startUrl=this.startUrl;
    data.stopUrl=this.stopUrl;
    data.name = this.appName;
    this.serviceDataService.updateServiceData(data);
    this.loadServiceData();

  }
}
