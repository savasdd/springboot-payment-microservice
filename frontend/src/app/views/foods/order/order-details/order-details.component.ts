import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import { DxDataGridComponent } from "devextreme-angular";
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
  totalPrice: number = 0;
  stockService: GenericService;
  stateData: any;

  constructor(public service: GenericService,
    private router: Router,
    private location: Location,
    private cd: ChangeDetectorRef) {
    this.stockService = this.service.instance('payment/stocks/elastic/');
  }

  ngOnInit(): void {
    this.stateData = this.location.getState();
  }

}
