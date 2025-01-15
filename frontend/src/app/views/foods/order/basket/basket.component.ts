import { ChangeDetectorRef, Component, OnChanges, SimpleChanges, ViewChild } from '@angular/core';
import CustomStore from "devextreme/data/custom_store";
import { DxDataGridComponent } from "devextreme-angular";
import { Orders } from "../../../../services/food-service-api";
import { GenericService } from '../../../../services/generic.service';
import { UtilService } from '../../../../services/util.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss']
})
export class BasketComponent implements OnChanges {
  @ViewChild('dataSourceGrid', { static: true }) dataSourceGrid: any = DxDataGridComponent;
  dataSource: any = {};
  totalPrice: number = 0;
  basketService: GenericService;
  events: Array<string> = [];

  constructor(private cd: ChangeDetectorRef,
    private service: GenericService) {
    this.basketService = this.service.instance('payment/stocks/basket');
    this.loadGrid();
  }

  ngOnChanges(changes: SimpleChanges): void {
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
        return this.basketService.pageableLoad(UtilService.setPage(loadOptions)).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },
      byKey: (key) => {
        return this.basketService.findOne(key).then((response: any) => {
          return response;
        });
      },
      remove: (key) => {
        return this.basketService.delete(key).then((response) => {
          return;
        }
        );
      }
    });
  }

}
