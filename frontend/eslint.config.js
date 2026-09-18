import js from '@eslint/js'
import globals from 'globals'
import reactHooks from 'eslint-plugin-react-hooks'
import reactRefresh from 'eslint-plugin-react-refresh'
import { defineConfig, globalIgnores } from 'eslint/config'

export default defineConfig([
  globalIgnores(['dist']),
  {
    files: ['**/*.{js,jsx}'],
    extends: [
      js.configs.recommended,
      reactHooks.configs.flat.recommended,
      reactRefresh.configs.vite,
    ],
    languageOptions: {
      ecmaVersion: 2020,
      globals: globals.browser,
      parserOptions: {
        ecmaVersion: 'latest',
        ecmaFeatures: { jsx: true },
        sourceType: 'module',
      },
    },
    rules: {
      'no-unused-vars': ['error', { varsIgnorePattern: '^[A-Z_]' }],
    },
  },
  {
    // Componentes generados por la CLI de shadcn/ui: exportan a propósito el
    // componente junto a sus helpers de variantes (p. ej. `buttonVariants`),
    // así que no aplica la regla de Fast Refresh que exige un solo export.
    files: ['src/components/ui/**/*.jsx'],
    rules: {
      'react-refresh/only-export-components': 'off',
    },
  },
])
