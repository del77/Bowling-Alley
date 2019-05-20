import { expect } from "chai";
import ChangeOtherUserPasswordPage from "../pageobjects/changeOtherUserPassword.page";
import UserList from "../pageobjects/userList.page";
import UserDetails from "../pageobjects/userDetails.page";
import { callTimeout, baseUrl } from "../constants";

const OLD_PASSWORD = "testEmployee";
const NEW_PASSWORD = "testNewEmployee";
const OTHER_PASSWORD = "testOldClient";
const TEST_USER_MAIL = "testEmployee@mail.com";
//TODO: change all error messages to Polish after error internationalization
const PASSWORD_DONT_MATCH = "Passwords don't match.";

const EMPLOYEE_USERNAME = "testEmployee";
const SUCCESS_MESSAGE = "Edycja konta użytkownika zakończyła się sukcesem.";

const _changePassword = (newPassword, confirmPassword) => {
  ChangeOtherUserPasswordPage.newPassword.setValue(newPassword);
  ChangeOtherUserPasswordPage.confirmNewPassword.setValue(confirmPassword);
  ChangeOtherUserPasswordPage.submitBtn.click();
  browser.acceptAlert();
};

describe("Change other user password form:", () => {
  describe("Error scenarios:", () => {
    before(function() {
      ChangeOtherUserPasswordPage.loginAsAdmin();
    });

    beforeEach(function() {
      ChangeOtherUserPasswordPage.open("accounts");
      UserList.editUser(TEST_USER_MAIL);
      UserDetails.editPasswordBtn.click();
    });

    it("should show error when new passwords don't match", () => {
      _changePassword(NEW_PASSWORD, OTHER_PASSWORD);
      expect(ChangeOtherUserPasswordPage.error.getText()).to.equal(PASSWORD_DONT_MATCH);
    });
  });

  describe("Success scenarios:", () => {
    before(function() {
      ChangeOtherUserPasswordPage.loginAsAdmin();
    });

    beforeEach(function() {
      ChangeOtherUserPasswordPage.open("accounts");
      UserList.editUser(TEST_USER_MAIL);
      UserDetails.editPasswordBtn.click();
    });

    it("should change password successfuly", () => {
      _changePassword(NEW_PASSWORD, NEW_PASSWORD);

      browser.waitUntil(
        () => {
          return browser.getUrl() == baseUrl + "accounts/success";
        },
        callTimeout,
        `Expected to redirect to '${baseUrl}accounts/success' after submit`
      );

      expect(ChangeOtherUserPasswordPage.success.getText()).to.equal(SUCCESS_MESSAGE);
    });

    it("should be able to login with new password", () => {
      ChangeOtherUserPasswordPage.logout();
      ChangeOtherUserPasswordPage.login(EMPLOYEE_USERNAME, NEW_PASSWORD);
      browser.waitUntil(
        () => {
          return browser.getUrl() === baseUrl;
        },
        callTimeout,
        `Login failed. Expected to navigate to landing page " + ${baseUrl}`
      );

      // login back to admin to restore password
      ChangeOtherUserPasswordPage.logout();
      ChangeOtherUserPasswordPage.loginAsAdmin();
    });

    it("should change password back", () => {
      _changePassword(OLD_PASSWORD, OLD_PASSWORD);

      browser.waitUntil(
        () => {
          return browser.getUrl() == baseUrl + "accounts/success";
        },
        callTimeout,
        `Expected to redirect to '${baseUrl}accounts/success' after submit`
      );

      expect(ChangeOtherUserPasswordPage.success.getText()).to.equal(SUCCESS_MESSAGE);
    });
  });
});
