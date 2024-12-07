import { Component, OnInit, ViewChild } from '@angular/core';
import { DxDataGridComponent } from "devextreme-angular";
import { Stock } from "../../../services/stock-service-api";
import CustomStore from "devextreme/data/custom_store";
import { UtilService } from "../../../services/util.service";
import { GenericService } from "../../../services/generic.service";
import { DxFormComponent } from "devextreme-angular";
import UnitEnum = Stock.UnitEnum;

@Component({
  selector: 'app-stock',
  templateUrl: './stock.component.html',
  styleUrls: ['./stock.component.scss']
})
export class StockComponent implements OnInit {
  dataSource: any = {};
  @ViewChild('stockDataGrid', { static: true }) stockDataGrid: any = DxDataGridComponent;
  fileModel: FileModel = new FileModel();
  @ViewChild(DxFormComponent, { static: false }) form: any = DxFormComponent;
  dropDownOptions: any;
  stockService: GenericService;
  dataUnitSource: any = [
    { name: UnitEnum.Adet },
    { name: UnitEnum.Kilogram },
    { name: UnitEnum.Gram },
    { name: UnitEnum.Ton },
    { name: UnitEnum.Litre },
    { name: UnitEnum.Metre },
    { name: UnitEnum.Santimetre },
  ];
  popupVisible = false;

  constructor(public service: GenericService) {
    this.stockService = this.service.instance('payment/stocks');
    this.loadGrid();
  }

  ngOnInit(): void {
  }

  onSelectionPersonChanged(selectedRowKeys: any, cellInfo: any, dropDownBoxComponent: any) {
    cellInfo.setValue(selectedRowKeys[0]);
    if (selectedRowKeys.length > 0) {
      dropDownBoxComponent.close();
    }
  }

  refreshDataGrid(e: any) {
    this.stockDataGrid.instance.refresh();
  }

  loadGrid() {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.stockService.pageableLoad(UtilService.setPage(loadOptions)).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },

      byKey: (key) => {
        return this.stockService.findOne(key).then((response) => {
          return response;
        });
      },

      insert: (values) => {
        values.details = [];
        return this.stockService.save(values).then((response) => {
          return;
        });
      },
      update: (key, values: any) => {
        values.id = key;
        values.details = [];
        return this.stockService.update(key, values).then((response) => {
          return;
        });
      },
      remove: (key) => {
        return this.stockService.delete(key).then((response) => {
          return;
        });
      }
    });
  }


  openPopapExcel() {
    this.popupVisible = true;
  }

  onValueChanged(e: any) {
    const file = e.value[0];
    const fileReader = new FileReader();
    fileReader.onload = () => {
      this.fileModel.fileData = fileReader.result as ArrayBuffer;
      this.fileModel.fileBlob = new Blob([fileReader.result as ArrayBuffer], { type: file.type });
      this.fileModel.file = file;
    }
    fileReader.readAsDataURL(file);
  }

  uploadFile() {
    const formValid = this.form.instance.validate();
    if (formValid && this.fileModel.fileData != null) {
      const formData = new FormData();
      formData.append('userId', '88');
      formData.append('file', this.fileModel.file);

      this.stockService.customPost("excel", formData).then((response: any) => {
      });

    }
  }

}


export class FileModel {
  userId?: string = '88';
  fileData: any;
  fileBlob: any;
  file: any;

  constructor() {
  }
}