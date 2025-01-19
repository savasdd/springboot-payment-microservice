import { Injectable } from "@angular/core";
import notify from 'devextreme/ui/notify';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  display: number = 5000;

  constructor() {
  }

  success(message: string) {
    notify({ message: message, width: 300, displayTime: this.display, type: 'success', shading: true }, {
      position: "top right",
      direction: "down-push"
    });
  }

  info(message: string) {
    notify({ message: message, width: 300, displayTime: this.display, type: 'info', shading: true }, {
      position: "top right",
      direction: "down-push"
    });
  }

  error(message: string) {
    notify({ message: message, width: 300, displayTime: this.display, type: 'error', shading: true }, {
      position: "top right",
      direction: "down-push"
    });
  }

  warning(message: string) {
    notify({ message: message, width: 300, displayTime: this.display, type: 'warning', shading: true }, {
      position: "top right",
      direction: "down-push"
    });
  }



}
