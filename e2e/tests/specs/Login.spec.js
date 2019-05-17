import { expect } from "chai";
import LoginPage from "../pageobjects/login.page";
import { baseUrl, callTimeout } from "../constants";

describe("login form", () => {
  const roleAdmin = "Admin";
  const roleEmployee = "Pracownik";
  const roleClient = "Klient";

  beforeEach(function() {
    LoginPage.open();
  });

  it("should deny access with wrong credentials", () => {
    const loginFailedMsg = "Nieudane logowanie";
    LoginPage.username.setValue("foo");
    LoginPage.password.setValue("bar");
    LoginPage.submit();

    browser.waitUntil(
      () => {
        return LoginPage.status.getText() === loginFailedMsg;
      },
      callTimeout,
      `expected error message: + ${loginFailedMsg}.`
    );
  });

  it("should allow access with correct creds", () => {
    LoginPage.username.setValue("admin");
    LoginPage.password.setValue("admin");
    LoginPage.submit();

    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl;
      },
      callTimeout,
      `expected to navigate to landing page: ${baseUrl}`
    );

    LoginPage.headerMenu.click();
  });

  it("should login as admin", () => {
    LoginPage.username.setValue("testAdmin");
    LoginPage.password.setValue("testAdmin");
    LoginPage.submit();
    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl;
      },
      callTimeout,
      `expected to navigate to landing page: ${baseUrl}`
    );

    LoginPage.headerMenu.click();
    const role = LoginPage.roles.pop();
    expect(role.getText()).to.equal(roleAdmin);
  });

  it("should login as client", () => {
    LoginPage.username.setValue("testClient");
    LoginPage.password.setValue("testClient");
    LoginPage.submit();
    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl;
      },
      callTimeout,
      `expected to navigate to landing page: ${baseUrl}`
    );

    LoginPage.headerMenu.click();
    const role = LoginPage.roles.pop();
    expect(role.getText()).to.equal(roleClient);
  });

  it("should login as employee", () => {
    LoginPage.username.setValue("testEmployee");
    LoginPage.password.setValue("testEmployee");
    LoginPage.submit();
    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl;
      },
      callTimeout,
      `expected to navigate to landing page: ${baseUrl}`
    );

    LoginPage.headerMenu.click();
    const role = LoginPage.roles.pop();
    expect(role.getText()).to.equal(roleEmployee);
  });

  it("should login as client, employee and admin", () => {
    LoginPage.username.setValue("testAllRoles");
    LoginPage.password.setValue("testAllRoles");
    LoginPage.submit();
    browser.waitUntil(
      () => {
        return browser.getUrl() === baseUrl;
      },
      callTimeout,
      `expected to navigate to landing page: ${baseUrl}`
    );

    LoginPage.headerMenu.click();
    const roles = LoginPage.roles.map(role => role.getText());
    expect(roles).to.have.members([roleClient, roleEmployee, roleAdmin]);
  });
});
