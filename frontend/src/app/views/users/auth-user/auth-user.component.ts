import {Component, OnInit, ViewChild} from '@angular/core';
import {DxDataGridComponent} from "devextreme-angular";
import CustomStore from "devextreme/data/custom_store";
import {faRefresh} from "@fortawesome/free-solid-svg-icons";
import {GenericService} from "../../../services/generic.service";
import {UtilService} from "../../../services/util.service";

@Component({
  selector: 'app-service-user',
  templateUrl: './auth-user.component.html',
  styleUrls: ['./auth-user.component.scss']
})
export class AuthUserComponent implements OnInit {
  dataSource: any = {};
  cityDataSource: any = {};
  @ViewChild('dataSourceGrid', {static: true}) dataSourceGrid: any = DxDataGridComponent;
  updateMod: boolean = false;
  data: any;
  userService: GenericService;
  cityService: GenericService;
  tabList: any[] = [
    {id: 1, name: "Department", key: 'action'},
  ];

  constructor(private service: GenericService) {
    this.loadGrid = this.loadGrid.bind(this);
    this.loadCity = this.loadCity.bind(this);
    this.userService = this.service.instance('payment/users');
    this.cityService = this.service.instance('payment/users/city');

    this.loadGrid();
    this.loadCity();
    console.log(this.cityDataSource)
  }

  ngOnInit(): void {
  }

  onEditorPreparing(e: any) {
    if (e.parentType === 'dataRow') {
      this.data = e.row.data;
      this.updateMod = e.row.data.id != undefined;
    }
  }


  refreshDataGrid(e: any) {
    this.dataSourceGrid.instance.refresh();
  }

  loadCity() {
    this.cityService.findAll(UtilService.setPage({ skip: null, take: null })).then((response: any) => {
      this.cityDataSource = response.data;
    });
  }

  loadGrid() {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.userService.pageableLoad(UtilService.setPage(loadOptions)).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },
      byKey: (key) => {
        return this.userService.findOne(key).then((response: any) => {
          return response;
        });
      },
      insert: (values) => {
        values.roles=[];
        return this.userService.save(values).then((response) => {
            return;
          }
        );
      },
      update: (key, values: any) => {
        values.id = key;
        values.roles=[];
        return this.userService.update(null,values).then((response) => {
          return;
        }
        );
      },
      remove: (key) => {
        return this.userService.delete(key).then((response) => {
            return;
          }
        );
      }
    });
  }

  protected readonly faRefresh = faRefresh;
}
