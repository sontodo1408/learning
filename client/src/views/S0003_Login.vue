<script setup>
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { ROLE, ROUTER_NAME } from '@/helpers/const';
import authService from '@/services/auth-service';
import { promptGoogleSignIn } from '@/utilities/google-identity';
import logoO from '@/assets/imgs/logo_o.png';
import googleIcon from '@/assets/imgs/google.svg';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const route = useRoute();
const router = useRouter();

// 2) =============== VARIABLE REF     ===============
/** Username input value */
const username = ref('');
/** Password input value */
const password = ref('');
/** Whether the password field is shown in plain text */
const showPassword = ref(false);
/** Whether a login request is in flight (disables the form while awaiting the server) */
const isLoading = ref(false);

// 3) =============== METHOD/FUNCTION  ===============
/**
 * Return to the URL the user was originally headed to before being redirected here
 * (see the router guard's `redirect` query param); otherwise send an admin to the
 * admin dashboard and everyone else to the regular user home.
 */
const redirectAfterLogin = (user) => {
  if (typeof route.query.redirect === 'string') {
    router.push(route.query.redirect);
    return;
  }
  router.push({ name: user.role === ROLE.ADMIN ? ROUTER_NAME.HOME : ROUTER_NAME.USER_HOME });
};

/** Log in via AuthService, then enter the app; a rejected login is already surfaced by RestClient's notice dialog */
const submit = async () => {
  isLoading.value = true;
  try {
    const user = await authService.login(username.value, password.value);
    redirectAfterLogin(user);
  } catch {
    // Nothing else to do: the server's error message was already shown by RestClient
  } finally {
    isLoading.value = false;
  }
};

/** Let the user pick a Google account, then log in via the resulting ID token */
const loginWithGoogle = async () => {
  isLoading.value = true;
  try {
    const idToken = await promptGoogleSignIn(import.meta.env.VITE_GOOGLE_CLIENT_ID);
    const user = await authService.googleLogin(idToken);
    redirectAfterLogin(user);
  } catch {
    // Cancelled chooser or a rejected login already surfaced by RestClient's notice dialog
  } finally {
    isLoading.value = false;
  }
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-page class="login-page flex flex-center">
    <div class="login-card">
      <div class="login-card__corner"></div>

      <img :src="logoO" alt="" class="login-card__logo" />
      <p class="login-card__greeting">{{ t('S0003.label.greeting') }}</p>
      <h1 class="login-card__brand">{{ t('common.app.name') }}</h1>
      <p class="login-card__title">{{ t('S0003.label.title') }}</p>

      <form class="login-card__form" @submit.prevent="submit">
        <q-input v-model="username" type="text" outlined bg-color="white" :disable="isLoading"
          :label="t('S0003.label.usernamePlaceholder')" class="login-card__field">
          <template v-slot:prepend>
            <q-icon name="person_outline" />
          </template>
        </q-input>

        <q-input v-model="password" :type="showPassword ? 'text' : 'password'" outlined bg-color="white"
          :disable="isLoading" :label="t('S0003.label.passwordPlaceholder')" class="login-card__field">
          <template v-slot:prepend>
            <q-icon name="lock_outline" />
          </template>
          <template v-slot:append>
            <q-icon :name="showPassword ? 'visibility_off' : 'visibility'" class="tw:cursor-pointer"
              @click="showPassword = !showPassword" />
          </template>
        </q-input>

        <a class="login-card__forgot" href="#" @click.prevent>{{ t('S0003.label.forgotPassword') }}</a>

        <CBtn type="submit" no-caps class="login-card__submit" :label="t('S0003.btn.submit')" :loading="isLoading"
          :disable="isLoading" />

        <div class="login-card__divider">
          <span>{{ t('S0003.label.orDivider') }}</span>
        </div>

        <CBtn no-caps outline class="login-card__google" :icon="`img:${googleIcon}`"
          :label="t('S0003.btn.loginWithGoogle')" :disable="isLoading" @click="loginWithGoogle" />
      </form>
    </div>
  </q-page>
</template>

<style lang="scss" scoped>
.login-page {
  min-height: 100%;
  background-color: $lime-6;
  background-image: repeating-linear-gradient(to bottom, transparent 0, transparent 27px, rgba($lime-1, 0.08) 28px);
}

.login-card {
  position: relative;
  width: 100%;
  max-width: 380px;
  margin: 24px;
  padding: 48px 32px 32px;
  background-color: $lime-4;
  border-radius: 16px;
  box-shadow: 0 20px 40px -12px rgba($lime-1, 0.35);
  animation: card-rise 0.5s cubic-bezier(0.16, 1, 0.3, 1);

  &__corner {
    position: absolute;
    top: 0;
    right: 0;
    width: 0;
    height: 0;
    border-style: solid;
    border-width: 0 36px 36px 0;
    border-color: transparent $lime-5 transparent transparent;
    border-top-right-radius: 16px;
    filter: drop-shadow(-2px 2px 3px rgba($lime-1, 0.25));
  }

  &__logo {
    display: block;
    width: 56px;
    height: 56px;
    margin-bottom: 12px;
  }

  &__greeting {
    margin: 0;
    font-family: 'Dancing Script', cursive;
    font-size: 26px;
    font-weight: 700;
    color: $lime-1;
    transform: rotate(-2deg);
  }

  &__brand {
    margin: 0;
    font-family: 'Be Vietnam Pro', sans-serif;
    font-size: 26px;
    line-height: 1.2;
    font-weight: 900;
    letter-spacing: -0.01em;
    color: #3a2a22;
  }

  &__title {
    margin: 4px 0 28px;
    font-size: 14px;
    font-weight: 600;
    letter-spacing: 0.08em;
    text-transform: uppercase;
    color: rgba(#3a2a22, 0.55);
  }

  &__form {
    display: flex;
    flex-direction: column;
    gap: 16px;
  }

  &__forgot {
    align-self: flex-end;
    margin-top: -8px;
    font-size: 13px;
    font-weight: 600;
    color: $lime-1;
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }

  &__submit {
    margin-top: 8px;
    height: 44px;
    border-radius: 999px;
    font-weight: 700;
  }

  &__divider {
    display: flex;
    align-items: center;
    margin: 4px 0;
    font-size: 12px;
    font-weight: 600;
    letter-spacing: 0.04em;
    text-transform: uppercase;
    color: rgba(#3a2a22, 0.5);

    &::before,
    &::after {
      flex: 1;
      height: 1px;
      content: '';
      background-color: rgba(#3a2a22, 0.15);
    }

    span {
      padding: 0 12px;
    }
  }

  &__google {
    height: 44px;
    border-radius: 999px;
    font-weight: 700;
    color: #3a2a22;
    background-color: white;

    :deep(.q-btn__content) {
      gap: 10px;
    }
  }
}

@keyframes card-rise {
  from {
    opacity: 0;
    transform: translateY(16px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (prefers-reduced-motion: reduce) {
  .login-card {
    animation: none;
  }
}
</style>
