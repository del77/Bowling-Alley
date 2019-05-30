import Page from "./page";

class CreateAccountPage extends Page {
  get username() {
    return $("#login");
  }

  get confirmPassword() {
    return $("#confirmPassword");
  }

  get password() {
    return $("#password");
  }

  get firstName() {
    return $("#firstName");
  }

  get lastName() {
    return $("#lastName");
  }
  get email() {
    return $("#email");
  }

  get phoneNumber() {
    return $("#phoneNumber");
  }

  get clientSelected() {
    return $("#clientSelected");
  }

  get employeeSelected() {
    return $("#employeeSelected");
  }

  get adminSelected() {
    return $("#adminSelected");
  }

  get submitBtn() {
    return $("button[type='submit']");
  }

  get error() {
    return $("div.alert-danger > div");
  }

  get success() {
    return $("div.alert-success > div");
  }
}

export default new CreateAccountPage();
