import { Component, ViewChild } from '@angular/core';
import { DxDataGridComponent } from "devextreme-angular";
import CustomStore from "devextreme/data/custom_store";
import { GenericService } from "../../../services/generic.service";
import { UtilService } from "../../../services/util.service";

@Component({
  selector: 'app-user-city',
  templateUrl: './user-city.component.html',
  styleUrls: ['./user-city.component.scss']
})
export class UserCityComponent {
  dataSource: any = {};
  @ViewChild('dataSourceGrid', { static: false }) dataSourceGrid: any = DxDataGridComponent;
  events: Array<string> = [];
  cityService: GenericService;

  showPageSizeSelector = true;
  showInfo = true;
  showNavButtons = true;

  constructor(private service: GenericService) {
    this.cityService = this.service.instance('payment/users/city');
    this.loadGrid();
  }

  ngOnInit(): void {
  }

  getTotalPageCount () {
    this.dataSourceGrid.instance.pageCount();
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
        //loadOptions.sort = loadOptions.sort == null ? 'creDate,desc' : loadOptions.sort;
        return this.cityService.pageableLoad(UtilService.setPage(loadOptions)).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },
      byKey: (key) => {
        return this.cityService.findOne(key).then((response: any) => {
          return response;
        });
      },
      insert: (values) => {
        return this.cityService.save(values).then((response) => {
          return;
        }
        );
      },
      update: (key, values: any) => {
        values.id = key;
        return this.cityService.update(null, values).then((response) => {
          return;
        }
        );
      },
      remove: (key) => {
        return this.cityService.delete(key).then((response) => {
          return;
        }
        );
      }
    });
  }

}
