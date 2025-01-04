import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { Router } from "@angular/router";
import { GenericService } from "../../../../services/generic.service";
import { Location } from '@angular/common';

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
  commentService: GenericService;
  stateData: any;
  detailData: any;
  quantity: number=0;
  tabList: any[] = [
    { id: 1, name: "Ürün", key: 'property' },
    { id: 2, name: "Yorumlar", key: 'comment' },
  ];


  constructor(public service: GenericService,
    private router: Router,
    private location: Location,
    private cd: ChangeDetectorRef) {
    this.stockService = this.service.instance('payment/stocks');
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
    console.log(this.quantity)
  }

  setValueQuantity(event: any) {
    this.quantity = event.value;
  }


}
