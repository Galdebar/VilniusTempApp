import Vue from "vue";
import Vuex from "vuex";

import {
  getLatestTemp,
  // getNextUpdateTime,
  getHistory,
} from "../components/Dispatcher";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    latestTemp: {},
    history: [],
  },
  mutations: {
    updateLatestTemp(state, payload) {
      state.latestTemp = payload;
    },
    updateHistory(state, payload) {
      state.history = payload;
    },
  },
  actions: {
    async updateTemp(context) {
      let response = await getLatestTemp();
      let temperatureObj = await context.dispatch("handleResponse", response);

      if (temperatureObj !== null) {
        context.commit("updateLatestTemp", temperatureObj);
      }
    },
    async updateHistory(context, days){
      let response = await getHistory(days);
      let responseArray = await context.dispatch("handleResponse", response);
      console.log(responseArray);
      if (responseArray !== null) {
        context.commit("updateHistory", responseArray);
      }
    },
    async handleResponse(context, response) {
      if (response.ok) {
        let responseObj = await response.json();
        return responseObj;
      }
      if (response.status === 403) {
        //something clever if getting an error
        let responseText = response.text();
        console.log(responseText);
      }
    },
    async startRefresh(context){
      let promise = new Promise((res) => {
        setTimeout(() => res("Now it's done!"), 100000);
      });
      let promiseTimeout = await promise;
      if (promiseTimeout) {
        context.dispatch("updateTemp");
      }
    },
  },

  getters: {
    getLatestTemp: (state) => {
      return state.latestTemp;
    },
    getHistory: (state) => {
      return state.history;
    },
  },
});
