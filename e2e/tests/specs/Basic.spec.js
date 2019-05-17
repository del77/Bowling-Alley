import { ok } from "assert";

describe("Bowling Alley page ", () => {
  it("should have title 'Bowling Alley'", () => {
    browser.url("");
    const title = browser.getTitle();
    ok(title.endsWith("Bowling Alley"));
  });
});
