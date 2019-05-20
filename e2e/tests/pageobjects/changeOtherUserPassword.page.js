import Page from "./page";

class ChangeOtherUserPassword extends Page {
  get newPassword() {
    return $("#new-password");
  }

  get confirmNewPassword() {
    return $("#confirm-new-password");
  }

  get submitBtn() {
    return $("form > button[type='submit']");
  }

  get error() {
    return $("div[role='alert'].alert-danger > div");
  }

  get success() {
    return $("div.alert-success > p");
  }
}

export default new ChangeOtherUserPassword();
