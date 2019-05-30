import CreateAccountPage from "../pageobjects/createAccount.page";
import UserListPage from "../pageobjects/userList.page";
import UserDetailsPage from "../pageobjects/userDetails.page";
import { expect } from "chai";
import { baseUrl, callTimeout } from "../constants";
import { extractTextFromElements } from "../helpers";
import uuidv4 from "uuid";

const LOGIN_EXTSTS_ERROR = "Podany login jest już używany.";
const MAIL_EXISTS_ERROR = "Podany email jest już używany.";
const PASSWORDS_DONT_MATCH = "Podane hasła są różne.";

const CREATE_ACCOUNT_SUCCESS = "Konto zostało utworzone";

const TAKEN_USERNAME = "testEmployee";
const USERNAME = "asef324esfedff";
const TAKEN_MAIL = "testClientowsky@mail.com";
const MAIL = "hashbardzoskomplikowany@mail.com";
const PASSWORD = "testEmployee";
const OTHER_PASSWORD = "tsetMeployee";

const FIRST_NAME = "test_name";
const LAST_NAME = "test_lastname";
const PHONE_NUMBER = "123456789";

describe("Create account page", () => {
  describe("Error scenarios", () => {
    beforeEach(function() {
      CreateAccountPage.loginAsAdmin();
      CreateAccountPage.open("admin/register");
    });

    it("should display login is taken error when provided login is not unique ", () => {
      CreateAccountPage.username.setValue(TAKEN_USERNAME);
      CreateAccountPage.password.setValue(PASSWORD);
      CreateAccountPage.confirmPassword.setValue(PASSWORD);
      CreateAccountPage.firstName.setValue(FIRST_NAME);
      CreateAccountPage.lastName.setValue(LAST_NAME);
      CreateAccountPage.email.setValue(MAIL);
      CreateAccountPage.phoneNumber.setValue(PHONE_NUMBER);
      CreateAccountPage.submitBtn.click();
      browser.acceptAlert();
      expect(CreateAccountPage.error.getText()).to.equal(LOGIN_EXTSTS_ERROR);
    });

    it("should display email is taken error when provided mail is not unique ", () => {
      CreateAccountPage.username.setValue(USERNAME);
      CreateAccountPage.password.setValue(PASSWORD);
      CreateAccountPage.confirmPassword.setValue(PASSWORD);
      CreateAccountPage.firstName.setValue(FIRST_NAME);
      CreateAccountPage.lastName.setValue(LAST_NAME);
      CreateAccountPage.email.setValue(TAKEN_MAIL);
      CreateAccountPage.phoneNumber.setValue(PHONE_NUMBER);
      CreateAccountPage.submitBtn.click();
      browser.acceptAlert();
      expect(CreateAccountPage.error.getText()).to.equal(MAIL_EXISTS_ERROR);
    });
    it("should display passwords don't match error when passwords don't match ", () => {
      CreateAccountPage.username.setValue(USERNAME);
      CreateAccountPage.password.setValue(PASSWORD);
      CreateAccountPage.confirmPassword.setValue(OTHER_PASSWORD);
      CreateAccountPage.firstName.setValue(FIRST_NAME);
      CreateAccountPage.lastName.setValue(LAST_NAME);
      CreateAccountPage.email.setValue(MAIL);
      CreateAccountPage.phoneNumber.setValue(PHONE_NUMBER);
      CreateAccountPage.submitBtn.click();
      browser.acceptAlert();

      expect(CreateAccountPage.error.getText()).to.equal(PASSWORDS_DONT_MATCH);
    });
  });

  describe("Success scenarios", () => {
    const UNIQUE_USERNAME = uuidv4().substr(0, 16);
    const UNIQUE_MAIL = UNIQUE_USERNAME + "@mail.com";
    const UNIQUE_FIRST_NAME = uuidv4().substr(0, 16);
    const UNIQUE_LAST_NAME = uuidv4().substr(0, 16);

    before(function() {
      CreateAccountPage.loginAsAdmin();
      CreateAccountPage.open("admin/register");
    });

    it("should create new account", () => {
      CreateAccountPage.username.setValue(UNIQUE_USERNAME);
      CreateAccountPage.password.setValue(PASSWORD);
      CreateAccountPage.confirmPassword.setValue(PASSWORD);
      CreateAccountPage.firstName.setValue(UNIQUE_FIRST_NAME);
      CreateAccountPage.lastName.setValue(UNIQUE_LAST_NAME);
      CreateAccountPage.email.setValue(UNIQUE_MAIL);
      CreateAccountPage.phoneNumber.setValue(PHONE_NUMBER);
      CreateAccountPage.submitBtn.click();
      browser.acceptAlert();
      browser.waitUntil(
        () => {
          return browser.getUrl() == baseUrl + "admin/register/success";
        },
        callTimeout,
        `Should create new account and redirect to '${baseUrl}admin/register/success'`
      );

      expect(CreateAccountPage.success.getText()).to.equal(CREATE_ACCOUNT_SUCCESS);
    });

    it("created account should exist on user list", () => {
      UserListPage.open("accounts");
      const userElement = UserListPage.user(UNIQUE_MAIL);
      expect(userElement.isExisting()).to.equal(true);
    });

    it("should have correct details", () => {
      UserListPage.open("accounts");
      UserListPage.showUserDetials(UNIQUE_MAIL);
      const details = extractTextFromElements(UserDetailsPage.details);
      expect(details).to.include.members([
        UNIQUE_MAIL,
        UNIQUE_FIRST_NAME,
        UNIQUE_LAST_NAME,
        PHONE_NUMBER
      ]);
    });
  });
});
