import { Component, OnInit, ViewChild } from '@angular/core';
import { DxDataGridComponent } from "devextreme-angular";
import CustomStore from "devextreme/data/custom_store";
import { UtilService } from "../../../services/util.service";
import { GenericService } from "../../../services/generic.service";
import { faRefresh, faShoppingBasket, faCheck, faCreditCard, faFileDownload } from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  dataSource: any = {};
  dataItemsSource: any = {};
  @ViewChild('dataSourceGrid', { static: true }) dataSourceGrid: any = DxDataGridComponent;
  orderService: GenericService;
  popupVisible = false;
  paymentVisible = false;
  orderData: any;

  constructor(private service: GenericService) {
    this.orderService = this.service.instance('order');
    this.loadGrid();
  }

  ngOnInit(): void {
  }


  refreshDataGrid() {
    this.dataSourceGrid.instance.refresh();
  }

  loadGrid() {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.orderService.pageableLoad(UtilService.setPage(loadOptions)).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },
      byKey: (key) => {
        return this.orderService.findOne(key).then((response: any) => {
          return response;
        });
      }
    });
  }

  onCellPrepared(e) {
    if (e.rowType === "data") {
      if (e.column.dataField === "orderStatus" && e.data.orderStatus == 'NEW') {
        e.cellElement.style.cssText = "color: rgb(225, 39, 39);";
      }
      if (e.column.dataField === "paymentButton") {
        e.cellElement.style.cssText = "color: green;text-align: center;font-size: 1.2rem;";
      }
      if (e.column.dataField === "invoiceButton") {
        e.cellElement.style.cssText = "color: rgb(225, 39, 39);text-align: center;font-size: 1.2rem;";
      }
    }
  }

  openPayment(event: any) {
    this.orderData = event;
    this.dataItemsSource = event.items;
    this.popupVisible = true;
  }

  paymentOrder() {
    this.paymentVisible = true;
  }

  canselOrder() {
    console.log(this.orderData)

  }

  invoiceOrder(event: any) {
    console.log("Invoice")
  }


  protected readonly faShoppingBasket = faShoppingBasket;
  protected readonly faRefresh = faRefresh;
  protected readonly faCheck = faCheck;
  protected readonly faCreditCard = faCreditCard;
  protected readonly faFileDownload = faFileDownload;
}
