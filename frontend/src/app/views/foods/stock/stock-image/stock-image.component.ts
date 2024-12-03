import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GenericService } from 'src/app/services/generic.service';
import { DxDataGridComponent } from "devextreme-angular";
import CustomStore from 'devextreme/data/custom_store';
import { UtilService } from 'src/app/services/util.service';

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

  constructor(public service: GenericService) {
    this.imageService = this.service.instance('payment/stocks/cdn');
  }

  ngOnInit(): void {
    console.log(this.data)
    this.loadGrid(this.data.id);
  }

  loadGrid(id: any) {
    this.dataSource = new CustomStore({
      key: 'stock.id',
      load: (loadOptions) => {
        loadOptions.filter = this.getFilters(id);
        return this.imageService.pageableLoad(UtilService.setPage(loadOptions)).then((response: any) => {
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
}
