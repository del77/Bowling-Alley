import { expect } from "chai";
import ChangeOtherUserPasswordPage from "../pageobjects/changeOtherUserPassword.page";
import UserList from "../pageobjects/userList.page";
import UserDetails from "../pageobjects/userDetails.page";
import { callTimeout, baseUrl } from "../constants";
import uuidv4 from "uuid";

const OLD_PASSWORD = "testChangePasswd";
const NEW_PASSWORD = uuidv4().substr(0, 16);
const OTHER_PASSWORD = "testOldClient";
const TEST_USER_MAIL = "testChangePasswd@mail.com";
const PASSWORD_DONT_MATCH = "Podane hasła są różne.";
const PASSWORD_WAS_USED = "Hasło było już używane.";

const USERNAME = "testChangePasswd";
const SUCCESS_MESSAGE = "The user's edition is successful.";

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


    it("should show error when new password was already used", () => {
      _changePassword(OLD_PASSWORD, OLD_PASSWORD);
      expect(ChangeOtherUserPasswordPage.error.getText()).to.equal(PASSWORD_WAS_USED);
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
      expect(ChangeOtherUserPasswordPage.success.getText()).to.equal(SUCCESS_MESSAGE);
    });

    it("should be able to login with new password", () => {
      ChangeOtherUserPasswordPage.logout();
      ChangeOtherUserPasswordPage.login(USERNAME, NEW_PASSWORD);
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
  });
});
