import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { GenericService } from '../../../../services/generic.service';
import { MessageService } from '../../../../services/message.service';

@Component({
  selector: 'app-submit-scane',
  templateUrl: './submit-scane.component.html',
  styleUrls: ['./submit-scane.component.scss']
})
export class SubmitScaneComponent implements OnInit, OnChanges {
  @Input() orderData: any;
  @Output() submitEmitter: EventEmitter<any> = new EventEmitter();
  submit: Submit = new Submit(null, null);
  orderService: GenericService;
  editorOptions = { disabled: true };

  constructor(private service: GenericService, private notify: MessageService,) {
    this.orderService = this.service.instance('order');
  }

  ngOnInit(): void {

  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.orderData !== undefined) {
      this.submit.orderNo = this.orderData.orderNo;
    }
  }

  submitOrder() {
    this.orderService.customPost('submit', this.submit).then((response: any) => {
      if (response.status == 200) {
        this.orderService.customPost('complete', this.submit).then((response: any) => {
          if (response.status == 200) {
            this.notify.success("Ödeme Başarıyla Tamamlandı! Ödeme Faturasını Alabilirsiniz.");
            this.submitEmitter.emit(response);
          } else {
            this.notify.error(response.data);
          }
        });

      } else {
        this.notify.error(response.data);
      }
    });
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