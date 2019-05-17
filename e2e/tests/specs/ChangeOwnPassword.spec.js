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

const oldPassword = "testClient";
const newPassword = "testNewClient";
const otherPassword = "testOldClient";
const shortPassword = "test";

describe("Change own password form:", () => {
  describe("Error scenarios:", () => {
    //TODO: change all error messages to Polish after error internationalization
    const passwordsDontMatchError = "Passwords don't match.";
    const passwordIsSameAsOldError =
      "New and current password must be different.";
    const passwordTooShortError = "Hasło musi mieć conajmniej 8 znaków.";
    const incorrectOldPasswordError = "Current password is incorrect.";

    before(function() {
      ChangeOwnPasswordPage.loginAsClient();
    });

    beforeEach(function() {
      ChangeOwnPasswordPage.open("account/edit-password");
    });

    it("should show error when old password is incorrect", () => {
      _changePassword(otherPassword, newPassword, newPassword);

      const errorMessages = extractTextFromElements(
        ChangeOwnPasswordPage.errors
      );

      expect(errorMessages).to.include(incorrectOldPasswordError);
    });

    it("should show error when new passwords don't match", () => {
      _changePassword(oldPassword, newPassword, otherPassword);

      const errorMessages = extractTextFromElements(
        ChangeOwnPasswordPage.errors
      );

      expect(errorMessages).to.include(passwordsDontMatchError);
    });

    it("should show error when new password is the same as old password", () => {
      _changePassword(oldPassword, oldPassword, oldPassword);

      const errorMessages = extractTextFromElements(
        ChangeOwnPasswordPage.errors
      );

      expect(errorMessages).to.include(passwordIsSameAsOldError);
    });

    it("should show error when new password is too short", () => {
      _changePassword(oldPassword, shortPassword, shortPassword);
      const errorMessages = extractTextFromElements(
        ChangeOwnPasswordPage.errors
      );
      expect(errorMessages).to.include(passwordTooShortError);
    });

    it("should show errors when new password is too short and doesn't match confirmation password", () => {
      _changePassword(oldPassword, shortPassword, newPassword);

      const errorMessages = extractTextFromElements(
        ChangeOwnPasswordPage.errors
      );

      expect(errorMessages).to.deep.include.members([
        passwordTooShortError,
        passwordsDontMatchError
      ]);
    });

    it("should show all errors when new password is too short, doesn't match confirmation password and is same as old", () => {
      _changePassword(shortPassword, shortPasswor, newPasswordd);

      const errorMessages = extractTextFromElements(
        ChangeOwnPasswordPage.errors
      );

      expect(errorMessages).to.deep.include.members([
        passwordIsSameAsOldError,
        passwordsDontMatchError,
        passwordTooShortError
      ]);
    });
  });

  describe("Success scenarios:", () => {
    const clientUsername = "testClient";
    const successMessage = "Password has been changed.";

    before(function() {
      ChangeOwnPasswordPage.loginAsClient();
    });

    beforeEach(function() {
      ChangeOwnPasswordPage.open("account/edit-password");
    });

    it("should change password successfuly", () => {
      _changePassword(oldPassword, newPassword, newPassword);

      expect(ChangeOwnPasswordPage.success.getText()).to.equal(successMessage);
    });

    it("should be able to login with new password", () => {
      ChangeOwnPasswordPage.logout();
      ChangeOwnPasswordPage.login(clientUsername, newPassword);
      browser.waitUntil(
        () => {
          return browser.getUrl() === baseUrl;
        },
        callTimeout,
        `Login failed. Expected to navigate to landing page " + ${baseUrl}`
      );
    });

    it("should change password back", () => {
      ChangeOwnPasswordPage.login(clientUsername, newPassword);
      ChangeOwnPasswordPage.open("account/edit-password");

      browser.waitUntil(
        () => {
          return browser.getUrl() === baseUrl + "account/edit-password";
        },
        callTimeout,
        `Login failed. Expected to navigate to landing page ${baseUrl}account/edit-password"`
      );

      _changePassword(newPassword, oldPassword, oldPassword);

      expect(ChangeOwnPasswordPage.success.getText()).to.equal(successMessage);
    });
  });
});
