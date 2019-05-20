import Page from "./page";

class UserDetailsPage {
  get editPasswordBtn() {
    return $("div.col-2 a[type='button']");
  }

  get details() {
    return $$("li.list-group-item p");
  }
}

export default new UserDetailsPage();
