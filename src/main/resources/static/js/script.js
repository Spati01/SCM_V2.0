console.log("Script loaded");

// Get current theme from localStorage or default to light
let currentTheme = getTheme();

// Apply theme on page load
document.addEventListener("DOMContentLoaded", () => {
  changePageTheme(currentTheme, "");
});

// Function to toggle theme on button click
function changeTheme() {
  const changeThemeButton = document.querySelector("#theme_change_button");

  changeThemeButton.addEventListener("click", () => {
    let oldTheme = currentTheme;
    console.log("Change theme button clicked");

    // Toggle theme
    currentTheme = currentTheme === "dark" ? "light" : "dark";

    console.log(currentTheme);
    changePageTheme(currentTheme, oldTheme);
  });
}

// Store theme in localStorage
function setTheme(theme) {
  localStorage.setItem("theme", theme);
}

// Retrieve theme from localStorage
function getTheme() {
  let theme = localStorage.getItem("theme");
  return theme ? theme : "light";
}

// Apply theme to the page with smooth animation
function changePageTheme(theme, oldTheme) {
  setTheme(currentTheme); // Update localStorage

  const htmlElement = document.querySelector("html");
  const sunIcon = document.getElementById("sun-icon");
  const moonIcon = document.getElementById("moon-icon");

  // Remove the previous theme if exists
  if (oldTheme) {
    htmlElement.classList.remove(oldTheme);
  }

  // Apply the new theme
  htmlElement.classList.add(theme);

  // **Smooth opacity transition**
  if (theme === "light") {
    sunIcon.style.animation = "fadeIn 0.5s forwards";
    moonIcon.style.animation = "fadeOut 0.5s forwards";
  } else {
    sunIcon.style.animation = "fadeOut 0.5s forwards";
    moonIcon.style.animation = "fadeIn 0.5s forwards";
  }
}

// Initialize theme switching
changeTheme();




  // Reusable counter function
  function animateCounter(id) {
    const counterEl = document.getElementById(id);
    const targetCount = parseInt(counterEl.getAttribute("data-count"), 10);

    if (!isNaN(targetCount)) {
      let current = 0;
      const duration = 1000; // animation time in ms
      const frameRate = 30;
      const totalSteps = Math.ceil(duration / frameRate);
      const increment = targetCount / totalSteps;

      const interval = setInterval(() => {
        current += increment;

        if (current >= targetCount) {
          current = targetCount;
          clearInterval(interval);
        }

        counterEl.textContent = `${Math.floor(current).toLocaleString()}+`;
      }, frameRate);
    } else {
      counterEl.textContent = "Error: Invalid count";
    }
  }

  // Initialize all counters on DOM ready
  document.addEventListener("DOMContentLoaded", () => {
    animateCounter("contactCounter");
    animateCounter("favoriteContactCount");
    animateCounter("unfavoriteContactCount");
  });