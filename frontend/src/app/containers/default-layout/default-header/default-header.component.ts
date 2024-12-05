import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { TokenService } from "../../../auth/service/token.service";

import { ClassToggleService, HeaderComponent } from '@coreui/angular';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
})
export class DefaultHeaderComponent extends HeaderComponent {

  @Input() sidebarId: string = "sidebar";

  public newMessages = new Array(4)
  public newTasks = new Array(5)
  public newNotifications = new Array(5)
  fullName: string = "";

  constructor(private classToggler: ClassToggleService, private tokenService: TokenService) {
    super();
    this.fullName = tokenService.getUserFullName();
  }

  logout(){
    this.tokenService.signOut();
  }
}
