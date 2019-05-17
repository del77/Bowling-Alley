import { baseUrl, callTimeout } from "../constants";
export default class Page {
  open(path) {
    browser.url(path);
  }

  refresh() {
    browser.refresh();
  }

  loginAsEmployee() {
    this.open("login");
    browser.$("input[name='login']").setValue("employee");
    $("input[name='password']").setValue("pass1");
    $("form button[type='submit']").click();
    this._checkIfLoggedIn();
  }

  loginAsManager() {
    this.open("login");
    browser.$("input[name='login']").setValue("manager");
    $("input[name='password']").setValue("pass2");
    $("form button[type='submit']").click();
    this._checkIfLoggedIn();
  }

  loginAsApprover() {
    this.open("login");
    browser.$("input[name='login']").setValue("approver");
    $("input[name='password']").setValue("pass3");
    $("form button[type='submit']").click();
    this._checkIfLoggedIn();
  }

  loginAsAccountant() {
    this.open("login");
    browser.$("input[name='login']").setValue("accountant");
    $("input[name='password']").setValue("pass4");
    $("form button[type='submit']").click();
    this._checkIfLoggedIn();
  }

  _checkIfLoggedIn() {
    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl + "/";
      },
      callTimeout,
      "Login failed. Expected to navigate to landing page " + baseUrl + "/"
    );
  }
}
