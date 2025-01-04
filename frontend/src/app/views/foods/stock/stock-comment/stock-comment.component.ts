import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GenericService } from '../../../../services/generic.service';
import { DxDataGridComponent } from 'devextreme-angular';
import { UtilService } from '../../../../services/util.service';
import CustomStore from 'devextreme/data/custom_store';

@Component({
  selector: 'app-stock-comment',
  templateUrl: './stock-comment.component.html',
  styleUrls: ['./stock-comment.component.scss']
})
export class StockCommentComponent implements OnInit {
  @Input() stockId: number;
  dataSource: any = {};
  commentService: GenericService;
  @ViewChild('dataSourceGrid', { static: true }) dataSourceGrid: any = DxDataGridComponent;
  events: Array<string> = [];

  constructor(public service: GenericService) {
    this.commentService = this.service.instance('payment/stocks/comment');
  }

  ngOnInit(): void {
    console.log(this.stockId)
    this.loadGrid(this.stockId);
  }


  logEvent(eventName: any) {
    this.events.unshift(eventName);
  }

  refreshDataGrid(e: any) {
    this.dataSourceGrid.instance.refresh();
  }


  loadGrid(stockId: number) {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.commentService.pageableLoad(this.getFilters(stockId, UtilService.setPage(loadOptions))).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },

      byKey: (key) => {
        return this.commentService.findOne(key).then((response) => {
          return response;
        });
      },

      insert: (values) => {
        return this.commentService.save(values).then((response) => {
          return;
        });
      },
      update: (key, values: any) => {
        values.id = key;
        return this.commentService.update(key, values).then((response) => {
          return;
        });
      },
      remove: (key) => {
        return this.commentService.delete(key).then((response) => {
          return;
        });
      }
    });
  }


  getFilters(id: any, loadOptions: any): any {

    if (loadOptions.filter != undefined && loadOptions.filter.length > 0) {
      loadOptions.filter.push('and');
      loadOptions.filter.push(['stock.id', '=', id]);
    } else {
      loadOptions.filter.push(['stock.id', '=', id]);
    }

    return loadOptions;
  }
}
