import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import CustomStore from "devextreme/data/custom_store";
import { faShoppingBasket, faTrashAlt } from "@fortawesome/free-solid-svg-icons";
import { DxDataGridComponent } from "devextreme-angular";
import { Orders } from "../../../services/food-service-api";
import { MessageService } from "../../../services/message.service";
import { Router } from "@angular/router";
import { UtilService } from "../../../services/util.service";
import { GenericService } from "../../../services/generic.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  preserveWhitespaces: true,
})
export class OrderComponent implements OnInit {
  @ViewChild('orderDataGrid', { static: true }) orderDataGrid: any = DxDataGridComponent;
  dataSource: any = {};
  totalPrice: number = 0;
  stockService: GenericService;
  basketList: Array<{ ID: string, Name: string, Price: number, Image: string }> = [];

  constructor(public service: GenericService,
    private messageService: MessageService,
    private router: Router,
    private cd: ChangeDetectorRef) {
    this.stockService = this.service.instance('payment/stocks/elastic/');
    this.loadGrid();
    this.totalPrice = 0;
  }

  ngOnInit(): void {
  }


  loadGrid() {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.stockService.search(UtilService.setPage(loadOptions), null).then((response: any) => {
          console.log(response)

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
    });
  }


  callBasket() {
    if (this.totalPrice > 0) {
      this.router.navigate(['home/foods/baskets']);
    }
  }

  calculateBasket(data: any[]) {
    if (data) {
      this.totalPrice = 0;
      data.map((m) => {
        this.totalPrice = this.totalPrice + m.price;
      });
    }
  }


  refreshDataGrid() {
    this.orderDataGrid.instance.refresh();
  }

  protected readonly faShoppingBasket = faShoppingBasket;
  protected readonly faTrashAlt = faTrashAlt;
}






