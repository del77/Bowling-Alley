const extractTextFromElements = function(elements) {
  return elements.map(function(element) {
    return element.getText();
  });
};

export { extractTextFromElements };
