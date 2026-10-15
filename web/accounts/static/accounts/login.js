(() => {
  const page = document.querySelector('.page');
  const lamp = document.querySelector('.lamp');
  const visual = document.querySelector('.visual');
  const cord = document.getElementById('pullCord');
  const form = document.getElementById('loginForm');
  const loginButton = document.getElementById('loginButton');
  const showBtn = document.getElementById('showPassword');
  const password = document.getElementById('password');
  const username = document.getElementById('username');

  /* ---------- Lamp: pull the cord to toggle ---------- */
  function syncCord() {
    // aria-pressed is true when the lamp is ON
    cord.setAttribute('aria-pressed', String(!page.classList.contains('off')));
  }
  syncCord();

  cord.addEventListener('click', () => {
    page.classList.toggle('off');
    syncCord();

    // little tug animation
    cord.classList.add('pulling');
    setTimeout(() => cord.classList.remove('pulling'), 220);

    // when the light comes on, put the cursor in the username box (desktop only)
    const lampIsOn = !page.classList.contains('off');
    const hasMouse = window.matchMedia('(pointer: fine)').matches;
    if (lampIsOn && hasMouse) {
      setTimeout(() => username.focus({ preventScroll: true }), 150);
    }
  });

  /* ---------- Lamp tilts slightly toward the mouse ---------- */
  const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  if (!reduceMotion && lamp && visual) {
    visual.addEventListener('mousemove', (e) => {
      const r = visual.getBoundingClientRect();
      const x = (e.clientX - r.left) / r.width - 0.5;   // -0.5 to 0.5
      const y = (e.clientY - r.top) / r.height - 0.5;
      lamp.style.setProperty('--ry', (x * 10).toFixed(2) + 'deg');
      lamp.style.setProperty('--rx', (-y * 8).toFixed(2) + 'deg');
    });

    visual.addEventListener('mouseleave', () => {
      lamp.style.setProperty('--rx', '0deg');
      lamp.style.setProperty('--ry', '0deg');
    });
  }

  /* ---------- Show / hide password ---------- */
  showBtn.addEventListener('click', () => {
    const reveal = password.type === 'password';
    password.type = reveal ? 'text' : 'password';
    showBtn.textContent = reveal ? 'Hide' : 'Show';
    showBtn.setAttribute('aria-pressed', String(reveal));
  });

  /* ---------- Sign-in button: spinner while submitting ---------- */
  form.addEventListener('submit', () => {
    loginButton.classList.add('loading');
    loginButton.disabled = true;
  });

  // if the user comes back with the browser's Back button, reset the button
  window.addEventListener('pageshow', (e) => {
    if (e.persisted) {
      loginButton.classList.remove('loading');
      loginButton.disabled = false;
    }
  });
})();