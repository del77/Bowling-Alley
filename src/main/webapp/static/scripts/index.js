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
    timeFormat: 'HH:mm',
    interval: 30,
    minTime: '10',
    maxTime: '11:00pm',
    startTime: '10:00',
    dynamic: false,
    dropdown: true,
    scrollbar: true
});

$('input.endtimepicker').timepicker({
    timeFormat: 'HH:mm',
    interval: 30,
    minTime: '10:30am',
    maxTime: '11:00pm',
    startTime: '10:00',
    dynamic: false,
    dropdown: true,
    scrollbar: true
});

$('input.datepicker').datepicker({
  autoShow: 'true',
  format: 'dd-mm-yyyy',
  startDate: 'today'
});