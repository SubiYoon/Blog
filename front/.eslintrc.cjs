// .eslintrc.cjs
require('@rushstack/eslint-patch/modern-module-resolution')

module.exports = {
    root: true,
    env: {
        browser: true,
        es2021: true,
        node: true,
    },
    parser: '@typescript-eslint/parser',
    parserOptions: {
        ecmaVersion: 'latest',
        sourceType: 'module',
        ecmaFeatures: {
            jsx: true,
        },
    },
    extends: [
        'eslint:recommended',
        'plugin:react/recommended',
        'plugin:react-hooks/recommended',
        'plugin:@typescript-eslint/recommended',
        'prettier',
    ],
    plugins: ['react', 'react-hooks', '@typescript-eslint'],
    ignorePatterns: ['*.md', 'build/', 'node_modules/'],
    rules: {
        'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
        'react/prop-types': 'off', // TypeScript 사용 시 불필요
        '@typescript-eslint/no-unused-vars': ['warn'],
        'react/react-in-jsx-scope': 'off', // React 17 이상은 필요 없음
    },
    settings: {
        react: {
            version: 'detect',
        },
    },
}
