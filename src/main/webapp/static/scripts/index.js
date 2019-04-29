var $forms = document.querySelectorAll('[data-form-confirm]');

$forms.forEach(function ($form) {
  $form.addEventListener('submit', function (event) {
    var flag = confirm('Do you really want to submit the form?');
    if (!flag) event.preventDefault();
  });
});
