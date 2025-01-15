import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from "@angular/router";
import { GenericService } from "../../../../services/generic.service";
import { Location } from '@angular/common';
import { MessageService } from '../../../../services/message.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit {
  dataSource: any = {};
  propertyDataSource: any = [];
  totalPrice: number = 0;
  stockService: GenericService;
  basketService: GenericService;
  stateData: any;
  detailData: any;
  quantity: number = 0;
  tabList: any[] = [
    { id: 1, name: "Ürün", key: 'property' },
    { id: 2, name: "Yorumlar", key: 'comment' },
  ];


  constructor(public service: GenericService,
    private notify: MessageService,
    private router: Router,
    private location: Location,
    private cd: ChangeDetectorRef) {
    this.stockService = this.service.instance('payment/stocks');
    this.basketService = this.service.instance('payment/stocks/basket');
  }

  ngOnInit(): void {
    this.stateData = this.location.getState();
    this.loadDetail(this.stateData.data.id);
  }


  loadDetail(id: any) {
    this.stockService.findOne(id).then((response: any) => {
      this.detailData = response.data;

      if (response.data.propertyList.length > 0) {
        response.data.propertyList.map((m) => {
          this.propertyDataSource.push(m.property);
        });
      }
    });
  }


  addBasket() {
    const basket = new Basket(this.quantity, this.stateData.data.id);
    this.basketService.customPost('save', basket).then((response: any) => {
      if (response.status == 200) {
        this.notify.success("Ürün Sepete Eklendi!")
      } else {
        this.notify.error(response.data)
      }
    });
  }

  setValueQuantity(event: any) {
    this.quantity = event.value;
  }


}


export class Basket {
  type: any = 'NEW';
  quantity: any = 0;
  stock: any = { 'id': null };

  constructor(quantity: any, stockId: any) {
    this.quantity = quantity;
    this.stock.id = stockId;
  }
}