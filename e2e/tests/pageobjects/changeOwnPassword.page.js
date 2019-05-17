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
    return $$("div[role='alert'] > div");
  }
}

export default new ChangeOwnPasswordPage();
