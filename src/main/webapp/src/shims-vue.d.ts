/* eslint-disable */
declare module '*.vue' {
  import type { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
}

// shims-vue.d.ts
declare module 'wave-ui' {
  export default WaveUI
}
