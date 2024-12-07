import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GenericService } from 'src/app/services/generic.service';
import { DxFormComponent } from "devextreme-angular";
import notify from "devextreme/ui/notify";

@Component({
  selector: 'app-file',
  templateUrl: './file.component.html',
  styleUrls: ['./file.component.scss']
})
export class FileComponent implements OnInit {
  @Input() path: any;
  @Input() params: Array<{ 'key': any, 'value': any }> = [];

  fileModel: FileModel = new FileModel();
  @ViewChild(DxFormComponent, { static: false }) form: any = DxFormComponent;
  fileService: GenericService;

  constructor(public service: GenericService) {
  }

  ngOnInit(): void {
    this.fileService = this.service.instance(this.path);
  }


  onValueChanged(e: any) {
    const file = e.value[0];
    const fileReader = new FileReader();
    fileReader.onload = () => {
      this.fileModel.fileData = fileReader.result as ArrayBuffer;
      this.fileModel.fileBlob = new Blob([fileReader.result as ArrayBuffer], { type: file.type });
      this.fileModel.file = file;
    }
    fileReader.readAsDataURL(file);
  }

  uploadFile() {
    const formValid = this.form.instance.validate();
    if (formValid && this.fileModel.fileData != null) {
      const formData = new FormData();
      formData.append('file', this.fileModel.file);

      if (this.params != undefined && this.params.length > 0) {
        this.params.map((m) => {
          formData.append(m.key, m.value);
        });
      }

      this.fileService.customPost("", formData).then((response: any) => {
        notify({ message: response.data});
      });

    }
  }
}


export class FileModel {
  userId?: string = '88';
  fileData: any;
  fileBlob: any;
  file: any;

  constructor() {
  }
}