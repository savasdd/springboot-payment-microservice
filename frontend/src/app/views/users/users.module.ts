import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ButtonGroupModule,
  ButtonModule,
  CardModule,
  CollapseModule,
  DropdownModule,
  FormModule,
  GridModule,
  NavbarModule,
  NavModule,
  SharedModule,
  UtilitiesModule
} from "@coreui/angular";
import { IconModule } from "@coreui/icons-angular";
import { ReactiveFormsModule } from "@angular/forms";
import { DocsComponentsModule } from "@docs-components/docs-components.module";
import { RouterModule, Routes } from "@angular/router";
import { DevExtremeModule } from "devextreme-angular";
import { UserProfileComponent } from "./user-profile/user-profile.component";
import { AuthUserComponent } from './auth-user/auth-user.component';
import { AuthRolComponent } from './auth-rol/auth-rol.component';
import { UserGroupComponent } from './auth-user/user-group/user-group.component';
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { UserDepartmentComponent } from './auth-user/user-department/user-department.component';
import { UserCityComponent } from './user-city/user-city.component';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: 'profile', component: UserProfileComponent,
        data: {
          title: 'Profile'
        }
      },
      {
        path: 'user', component: AuthUserComponent,
        data: {
          title: ''
        }
      },
      {
        path: 'rol', component: AuthRolComponent,
        data: {
          title: ''
        }
      },
      {
        path: 'city', component: UserCityComponent,
        data: {
          title: ''
        }
      },
    ]
  }
];

@NgModule({
  declarations: [
    UserProfileComponent,
    AuthUserComponent,
    AuthRolComponent,
    UserGroupComponent,
    UserDepartmentComponent,
    UserCityComponent
  ],
  imports: [
    CommonModule,
    RouterModule.forChild(routes),
    ButtonModule,
    ButtonGroupModule,
    GridModule,
    IconModule,
    CardModule,
    UtilitiesModule,
    DropdownModule,
    SharedModule,
    FormModule,
    ReactiveFormsModule,
    DocsComponentsModule,
    NavbarModule,
    CollapseModule,
    NavModule,
    DevExtremeModule,
    FontAwesomeModule,
  ]
})
export class UsersModule {
}
