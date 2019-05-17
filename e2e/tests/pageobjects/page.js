import { baseUrl, callTimeout } from "../constants";
export default class Page {
  constructor() {
    const _username = () => browser.$("#j_username");
    const _password = () => browser.$("#j_password");
    const _submitBtn = () => browser.$("form > button[type='submit']");

    this.login = (username, password) => {
      this.open("login");
      _username().setValue(username);
      _password().setValue(password);
      _submitBtn().click();
      this._checkIfLoggedIn();
    };
  }

  open(path) {
    browser.url(path);
  }

  refresh() {
    browser.refresh();
  }

  loginAsClient() {
    this.login("testClient", "testClient");
    this._checkIfLoggedIn();
  }

  loginAsAdmin() {
    this.login("testAdmin", "testAdmin");
    this._checkIfLoggedIn();
  }

  loginAsEmployee() {
    this.login("testEmployee", "testEmployee");
    this._checkIfLoggedIn();
  }

  loginAsGod() {
    this.login("testAllRoles", "testAllRoles");
    this._checkIfLoggedIn();
  }

  logout() {
    $("#user-dropdown").click();
    $("div.dropdown.show > div > a:last-child").click();
  }

  _checkIfLoggedIn() {
    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl;
      },
      callTimeout,
      `Login failed. Expected to navigate to landing page " + ${baseUrl}`
    );
  }
}
