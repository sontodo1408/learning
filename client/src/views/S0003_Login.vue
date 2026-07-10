<script setup>
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRouter } from 'vue-router';
import { ROUTER_NAME } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();
const router = useRouter();

// 2) =============== VARIABLE REF     ===============
/** Email input value */
const email = ref('');
/** Password input value */
const password = ref('');
/** Whether the password field is shown in plain text */
const showPassword = ref(false);

// 3) =============== METHOD/FUNCTION  ===============
/** Placeholder submit handler: no auth backend yet, so just enter the app */
const submit = () => {
  router.push({ name: ROUTER_NAME.HOME });
};

// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <q-page class="login-page flex flex-center">
    <div class="login-card">
      <div class="login-card__corner"></div>

      <p class="login-card__greeting">{{ t('S0003.label.greeting') }}</p>
      <h1 class="login-card__brand">SonToDo</h1>
      <p class="login-card__title">{{ t('S0003.label.title') }}</p>

      <form class="login-card__form" @submit.prevent="submit">
        <q-input v-model="email" type="email" outlined dense bg-color="white" :label="t('S0003.label.emailPlaceholder')"
          class="login-card__field">
          <template v-slot:prepend>
            <q-icon name="mail_outline" />
          </template>
        </q-input>

        <q-input v-model="password" :type="showPassword ? 'text' : 'password'" outlined dense bg-color="white"
          :label="t('S0003.label.passwordPlaceholder')" class="login-card__field">
          <template v-slot:prepend>
            <q-icon name="lock_outline" />
          </template>
          <template v-slot:append>
            <q-icon :name="showPassword ? 'visibility_off' : 'visibility'" class="tw:cursor-pointer"
              @click="showPassword = !showPassword" />
          </template>
        </q-input>

        <a class="login-card__forgot" href="#" @click.prevent>{{ t('S0003.label.forgotPassword') }}</a>

        <CBtn type="submit" no-caps class="login-card__submit" :label="t('S0003.btn.submit')" />
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
    font-size: 40px;
    font-weight: 900;
    letter-spacing: -0.02em;
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
