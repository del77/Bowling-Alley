import { expect } from "chai";
import UserListPage from "../pageobjects/userList.page";
import LoginPage from "../pageobjects/login.page";

const TEST_USER_MAIL = "testLockUnlock@mail.com";

const TEST_USER = "testLockUnlock";
const TEST_USER_PASSWORD = "testLockUnlock";
const SUCCESS_LOCK_MESSAGE = `Zablokowano użytkownika`;
const SUCCESS_UNLOCK_MESSAGE = `Odblokowano użytkownika`;
const LOGIN_ERROR = `Login failed`;

describe("User list", () => {
  describe("Success scenarios", () => {
    beforeEach(function() {
      UserListPage.loginAsAdmin();
      UserListPage.open("accounts");
    });

    it("should lock unlocked user", () => {
      UserListPage.lockUser(TEST_USER_MAIL);
      expect(UserListPage.status.getText()).to.equal(SUCCESS_LOCK_MESSAGE);
    });

    it("should not be able to log in with locked account", () => {
      UserListPage.logout();
      UserListPage.loginNoCheck(TEST_USER, TEST_USER_PASSWORD);
      expect(LoginPage.status.getText()).to.equal(LOGIN_ERROR);
    });

    it("should unlock locked user", () => {
      UserListPage.unlockUser(TEST_USER_MAIL);
      expect(UserListPage.status.getText()).to.equal(SUCCESS_UNLOCK_MESSAGE);
    });
  });
});
