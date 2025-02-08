<template>
  <a-config-provider :locale="local">
    <a-layout class="main">
      <MyHeader />
      <a-layout>
        <Sidebar :collapsed="collapsed" />
        <a-layout>
          <a-layout-content
            :style="contentStyle">
            <router-view />
          </a-layout-content>
        </a-layout>
      </a-layout>
      <menu-unfold-outlined
      v-if="collapsed"
      class="trigger"
      @click="toggleCollapse"
    />
    <menu-fold-outlined v-else class="trigger" @click="toggleCollapse" />
    </a-layout>
  </a-config-provider>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import MyHeader from '@/components/Header.vue'
import Sidebar from '@/components/Sidebar.vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import dayjs from 'dayjs';
import 'dayjs/locale/zh-cn';
dayjs.locale('zh-cn');

export default defineComponent({
  setup() {
    const collapsed = ref(false);
    const toggleCollapse = () => {
      collapsed.value = !collapsed.value;
    };
    return {
      collapsed,
      toggleCollapse,
    };
  },
  name: 'myHome',
  components: {
    MyHeader,
    Sidebar
  },
  data() {
    const contentStyle = { 
      padding: '0px 24px 0px 24px', 
      marginTop: '24px', 
      minHeight: '280px', 
      overflow: 'scroll' 
    }
    return {
      local: zhCN, 
      contentStyle
    }
  }
})
</script>

<style lang="less">
* {
  box-sizing: border-box;
}

.main {
  height: 100vh;
}
.trigger {
  font-size: 18px;
  line-height: 64px;
  padding: 0 24px;
  cursor: pointer;
  transition: color 0.3s;
  position: absolute;
  top: 24px;
  left: 200px;
}
</style>