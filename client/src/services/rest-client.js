import axios from 'axios';
import { useAuthStore } from '@/stores/auth-store';
import dialog from '@/utilities/dialog';
import i18n from '@/i18n';

/** Merge query params into an axios request config */
const mergeAxiosConfig = (params, config) => ({ ...(config || {}), params });

/** Show any backend-provided message in the app's notice dialog */
const notifyIfHasMessage = (data) => {
  if (data?.msg) {
    dialog.showMessage(i18n.global.t('common.dialog.notice'), data.msg);
  }
};

/**
 * Thin axios wrapper bound to one backend service path (e.g. "/auth").
 * Attaches the stored JWT to every outgoing request, signs the user out on a
 * 401, surfaces any backend `msg` via the app's notice dialog, and resolves/rejects
 * with the server's `{ code, msg, payload }` envelope (see server's ResponseRoot)
 * so callers decide what to do with a business error.
 */
export default class RestClient {
  #client;

  #servicePath;

  constructor(servicePath) {
    this.#servicePath = servicePath;
    this.#client = this.#createAxiosClient();
  }

  /** Build the axios instance and wire the auth request interceptor */
  #createAxiosClient() {
    const instance = axios.create({
      baseURL: import.meta.env.VITE_API_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    instance.interceptors.request.use(
      (config) => this.#onRequest(config),
      (error) => Promise.reject(error),
    );

    return instance;
  }

  /** Attach the current JWT, if any, to every outgoing request */
  #onRequest(config) {
    const auth = useAuthStore();
    if (config.headers && auth.token) {
      config.headers.Authorization = `Bearer ${auth.token}`;
    }
    return config;
  }

  /** Send the request and unwrap the server's response envelope */
  #request(method, url, data, config) {
    return new Promise((resolve, reject) => {
      const axiosConfig = { ...config, url: `${this.#servicePath}${url}`, method, data };

      this.#client
        .request(axiosConfig)
        .then((response) => {
          const result = response.data;

          notifyIfHasMessage(result);

          // The token was rejected (expired/invalid): clear the session so the UI can react
          if (result.code === 401) {
            useAuthStore().signOut();
          }

          if (result.code !== 0) {
            reject(result);
            return;
          }

          resolve(result);
        })
        .catch((error) => {
          // Prefer the server's own error envelope when available, otherwise pass the raw error along
          const errorData = axios.isAxiosError(error) ? error.response?.data : null;
          notifyIfHasMessage(errorData);
          reject(errorData ?? error);
        });
    });
  }

  get(url, params, config) {
    return this.#request('GET', url, null, mergeAxiosConfig(params, config));
  }

  post(url, data, config) {
    return this.#request('POST', url, data, config);
  }

  put(url, data, config) {
    return this.#request('PUT', url, data, config);
  }

  delete(url, data, config) {
    return this.#request('DELETE', url, data, config);
  }
}
