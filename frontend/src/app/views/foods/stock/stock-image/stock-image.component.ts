import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GenericService } from "../../../../services/generic.service";
import { DxDataGridComponent } from "devextreme-angular";
import CustomStore from 'devextreme/data/custom_store';


@Component({
  selector: 'app-stock-image',
  templateUrl: './stock-image.component.html',
  styleUrls: ['./stock-image.component.scss']
})
export class StockImageComponent implements OnInit {
  @Input() data: any;
  dataSource: any = {};
  @ViewChild('dataGrid', { static: true }) dataGrid: any = DxDataGridComponent;
  imageService: GenericService;
  popupVisible = false;
  params: Array<{ 'key': any, 'value': any }> = [];

  constructor(public service: GenericService) {
    this.imageService = this.service.instance('payment/stocks/cdn');
  }

  ngOnInit(): void {
    this.loadGrid(this.data.id);
  }

  loadGrid(id: any) {
    this.dataSource = new CustomStore({
      key: 'stockId',
      load: (loadOptions) => {
        loadOptions.filter = this.getFilters(id);
        return this.imageService.customGet("image?stockId=" + id).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },

      byKey: (key) => {
        return this.imageService.findOne(key).then((response) => {
          return response;
        });
      },
    });
  }

  getFilters(id: any): any {
    const filters: Array<{}> = [];
    filters.push(['stock.id', '=', id]);
    return filters;
  }


  openPopapFile() {
    this.popupVisible = true;
    this.params.push({ key: 'stockId', value: this.data.id });
  }

  fileEmitter(event: any) {
    if (event.status == 200) {
      this.popupVisible = false;
    }
  }
}
