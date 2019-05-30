/*
TEST EXCLUDED BECAUSE OF RECAPTCHA
*/
import { expect } from "chai";
import ChangeOwnPasswordPage from "../pageobjects/changeOwnPassword.page";
import { callTimeout, baseUrl } from "../constants";
import { extractTextFromElements } from "../helpers";

const _changePassword = (oldPassword, newPassword, confirmPassword) => {
  ChangeOwnPasswordPage.currentPassword.setValue(oldPassword);
  ChangeOwnPasswordPage.newPassword.setValue(newPassword);
  ChangeOwnPasswordPage.confirmNewPassword.setValue(confirmPassword);
  ChangeOwnPasswordPage.submitBtn.click();
  browser.acceptAlert();
};

const OLD_PASSWORD = "testClient";
const NEW_PASSWORD = "testNewClient";
const OTHER_PASSWORD = "testOldClient";

//TODO: change all error messages to Polish after error internationalization
const PASSWORDS_DONT_MATCH_ERROR = "Podane hasła są różne.";
const PASSWORD_IS_SAME_AS_OLD_ERROR = "New and current password must be different.";
const INCORECT_OLD_PASSWORD_ERROR = "Current password is incorrect.";
const PASSWORD_WAS_USED_ERROR = "Haslo bylo uzyte.";

const CLIENT_USERNAME = "testPassword";
const SUCCESS_MESSAGE = "Password has been changed.";

describe("Change own password form:", () => {
  describe("Error scenarios:", () => {
    before(function() {
      ChangeOwnPasswordPage.loginAsClient();
    });

    beforeEach(function() {
      ChangeOwnPasswordPage.open("account/edit-password");
    });

    it("should show error when old password is incorrect", () => {
      _changePassword(OTHER_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);

      const errorMessages = extractTextFromElements(ChangeOwnPasswordPage.errors);

      expect(errorMessages).to.include(INCORECT_OLD_PASSWORD_ERROR);
    });

    it("should show error when new passwords don't match", () => {
      _changePassword(OLD_PASSWORD, NEW_PASSWORD, OTHER_PASSWORD);

      const errorMessages = extractTextFromElements(ChangeOwnPasswordPage.errors);

      expect(errorMessages).to.include(PASSWORDS_DONT_MATCH_ERROR);
    });

    it("should show error when new password is the same as old password", () => {
      _changePassword(OLD_PASSWORD, OLD_PASSWORD, OLD_PASSWORD);

      const errorMessages = extractTextFromElements(ChangeOwnPasswordPage.errors);

      expect(errorMessages).to.include(PASSWORD_IS_SAME_AS_OLD_ERROR);
    });

    it("should show all errors when new password is too short, doesn't match confirmation password and is same as old", () => {
      _changePassword(OTHER_PASSWORD, OTHER_PASSWORD, NEW_PASSWORD);

      const errorMessages = extractTextFromElements(ChangeOwnPasswordPage.errors);

      expect(errorMessages).to.deep.include.members([
        PASSWORD_IS_SAME_AS_OLD_ERROR,
        PASSWORDS_DONT_MATCH_ERROR
      ]);
    });

    it("should show error that password was already used", () => {
      ChangeOwnPasswordPage.login(CLIENT_USERNAME, NEW_PASSWORD);
      ChangeOwnPasswordPage.open("account/edit-password");

      browser.waitUntil(
        () => {
          return browser.getUrl() === baseUrl + "account/edit-password";
        },
        callTimeout,
        `Login failed. Expected to navigate to landing page ${baseUrl}account/edit-password"`
      );

      _changePassword(NEW_PASSWORD, OLD_PASSWORD, OLD_PASSWORD);
      const errorMessages = extractTextFromElements(ChangeOwnPasswordPage.errors);

      expect(errorMessages).to.include(PASSWORD_WAS_USED_ERROR);
    });
  });

  describe("Success scenarios:", () => {
    before(function() {
      ChangeOwnPasswordPage.loginAsClient();
    });

    beforeEach(function() {
      ChangeOwnPasswordPage.open("account/edit-password");
    });

    it("should change password successfuly", () => {
      _changePassword(OLD_PASSWORD, NEW_PASSWORD, NEW_PASSWORD);

      expect(ChangeOwnPasswordPage.success.getText()).to.equal(SUCCESS_MESSAGE);
    });

    it("should be able to login with new password", () => {
      ChangeOwnPasswordPage.logout();
      ChangeOwnPasswordPage.login(CLIENT_USERNAME, NEW_PASSWORD);
      browser.waitUntil(
        () => {
          return browser.getUrl() === baseUrl;
        },
        callTimeout,
        `Login failed. Expected to navigate to landing page " + ${baseUrl}`
      );
    });
  });
});
