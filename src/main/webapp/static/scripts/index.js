let $forms = document.querySelectorAll('[data-form-confirm]');
let $buttons = document.querySelectorAll('[data-button-confirm]');

function confirmChoice(event) {
  const flag = confirm('Do you really want to submit the form?');
  if (!flag) event.preventDefault();
}

$forms.forEach(function ($form) {
  $form.addEventListener('submit', confirmChoice);
});

$buttons.forEach(function ($button) {
  $button.addEventListener('click', confirmChoice, true);
});