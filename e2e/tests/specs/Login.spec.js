import { expect } from "chai";
import LoginPage from "../pageobjects/login.page";
import { baseUrl, callTimeout } from "../constants";

const ROLE_ADMIN = "Admin";
const ROLE_EMPLOYEE = "Pracownik";
const ROLE_CLIENT = "Klient";
const LOGIN_FAILED_MSG = "Nieudane logowanie";

describe("Login form", () => {
  describe("Error scenarios", () => {
    beforeEach(function() {
      LoginPage.open();
    });

    it("should deny access with wrong credentials", () => {
      LoginPage.username.setValue("foo");
      LoginPage.password.setValue("bar");
      LoginPage.submit();

      browser.waitUntil(
        () => {
          return LoginPage.status.getText() === LOGIN_FAILED_MSG;
        },
        callTimeout,
        `expected error message: + ${LOGIN_FAILED_MSG}.`
      );
    });
  });

  describe("Success scenarios", () => {
    beforeEach(function() {
      LoginPage.open();
    });

    it("should allow access with correct creds", () => {
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
    });

    it("should login as admin", () => {
      LoginPage.loginAsAdmin();

      LoginPage.headerMenu.click();
      const role = LoginPage.roles.pop();
      expect(role.getText()).to.equal(ROLE_ADMIN);
    });

    it("should login as client", () => {
      LoginPage.loginAsClient();
      LoginPage.headerMenu.click();
      const role = LoginPage.roles.pop();
      expect(role.getText()).to.equal(ROLE_CLIENT);
    });

    it("should login as employee", () => {
      LoginPage.loginAsEmployee();

      LoginPage.headerMenu.click();
      const role = LoginPage.roles.pop();
      expect(role.getText()).to.equal(ROLE_EMPLOYEE);
    });

    it("should login as client, employee and admin", () => {
      LoginPage.loginAsGod();

      LoginPage.headerMenu.click();
      const roles = LoginPage.roles.map(role => role.getText());
      expect(roles).to.have.members([ROLE_CLIENT, ROLE_EMPLOYEE, ROLE_ADMIN]);
    });
  });
});
