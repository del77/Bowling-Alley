import Page from "./page";

class ChangeOwnPasswordPage extends Page {
  get currentPassword() {
    return $("#current-password");
  }

  get newPassword() {
    return $("#new-password");
  }

  get confirmNewPassword() {
    return $("#confirm-new-password");
  }

  get submitBtn() {
    return $("form > button[type='submit']");
  }

  get errors() {
    return $$("div[role='alert'].alert-danger > div");
  }

  get success() {
    return $("div[role='alert'].alert-success > div");
  }
}

export default new ChangeOwnPasswordPage();
