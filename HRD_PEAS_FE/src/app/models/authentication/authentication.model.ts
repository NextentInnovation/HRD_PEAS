/**
 * Belépési adatok
 */
export class AuthenticationRequest {
  /**
   * Felhasználónév
   * @readOnly: true
   */
  protected username: string;

  /**
   * Jelszó
   * @readOnly: true
   */
  protected password: string;

  constructor( username: string, password: string ) {
    this.username = username;
    this.password = password;
  }

}
