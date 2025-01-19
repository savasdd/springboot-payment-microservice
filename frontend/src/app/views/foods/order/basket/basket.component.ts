import { ChangeDetectorRef, Component, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import CustomStore from "devextreme/data/custom_store";
import { DxDataGridComponent } from "devextreme-angular";
import { Orders } from "../../../../services/food-service-api";
import { GenericService } from '../../../../services/generic.service';
import { UtilService } from '../../../../services/util.service';
import { MessageService } from '../../../../services/message.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss']
})
export class BasketComponent implements OnInit {
  @ViewChild('dataSourceGrid', { static: true }) dataSourceGrid: any = DxDataGridComponent;
  dataSource: any = {};
  totalPrice: number = 0;
  basketService: GenericService;
  orderService: GenericService;
  events: Array<string> = [];

  constructor(private cd: ChangeDetectorRef,
    private notify: MessageService,
    private service: GenericService) {
    this.basketService = this.service.instance('payment/stocks/basket');
    this.orderService = this.service.instance('order');
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


  payment() {
    const items = new Array;

    if (this.getDataSource().length > 0) {
      this.getDataSource().map((m: any) => {
        if (m.type == 'NEW') {
          items.push(new Product(m.stock.id, m.id, m.stock.stockName, m.disprice, m.quantity));
        }
      });
    }

    this.createOrder(items);
  }

  createOrder(items: any) {
    if (items.length > 0) {
      this.orderService.customPost('create', { 'items': items }).then((response: any) => {
        if (response.status == 200) {
          this.notify.success("Sipariş No: " + response.data.orderNo)
        } else {
          this.notify.error(response.data)
        }
      });
    }
  }

  getDataSource(): any {
    return this.dataSourceGrid.instance.getDataSource().items();
  }

}


export class Product {
  stockId: any = null;
  basketId: any = null;
  stockName: any = null;
  price: any = 0;
  quantity: any = 0;

  constructor(stockId: any, basketId: any, stockName: any, price: any, quantity: any) {
    this.stockId = stockId;
    this.basketId = basketId;
    this.stockName = stockName;
    this.price = price;
    this.quantity = quantity;
  }
}