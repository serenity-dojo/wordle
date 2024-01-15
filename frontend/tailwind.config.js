module.exports = {
    content: ['./src/**/*.{js,jsx,ts,tsx}'],
    darkMode: 'class',
    theme: {
        extend: {
            screens: {
                short: { raw: '(max-height: 650px)' },
                xshort: { raw: '(max-height: 560px)' },
                xxshort: { raw: '(max-height: 490px)' },
            },
            colors: {
                'primary-400': '#699250',
                'primary-500': '#4D6C39',
            }
        },
    },
    plugins: [require('@tailwindcss/forms')],
}