import Page from "./page";

class LoginPage extends Page {
  get username() {
    return $("#j_username");
  }

  get password() {
    return $("#j_password");
  }

  get submitBtn() {
    return $("form > button[type='submit']");
  }

  get status() {
    return $("div.alert-danger > p.display-2");
  }

  get headerMenu() {
    return $("#user-dropdown");
  }

  get roles() {
    return $$("#user-dropdown	~ div  span.badge");
  }

  open() {
    super.open("login");
  }

  submit() {
    this.submitBtn.click();
  }
}

export default new LoginPage();
