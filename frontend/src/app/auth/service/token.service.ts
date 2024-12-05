import { Injectable } from "@angular/core";
import { SessionStorageService } from "angular-web-storage";
import { Router } from "@angular/router";
import { jwtDecode } from "jwt-decode";


const TOKEN_KEY = 'service-token';
const REFRESH_TOKEN_KEY = 'service-refresh-token';
const USER_USERNAME = 'service-user';
const USER_FULLANME = 'service-user-fullname';
const USER_ID = 'service-user-id';
const ROL_KEY = 'service-rol';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  constructor(
    private sessionStorage: SessionStorageService,
    private router: Router
  ) {
  }


  public saveToken(token: any): void {
    this.sessionStorage.remove(TOKEN_KEY);
    this.sessionStorage.set(TOKEN_KEY, token);
    this.saveUser(token);
  }

  public getToken(): any {
    return this.sessionStorage.get(TOKEN_KEY);
  }

  public saveRefreshToken(token: any): void {
    this.sessionStorage.remove(REFRESH_TOKEN_KEY);
    this.sessionStorage.set(REFRESH_TOKEN_KEY, token);
  }

  public getRefreshToken(): any {
    return this.sessionStorage.get(REFRESH_TOKEN_KEY);
  }

  public saveUser(token: any): void {
    this.removeUser();

    const decode = this.decodeToken(token);
    this.sessionStorage.set(USER_ID, decode.id);
    this.sessionStorage.set(USER_USERNAME, decode.sub);
    this.sessionStorage.set(USER_FULLANME, decode.firstName + ' ' + decode.lastName);
  }

  public saveRol(rol: any): void {
    this.sessionStorage.remove(ROL_KEY);
    this.sessionStorage.set(ROL_KEY, rol);
  }

  public getUser(): string {
    return this.sessionStorage.get(USER_USERNAME);
  }

  public getUserFullName(): string {
    return this.sessionStorage.get(USER_FULLANME);
  }

  public getUserId(): string {
    return this.sessionStorage.get(USER_ID);
  }


  public getRol(): any {
    return this.sessionStorage.get(ROL_KEY);
  }

  public removeToken(): void {
    this.sessionStorage.remove(TOKEN_KEY);
  }

  public removeUser(): void {
    this.sessionStorage.remove(USER_USERNAME);
    this.sessionStorage.remove(USER_FULLANME);
    this.sessionStorage.remove(USER_ID);
  }

  public removeRol(): void {
    this.sessionStorage.remove(ROL_KEY);
  }

  signOut(): void {
    window.sessionStorage.clear();
    this.removeToken();
    this.removeUser();
    this.removeRol();
    this.router.navigate(['/login']);
  }


  decodeToken(token: string): any {
    const decoded = jwtDecode(token);
    return decoded;
  }

}
