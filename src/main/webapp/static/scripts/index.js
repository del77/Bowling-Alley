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

$(function () {
    $('[data-toggle="popover"]').popover()
});

$('.popover-dismiss').popover({
    trigger: 'focus'
});

$('input.starttimepicker').timepicker({
    autoShow: false,
    timeFormat: 'HH:mm',
    interval: 30,
    minTime: '10:00am',
    maxTime: '10:30pm',
    startTime: '10:00',
    dynamic: false,
    dropdown: true,
    scrollbar: true
});

$('input.endtimepicker').timepicker({
    autoShow: false,
    timeFormat: 'HH:mm',
    format: 'LT',
    interval: 30,
    minTime: '10:30am',
    maxTime: '11:00pm',
    startTime: '10:00',
    dynamic: false,
    dropdown: true,
    scrollbar: true
});

$('input.datepicker').datepicker({
  autoShow: false,
  format: 'dd-mm-yyyy',
  startDate: 'today'
});