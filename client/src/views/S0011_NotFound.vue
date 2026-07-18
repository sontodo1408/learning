<script setup>
import { useI18n } from 'vue-i18n';
import { ROUTER_NAME } from '@/helpers/const';

// 1) =============== INITIALIZATION   ===============
const { t } = useI18n();

// 2) =============== VARIABLE REF     ===============
// 3) =============== METHOD/FUNCTION  ===============
// 4) =============== VUE JS LIFECYCLE ===============
</script>

<template>
  <div class="page-404">
    <div class="container">
      <div class="image-box">
        <h1>{{ t('S0011.label.code') }}</h1>
      </div>

      <div class="content">
        <h2>{{ t('S0011.label.title') }}</h2>
        <p>{{ t('S0011.label.description') }}</p>

        <RouterLink :to="{ name: ROUTER_NAME.USER_HOME }" class="home-btn">
          {{ t('S0011.btn.goHome') }}
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
@import url('https://fonts.googleapis.com/css?family=Arvo');

.page-404 {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fff;
  font-family: 'Arvo', serif;
  padding: 30px 20px;
}

.container {
  width: 100%;
  max-width: 900px;
  text-align: center;
}

.image-box {
  height: 400px;
  background-image: url('https://cdn.dribbble.com/users/285475/screenshots/2083086/dribbble_1.gif');
  background-repeat: no-repeat;
  background-position: center;
  background-size: contain;

  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.image-box h1 {
  font-size: 90px;
  margin-top: 20px;
  color: #222;
}

.content {
  margin-top: -50px;
}

.content h2 {
  font-size: 38px;
  margin-bottom: 15px;
  color: #333;
}

.content p {
  font-size: 18px;
  color: #666;
  margin-bottom: 30px;
}

.home-btn {
  display: inline-block;
  padding: 12px 28px;
  background: $lime-1;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  transition: 0.25s;
  font-size: 17px;
}

.home-btn:hover {
  background: darken($lime-1, 8%);
  transform: translateY(-2px);
}

/* Responsive */

@media (max-width: 768px) {
  .image-box {
    height: 300px;
  }

  .image-box h1 {
    font-size: 70px;
  }

  .content {
    margin-top: -30px;
  }

  .content h2 {
    font-size: 28px;
  }

  .content p {
    font-size: 16px;
  }
}
</style>
