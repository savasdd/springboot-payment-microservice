import {Injectable} from "@angular/core";
import {TokenService} from "./token.service";
import {TokenResponse, UserDto} from "../../services/gateway-service-api";
import {GenericService} from "../../services/generic.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  authService: GenericService;

  constructor(
    public service: GenericService,
    private tokenService: TokenService) {
    this.authService = this.service.instance('payment/users/auth');
  }

  login(username: any, password: any) {
    const dto: UserDto = {
      username: username,
      password: password
    };


    return this.authService.customPostPermit('login', dto).then((response: any): any => {
      if (response) {
        this.tokenService.saveToken(response.token);
        this.tokenService.saveRefreshToken(response.refresh_token);
        this.tokenService.saveRol(response.roles);
      }
    });

  }

  refreshToken() {
    const token = this.tokenService.getToken();
    const dto: TokenResponse = {
      access_token: token,
    };

    return this.authService.customPostPermit('getRefreshToken', dto);
  }

}
