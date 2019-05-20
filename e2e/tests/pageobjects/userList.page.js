import Page from "./page";

class UserListPage extends Page {
  editUser(mail) {
    $(`//a[text()='${mail}']/ancestor::div[1]/following-sibling::div/a`).click();
  }
  showUserDetials(mail) {
    $(`//a[text()='${mail}']/ancestor::div[1]/following-sibling::div/a[2]`).click();
  }

  user(mail) {
    return $(`//a[text()='${mail}']`);
  }

  showUserDetials(mail) {
    $(`//a[text()='${mail}']/ancestor::div[1]/following-sibling::div/a[2]`).click();
  }

  lockUser(mail) {
    $(`//a[text()='${mail}']/ancestor::div[2]//label[contains(@class, 'btn-danger')]`).click();
  }

  unlockUser(mail) {
    $(`//a[text()='${mail}']/ancestor::div[2]//label[contains(@class, 'btn-danger')]`).click();
  }

  get status() {
    return $("div.alert-success > div");
  }
}

export default new UserListPage();
