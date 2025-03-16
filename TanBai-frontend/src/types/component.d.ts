/**
 * declare module '@vue/runtime-core'
 *   现调整为
 * declare module 'vue'
 */
import 'vue'
// src/types/components.d.ts 全局组件类型声明
import XtxSwiper from './XtxSwiper.vue'

declare module 'vue' {
  export interface GlobalComponents {
    //
  }
}

declare module 'vue' {
  export interface GlobalComponents {
    XtxSwiper: typeof XtxSwiper
  }
}
