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
  dateFormat:string = 'dd/MM/yyyy HH:mm:ss';


  constructor(private http: HttpClient, private serviceDataService: ServiceDataService) {
    this.loadServiceData();
  }
  
  private loadServiceData() {
    this.serviceDataService.serviceData$.subscribe(serviceDataList => {
      if(serviceDataList.length==0)
          this.services = [];
      serviceDataList.forEach(serviceData => {
        console.log("servicedata: ", serviceData);
        this.monitorService(serviceData);
      });
    });
  }

  monitorService(serviceData: ServiceData) {
    const id = serviceData.id;
    const url = serviceData.url;
    const startUrl = serviceData.startUrl;
    const stopUrl = serviceData.stopUrl;
    const name = serviceData.name;
    const startTime = serviceData.startTime;
    const stopTime = serviceData.stopTime;
    const upTime = serviceData.upTime;
    const downTime = serviceData.downTime;
    const subscription = interval(15000).subscribe(() => {
     // this.http.get<any>(`${url}`).subscribe(data => {
      this.serviceDataService.getServiceDetails(serviceData).subscribe(data =>{
        const status = data.status;
        const dateTime = new Date();
        const service = this.services.find(s => s.url === url);
        // if(startTime===null)
        //         startTime=dateTime;
        if (!service) {
       const serve =   {
            id,
            name,
            url,
            startUrl,
            stopUrl,
            status,
            startTime,
            stopTime,
            upTime,
            downTime,
            subscription
          };
          this.services.push(serve);
          this.updateServiceData(serve);
        } else {
          if (service.status !== status) {
            if (status === 'UP') {
              if (service.stopTime) {
                service.upTime = this.getTimeDiff(new Date(service.startTime), dateTime);
                service.startTime = dateTime;
                service.stopTime = null;
              } else {
                service.upTime = this.getTimeDiff(new Date(service.startTime), dateTime);
              }
            } else {
              if (service.stopTime) {
                service.downTime = this.getTimeDiff(new Date(service.startTime), dateTime);
              } else {
                service.downTime = this.getTimeDiff(new Date(service.startTime), dateTime);
                service.stopTime = dateTime.toISOString();
              }
              this.serviceDataService.sendEmail(this.services);
            }
            this.updateServiceData(service);
          }
          service.status = status;
          //this.updateServiceData(service);
        }
      },
      error => {
        console.log(`Error monitoring ${name}: ${error.message}`);
        const status = "DOWN";
        const dateTime = new Date();
        const service = this.services.find(s => s.url === url);
        // if(startTime===null)
        //     startTime=dateTime;
        if (!service) {
          const serve =   {
            id,
            name,
            url,
            startUrl,
            stopUrl,
            status,
            startTime,
            stopTime,
            upTime,
            downTime,
            subscription
          };
          this.services.push(serve);
          this.updateServiceData(serve);
        } else {
          if (service.status === status) {
            if (service.stopTime) {
              const lastStopTime = service.downTime;
              service.downTime = this.getTimeDiff(new Date(service.startTime), dateTime);
              if(lastStopTime!== service.downTime)
              this.updateServiceData(service);
            } else {
              service.downTime = this.getTimeDiff(new Date(service.startTime), dateTime);
              service.stopTime = dateTime.toISOString();
              this.updateServiceData(service);
            }
            this.serviceDataService.sendEmail(this.services);
         }
          service.status = status;
          //this.updateServiceData(service);
        }
      });
      

    // const index = this.services.findIndex(s => s.url === url);
    // if (index === -1 && !service) {
    //   subscription.unsubscribe();
    // }
    });
  }
  
  
  getTimeDiff(start: Date, end: Date): string {
    console.log("start:"+ start);
    console.log("end:"+ end);
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
  removeService(serviceId: number){
    const data: ServiceData = new ServiceData();
    data.id=serviceId;
    const index = this.services.findIndex((s: any) => s.id === serviceId);
    const service = this.services.find(s => s.id === serviceId);
      // Remove the 'service' object from the array
      if (index !== -1) {
        // Unsubscribe from the interval when the service is removed
        console.log("service removed: ",service);
        service.subscription.unsubscribe();
        this.services.splice(index, 1);
      }
      this.serviceDataService.removeServiceData(data);
     // this.loadServiceData();
  }

  addServiceData() {
    if(this.serviceUrl===null || this.appName===null){
      alert("Service Name and URL is required");
      return;
    }
    const service = this.services.find(s => s.url === this.serviceUrl);
    if(!service){
    const data: ServiceData = new ServiceData();
    data.url = this.serviceUrl;
    data.startUrl=this.startUrl;
    data.stopUrl=this.stopUrl;
    data.name = this.appName;
    data.startTime= new Date().toISOString();
   
    this.serviceDataService.addServiceData(data);
   // this.loadServiceData();
    
    }
    else {
      alert("Service url " + this.serviceUrl +" already exists");
    }

  }
  updateServiceData(service: any) {
    const data: ServiceData = new ServiceData();
    data.url = service.url;
    data.startUrl=service.startUrl;
    data.stopUrl=service.stopUrl;
    data.name = service.name;
    data.startTime = service.startTime;
    data.stopTime = service.stopTime;
    data.downTime=service.downTime;
    data.status = service.status;
    data.upTime = service.upTime;
    data.id=service.id;
    this.serviceDataService.updateServiceData(data);

  }
}
