import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { GenericService } from '../../../../services/generic.service';
import { MessageService } from '../../../../services/message.service';

@Component({
  selector: 'app-payment-scane',
  templateUrl: './payment-scane.component.html',
  styleUrls: ['./payment-scane.component.scss']
})
export class PaymentScaneComponent implements OnInit, OnChanges {
  @Input() orderData: any;
  @Output() paymentEmitter: EventEmitter<any> = new EventEmitter();
  payment: Payment = new Payment(null, null, null, null);
  orderService: GenericService;
  popupVisible = false;
  monthList: Array<{}> = [];
  yearList: Array<{}> = [];
  cartMonthEditor: Object;
  cartYearEditor: Object;
  editorOptions = { disabled: true };

  constructor(private service: GenericService, private notify: MessageService,) {
    this.orderService = this.service.instance('order');
  }

  ngOnInit(): void {
    this.setListMonth(1, 12);
    this.setListYear(new Date().getFullYear(), 2030);
    this.cartMonthEditor = { items: this.monthList, searchEnabled: true, value: '' };
    this.cartYearEditor = { items: this.yearList, searchEnabled: true, value: '' };
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.orderData !== undefined) {
      this.payment.orderNo = this.orderData.orderNo;
      this.payment.cartNo = 'SV22 705S 8188 660L 82Q3 1922';
    }
  }

  paymentOrder() {
    this.orderService.customPost('payment', this.payment).then((response: any) => {
      if (response.status == 200) {
        this.notify.success("Ödeme Başarıyla Yapıldı: " + response.data.paymentNo);
        this.paymentEmitter.emit(response);
        this.popupVisible = true;
      } else {
        this.notify.error(response.data);
      }
    });
  }

  setListMonth(start: number, end: number) {
    for (let index = start; index <= end; index++) {
      this.monthList.push(index);
    }
  }

  setListYear(start: number, end: number) {
    for (let index = start; index <= end; index++) {
      this.yearList.push(index);
    }
  }
}


export class Payment {
  orderNo: any = null;
  cartNo: any = null;
  cartExpMonth: any = null;
  cartExpYear: any = null;

  constructor(orderNo: any, cartNo: any, cartExpMonth: any, cartExpYear: any) {
    this.orderNo = orderNo;
    this.cartNo = cartNo;
    this.cartExpMonth = cartExpMonth;
    this.cartExpYear = cartExpYear;
  }
}