import { Component, OnInit, ViewChild } from '@angular/core';
import { DxDataGridComponent } from "devextreme-angular";
import CustomStore from "devextreme/data/custom_store";
import { Payment } from "../../../services/payment-service-api";
import { UtilService } from "../../../services/util.service";
import { GenericService } from "../../../services/generic.service";
import StatusEnum = Payment.StatusEnum;

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.scss']
})
export class PaymentComponent implements OnInit {
  dataSource: any = {};
  @ViewChild('dataSourceGrid', { static: true }) dataSourceGrid: any = DxDataGridComponent;
  orderService: GenericService;
  popupVisible = false;

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
        e.cellElement.style.cssText = "color: rgb(225, 39, 39); font-weight: bold";
      }
    }
  }

  submitOrder() {
    console.log("Submit")
  }

  cancelOrder() {
    console.log("Cancel")
  }

  invoiceOrder() {
    console.log("Invoice")
  }

}
