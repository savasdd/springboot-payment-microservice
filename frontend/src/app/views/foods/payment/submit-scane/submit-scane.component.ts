import { Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { GenericService } from '../../../../services/generic.service';
import { MessageService } from '../../../../services/message.service';

@Component({
  selector: 'app-submit-scane',
  templateUrl: './submit-scane.component.html',
  styleUrls: ['./submit-scane.component.scss']
})
export class SubmitScaneComponent implements OnInit, OnChanges {
  @Input() paymentData: any;
  submit: Submit = new Submit(null, null);
  orderService: GenericService;
  editorOptions = { disabled: true };

  constructor(private service: GenericService, private notify: MessageService,) {
    this.orderService = this.service.instance('order');
  }

  ngOnInit(): void {

  }

  ngOnChanges(changes: SimpleChanges): void {
    this.submit.orderNo = this.paymentData.orderNo;
  }
}


export class Submit {
  orderNo: any = null;
  securityCode: any = null;

  constructor(orderNo: any, securityCode: any) {
    this.orderNo = orderNo;
    this.securityCode = securityCode;
  }
}