import React from 'react';
import { render, fireEvent } from '@testing-library/react';
import '@testing-library/jest-dom';

import App from '../App'
import LogIn from '../components/LogIn';
import userEvent from '@testing-library/user-event';
// Login component tests

test('Render Login Page', () => {
    // Each element
    const { getByText} = render(<LogIn href="http://testing.com"/>);
    expect(getByText('Welcome to')).toBeInTheDocument();
    expect(getByText('Harmony')).toBeInTheDocument();
    expect(getByText('LOG IN WITH SPOTIFY')).toBeInTheDocument();

    // functional components (login button)
    const button = getByText('LOG IN WITH SPOTIFY');
    expect(button).toBeInTheDocument();
    expect(button).toHaveClass('spotify-login-button');

});

test('Render App', () => {
    const {getByText} = render(<App />)
    expect(getByText('Welcome to')).toBeInTheDocument();
    expect(getByText('Harmony')).toBeInTheDocument();
    expect(getByText('LOG IN WITH SPOTIFY')).toBeInTheDocument();

    // functional components (login button)
    const button = getByText('LOG IN WITH SPOTIFY');
    expect(button).toBeInTheDocument();
    expect(button).toHaveClass('spotify-login-button');
})










