<template>
  <div :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
    <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
    <sidebar v-if="!sidebar.hide" class="sidebar-container"/>
    <div :class="{hasTagsView:needTagsView,sidebarHide:sidebar.hide}" class="main-container">
      <div :class="{'fixed-header':fixedHeader}">
        <navbar/>
        <tags-view v-if="needTagsView"/>
      </div>
      <app-main/>
      <right-panel>
        <settings/>
      </right-panel>
    </div>
    <!-- 用户画像引导弹窗 -->
    <profile-guide ref="profileGuide" @completed="onProfileCompleted"/>
  </div>
</template>

<script>
import RightPanel from '@/components/RightPanel'
import ProfileGuide from '@/components/ProfileGuide'
import { AppMain, Navbar, Settings, Sidebar, TagsView } from './components'
import ResizeMixin from './mixin/ResizeHandler'
import { mapState, mapGetters } from 'vuex'
import variables from '@/assets/styles/variables.scss'

export default {
  name: 'Layout',
  components: {
    AppMain,
    Navbar,
    RightPanel,
    Settings,
    Sidebar,
    TagsView,
    ProfileGuide
  },
  mixins: [ResizeMixin],
  data() {
    return {
      // 标志位：是否已经显示过引导弹窗（防止重复触发）
      guideShown: false
    }
  },
  computed: {
    ...mapState({
      theme: state => state.settings.theme,
      sideTheme: state => state.settings.sideTheme,
      sidebar: state => state.app.sidebar,
      device: state => state.app.device,
      needTagsView: state => state.settings.tagsView,
      fixedHeader: state => state.settings.fixedHeader
    }),
    ...mapGetters(['roles', 'questionnaireCompleted']),
    classObj() {
      return {
        hideSidebar: !this.sidebar.opened,
        openSidebar: this.sidebar.opened,
        withoutAnimation: this.sidebar.withoutAnimation,
        mobile: this.device === 'mobile'
      }
    },
    variables() {
      return variables;
    },
    // 是否为普通用户（有common角色）
    isCommonUser() {
      return this.roles && this.roles.includes('common')
    },
    // 是否需要显示画像引导
    needProfileGuide() {
      return this.isCommonUser && this.questionnaireCompleted === '0'
    }
  },
  watch: {
    // 监听角色和问卷状态变化，判断是否需要弹窗
    needProfileGuide: {
      handler(val) {
        // 只有需要显示且尚未显示过时才触发
        if (val && !this.guideShown) {
          this.guideShown = true
          this.$nextTick(() => {
            this.$refs.profileGuide && this.$refs.profileGuide.show()
          })
        }
      },
      immediate: true
    }
  },
  methods: {
    handleClickOutside() {
      this.$store.dispatch('app/closeSideBar', { withoutAnimation: false })
    },
    onProfileCompleted() {
      // 画像完成后的回调，可以刷新用户信息
      this.$store.dispatch('GetInfo')
    }
  }
}
</script>

<style lang="scss" scoped>
  @import "~@/assets/styles/mixin.scss";
  @import "~@/assets/styles/variables.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }

  .fixed-header {
    position: fixed;
    top: 0;
    right: 0;
    z-index: 9;
    width: calc(100% - #{$base-sidebar-width});
    transition: width 0.28s;
  }

  .hideSidebar .fixed-header {
    width: calc(100% - 54px);
  }

  .sidebarHide .fixed-header {
    width: 100%;
  }

  .mobile .fixed-header {
    width: 100%;
  }
</style>
