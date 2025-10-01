/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}"
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#ff7043'
        },
        slate: {
          DEFAULT: '#475569'
        }
      }
    }
  },
  plugins: [
    require('@tailwindcss/forms')
  ]
};
