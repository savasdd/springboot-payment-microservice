import { Component, OnInit, ViewChild } from '@angular/core';
import { DxDataGridComponent } from "devextreme-angular";
import CustomStore from "devextreme/data/custom_store";
import { GenericService } from "../../../services/generic.service";
import { UtilService } from "../../../services/util.service";

@Component({
  selector: 'app-service-rol',
  templateUrl: './auth-rol.component.html',
  styleUrls: ['./auth-rol.component.scss']
})
export class AuthRolComponent implements OnInit {
  dataSource: any = {};
  @ViewChild('dataSourceGrid', { static: true }) dataSourceGrid: any = DxDataGridComponent;
  events: Array<string> = [];
  roleService: GenericService;

  constructor(private service: GenericService) {
    this.roleService = this.service.instance('payment/users/role');
    this.loadGrid();
  }

  ngOnInit(): void {
  }

  logEvent(eventName: any) {
    this.events.unshift(eventName);
  }

  refreshDataGrid(e: any) {
    this.dataSourceGrid.instance.refresh();
  }

  loadGrid() {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.roleService.findAllPageable(UtilService.setPage(loadOptions), 0, 200, 'id,desc').then((response: any) => {
          return {
            data: response.data.content,
            totalCount: response.data.numberOfElements
          };
        });
      },
      byKey: (key) => {
        return this.roleService.findOne(key).then((response: any) => {
          return response;
        });
      },
      insert: (values) => {
        return this.roleService.save(values).then((response) => {
          return;
        }
        );
      },
      update: (key, values: any) => {
        values.id = key;
        return this.roleService.update(null, values).then((response) => {
          return;
        }
        );
      },
      remove: (key) => {
        return this.roleService.delete(key).then((response) => {
          return;
        }
        );
      }
    });
  }
}
