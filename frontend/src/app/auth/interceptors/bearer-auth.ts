import { Injectable } from "@angular/core";
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest
} from "@angular/common/http";
import { SessionStorageService } from "angular-web-storage";
import { TokenService } from "../service/token.service";
import { Observable, tap } from "rxjs";
import { environment } from "../../../environments/environment";
import { Router } from "@angular/router";
import { MessageService } from "../../services/message.service";


@Injectable()
export class BearerAuthInterceptor implements HttpInterceptor {
  constructor(public sessionStorage: SessionStorageService,
    private router: Router, private token: TokenService, private message: MessageService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.token.getToken();
    const isApiUrl = request.url.startsWith(environment.apiUrl);
    if (isApiUrl && token) {

      if (!(request.body instanceof FormData)) {
        request = request.clone({
          headers: request.headers
            .set('X-Api-Version', '1')
            .set('Content-Type', 'application/json')
            .set('Access-Control-Allow-Origin', '*')
            .set('Authorization', 'Bearer ' + token)
        });
        return next.handle(request);
      }

      request = request.clone({
        headers: request.headers
          .set('X-Api-Version', '1')// when sending a FormData Request Body, the content-type is omitted because the FormData would set the content-type itself 
          .set('Access-Control-Allow-Origin', '*')
          .set('Authorization', 'Bearer ' + token)
      });
      return next.handle(request);
    }

    return next.handle(request).pipe(tap(() => {
    },
      (err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status !== 401) {
            return;
          }
          this.message.error('401 Unauthorized Token');
          this.router.navigate(['login']);
        }
      }));
  }

}
