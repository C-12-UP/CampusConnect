const page = document.querySelector('.page');
const cord = document.getElementById('pullCord');

function setLamp(isOn) {
  page.classList.toggle('off', !isOn);
  cord.setAttribute('aria-pressed', String(isOn));
}

// match the cord's state to whatever the page loaded with
setLamp(!page.classList.contains('off'));

cord.addEventListener('click', () => {
  const turningOn = page.classList.contains('off');
  setLamp(turningOn);

  cord.classList.add('pulling');
  setTimeout(() => cord.classList.remove('pulling'), 220);
});
const passwordInput = document.getElementById('password');
const showBtn = document.getElementById('showPassword');

showBtn.addEventListener('click', () => {
  const isHidden = passwordInput.type === 'password';

  passwordInput.type = isHidden ? 'text' : 'password';
  showBtn.textContent = isHidden ? 'Hide' : 'Show';
  showBtn.setAttribute('aria-pressed', String(isHidden));
});
const loginForm = document.getElementById('loginForm');
const loginButton = document.getElementById('loginButton');
const buttonText = loginButton.querySelector('.button-text');

loginForm.addEventListener('submit', () => {
  loginButton.classList.add('loading');
  loginButton.disabled = true;
  buttonText.textContent = 'Signing in...';
});

// if the browser restores the page via Back, undo the loading state
window.addEventListener('pageshow', () => {
  loginButton.classList.remove('loading');
  loginButton.disabled = false;
  buttonText.textContent = 'Sign in';
});
const lamp = document.querySelector('.lamp');
const visual = document.querySelector('.visual');
const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)');

visual.addEventListener('mousemove', (e) => {
  if (reduceMotion.matches) return;

  const box = visual.getBoundingClientRect();
  const x = (e.clientX - box.left) / box.width - 0.5;   // -0.5 (left edge) to +0.5 (right edge)
  const y = (e.clientY - box.top) / box.height - 0.5;   // -0.5 (top edge) to +0.5 (bottom edge)

  lamp.style.setProperty('--ry', (x * 12) + 'deg');
  lamp.style.setProperty('--rx', (-y * 12) + 'deg');
});

visual.addEventListener('mouseleave', () => {
  lamp.style.setProperty('--rx', '0deg');
  lamp.style.setProperty('--ry', '0deg');
});