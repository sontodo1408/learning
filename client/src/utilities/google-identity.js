const GSI_SCRIPT_SRC = 'https://accounts.google.com/gsi/client';

// Reused across calls so the script tag is only injected once
let scriptPromise = null;

/** Load the Google Identity Services script once and resolve with the global `google` object */
const loadGoogleIdentityScript = () => {
  if (window.google?.accounts?.id) {
    return Promise.resolve(window.google);
  }

  if (!scriptPromise) {
    scriptPromise = new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = GSI_SCRIPT_SRC;
      script.async = true;
      script.defer = true;
      script.onload = () => resolve(window.google);
      script.onerror = () => reject(new Error('Failed to load Google Identity Services script'));
      document.head.appendChild(script);
    });
  }

  return scriptPromise;
};

/**
 * Shows Google's account chooser and resolves with the resulting Google ID token.
 * Rejects if the user closes the chooser without picking an account.
 */
export const promptGoogleSignIn = async (clientId) => {
  const google = await loadGoogleIdentityScript();

  return new Promise((resolve, reject) => {
    google.accounts.id.initialize({
      client_id: clientId,
      auto_select: false,
      callback: (response) => resolve(response.credential),
    });

    google.accounts.id.prompt((notification) => {
      const wasCancelled = notification.isNotDisplayed()
        || notification.isSkippedMoment()
        || (notification.isDismissedMoment() && notification.getDismissedReason() !== 'credential_returned');

      if (wasCancelled) {
        reject(new Error('Google sign-in was cancelled'));
      }
    });
  });
};
