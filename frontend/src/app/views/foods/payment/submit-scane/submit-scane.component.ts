import { Component, Input, OnChanges, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { GenericService } from '../../../../services/generic.service';
import { MessageService } from '../../../../services/message.service';

@Component({
  selector: 'app-submit-scane',
  templateUrl: './submit-scane.component.html',
  styleUrls: ['./submit-scane.component.scss']
})
export class SubmitScaneComponent implements OnInit, OnChanges {
  @Input() orderData: any;
  submit: Submit = new Submit(null, null);
  orderService: GenericService;
  editorOptions = { disabled: true};

  constructor(private service: GenericService, private notify: MessageService,) {
    this.orderService = this.service.instance('order');
  }

  ngOnInit(): void {

  }

  ngOnChanges(changes: SimpleChanges): void {
    if(this.orderData!==undefined){
      this.submit.orderNo = this.orderData.orderNo;
      console.log(this.orderData)
    }
  }

  submitOrder() {
    console.log(this.submit)
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