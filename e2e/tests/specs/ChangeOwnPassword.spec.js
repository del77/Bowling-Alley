import { expect } from "chai";
import ChangeOwnPasswordPage from "../pageobjects/changeOwnPassword.page";
import { callTimeout, baseUrl } from "../constants";

describe("Change own password form:", () => {
  describe("Error scenarios:", () => {
    const oldPassword = "testClient";
    const newPassword = "testNewClient";
    const otherPassword = "testOldClient";
    const shortPassword = "test";
    //TODO: change errors to Polish after error internationalization
    const passwordsDontMatch = "Passwords don't match.";
    const passwordIsSameAsOld = "New and current password must be different.";
    const passwordTooShort = "Hasło musi mieć conajmniej 8 znaków.";
    const incorrectPassword = "Current password is incorrect.";

    before(function() {
      ChangeOwnPasswordPage.loginAsClient();
    });

    beforeEach(function() {
      ChangeOwnPasswordPage.open("account/edit-password");
    });

    it("should show error when old password is incorrect", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(otherPassword);
      ChangeOwnPasswordPage.newPassword.setValue(newPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(newPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      const errorMessages = ChangeOwnPasswordPage.errors.map(error =>
        error.getText()
      );
      expect(errorMessages).to.include(incorrectPassword);
    });

    it("should show error when new passwords don't match", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.newPassword.setValue(newPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(otherPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      const errorMessages = ChangeOwnPasswordPage.errors.map(error =>
        error.getText()
      );
      expect(errorMessages).to.include(passwordsDontMatch);
    });

    it("should show error when new password is the same as old password", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.newPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      const errorMessages = ChangeOwnPasswordPage.errors.map(error =>
        error.getText()
      );
      expect(errorMessages).to.include(passwordIsSameAsOld);
    });

    it("should show error when new password is too short", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.newPassword.setValue(shortPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(shortPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      const errorMessages = ChangeOwnPasswordPage.errors.map(error =>
        error.getText()
      );
      expect(errorMessages).to.include(passwordTooShort);
    });

    it("should show errors when new password is too short and doesn't match confirmation password", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.newPassword.setValue(shortPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(newPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      const errorMessages = ChangeOwnPasswordPage.errors.map(error =>
        error.getText()
      );
      expect(errorMessages).to.deep.include.members([
        passwordTooShort,
        passwordsDontMatch
      ]);
    });

    it("should show all errors when new password is too short, doesn't match confirmation password and is same as old", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(shortPassword);
      ChangeOwnPasswordPage.newPassword.setValue(shortPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(newPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      const errorMessages = ChangeOwnPasswordPage.errors.map(error =>
        error.getText()
      );
      expect(errorMessages).to.deep.include.members([
        passwordIsSameAsOld,
        passwordsDontMatch,
        passwordTooShort
      ]);
    });
  });

  describe("Success scenarios:", () => {
    before(function() {
      ChangeOwnPasswordPage.loginAsClient();
    });

    beforeEach(function() {
      ChangeOwnPasswordPage.open("account/edit-password");
    });

    const clientUsername = "testClient";
    const oldPassword = "testClient";
    const newPassword = "testNewClient";
    const successMessage = "Password has been changed.";

    it("should change password successfuly", () => {
      ChangeOwnPasswordPage.currentPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.newPassword.setValue(newPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(newPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

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

      ChangeOwnPasswordPage.currentPassword.setValue(newPassword);
      ChangeOwnPasswordPage.newPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.confirmNewPassword.setValue(oldPassword);
      ChangeOwnPasswordPage.submitBtn.click();
      browser.acceptAlert();

      expect(ChangeOwnPasswordPage.success.getText()).to.equal(successMessage);
    });
  });
});
