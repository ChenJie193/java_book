import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import piniaStore from './store';

import bootstrap from './core/bootstrap';
import '/@/styles/reset.less';
import '/@/styles/index.less';
import Antd from 'ant-design-vue';

const app = createApp(App);

//UI
app.use(Antd);
//路由
app.use(router);
//
app.use(piniaStore);
//UI
app.use(bootstrap);
app.mount('#app');
