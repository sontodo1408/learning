import RestClient from '@/services/rest-client';

/**
 * Base class for every API service. Each subclass calls `super(servicePath)`
 * with its backend resource path (e.g. "/auth") and then calls this.get/post/
 * put/delete — never touching RestClient directly.
 */
export default class BaseService {
  #client;

  constructor(servicePath) {
    this.#client = new RestClient(servicePath);
  }

  get(url, params, config) {
    return this.#client.get(url, params, config);
  }

  post(url, data, config) {
    return this.#client.post(url, data, config);
  }

  put(url, data, config) {
    return this.#client.put(url, data, config);
  }

  delete(url, data, config) {
    return this.#client.delete(url, data, config);
  }
}
