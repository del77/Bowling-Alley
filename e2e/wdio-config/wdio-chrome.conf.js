const { baseUrl } = require("../tests/constants.js");
const chromedriver = require("chromedriver");
exports.config = {
  path: "/",
  baseUrl: baseUrl,
  port: 9515,
  runner: "local",
  specs: ["./tests/specs/**/*.spec.js"],

  exclude: ["./tests/specs/ChangeOwnPassword.spec.js"],

  maxInstances: 10,

  capabilities: [
    {
      maxInstances: 5,
      browserName: "chrome",
      // Uncomment to run tests withiout opening browser window
      // "goog:chromeOptions": {
      //   args: ["--headless", "--disable-gpu"]
      // }
      acceptInsecureCerts: true
    }
  ],

  logLevel: "error", // Level of logging verbosity: silent | verbose | command | data | result | error

  // If you only want to run your tests until a specific amount of tests have failed use bail (default is 0 - don't bail, run all tests).
  bail: 0,

  waitforTimeout: 10000,

  connectionRetryTimeout: 90000,

  connectionRetryCount: 3,

  framework: "mocha",

  reporters: ["spec"],
  mochaOpts: {
    ui: "bdd",
    timeout: 60000,
    compilers: ["js:@babel/register"]
  },

  onPrepare: function() {
    let args = [
      // optional arguments
    ];
    let returnPromise = true;
    chromedriver.start(args, returnPromise).then(() => {
      console.log("chromedriver is ready");
    });
  },

  before: function() {
    require("@babel/register");
    let chai = require("chai");
    global.expect = chai.expect;
    chai.Should();
  },

  onComplete: function() {
    chromedriver.stop();
  }
};
