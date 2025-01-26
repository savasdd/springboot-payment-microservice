import { ChangeDetectorRef, Component, OnInit, ViewChild } from '@angular/core';
import CustomStore from "devextreme/data/custom_store";
import { faShoppingBasket, faTrashAlt } from "@fortawesome/free-solid-svg-icons";
import { DxDataGridComponent, DxDrawerComponent } from "devextreme-angular";
import { MessageService } from "../../../services/message.service";
import { Router } from "@angular/router";
import { UtilService } from "../../../services/util.service";
import { GenericService } from "../../../services/generic.service";
import { DxFormTypes } from 'devextreme-angular/ui/form';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
  preserveWhitespaces: true,
})
export class OrderComponent implements OnInit {
  @ViewChild('orderDataGrid', { static: true }) orderDataGrid: any = DxDataGridComponent;
  labelMode: DxFormTypes.FormLabelMode = 'floating';
  dataSource: any = {};
  dataCategorySource: any = {};
  totalPrice: number = 0;
  stockService: GenericService;
  categoryService: GenericService;
  basketList: Array<{ ID: string, Name: string, Price: number, Image: string }> = [];
  filterModel: FilterModel = new FilterModel();
  yearList: Array<{ year: number }> = [];
  currentYear: any;

  constructor(public service: GenericService,
    private messageService: MessageService,
    private router: Router,
    private cd: ChangeDetectorRef) {
    this.setValueYear.bind(null);
    this.stockService = this.service.instance('payment/stocks/elastic/');
    this.categoryService = this.service.instance('payment/stocks/category');

    this.loadCategory();
    this.loadGrid();
  }

  ngOnInit(): void {
    this.setListYear(2015, 2040);
    this.currentYear = this.yearList.filter(f => f.year == new Date().getFullYear());
    this.filterModel.year = this.currentYear ? this.currentYear[0].year : null;
  }


  loadCategory() {
    this.categoryService.findAll(null).then((response: any) => {
      this.dataCategorySource = response.data;
    });
  }


  loadGrid() {
    this.dataSource = new CustomStore({
      key: 'id',
      load: (loadOptions) => {
        return this.stockService.search(UtilService.setPage(loadOptions), this.filterModel).then((response: any) => {
          return {
            data: response.data,
            totalCount: response.totalCount,
            summary: response.summary,
            groupCount: response.groupCount,
          };
        });
      },

      byKey: (key) => {
        return this.stockService.findOne(key).then((response) => {
          return response;
        });
      },
    });
  }


  callBasket() {
    if (this.totalPrice > 0) {
      this.router.navigate(['home/foods/baskets']);
    }
  }

  calculateBasket(data: any[]) {
    if (data) {
      this.totalPrice = 0;
      data.map((m) => {
        this.totalPrice = this.totalPrice + m.price;
      });
    }
  }


  refreshDataGrid() {
    this.orderDataGrid.instance.refresh();
  }

  onFocusedRowChanged(event: any) {
    if (event) {
      this.router.navigate(['home/foods/orders/details'], { state: { data: event.selectedRowsData[0] } });
    }
  }

  setValueCategory(event: any) {
    this.filterModel.category = event.value.id;
  }
  setValueYear(event: any) {
    this.filterModel.year = event.value.year;
  }

  setValueSearch(event: any) {
    this.filterModel.search = event.value;
  }

  setListYear(start: number, end: number) {
    for (let index = start; index <= end; index++) {
      this.yearList.push({ year: index });
    }
  }

  search() {
    this.refreshDataGrid();
  }

  protected readonly faShoppingBasket = faShoppingBasket;
  protected readonly faTrashAlt = faTrashAlt;
}


export class FilterModel {
  category: any = 0;
  year: any = null;
  search: any = "";

  constructor() {
  }
}



