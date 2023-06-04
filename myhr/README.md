# myhr

This template should help get you started developing with Vue 3 in Vite.

## Recommended IDE Setup

[VSCode](https://code.visualstudio.com/) + [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) (and disable Vetur) + [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin).

## Type Support for `.vue` Imports in TS

TypeScript cannot handle type information for `.vue` imports by default, so we replace the `tsc` CLI with `vue-tsc` for type checking. In editors, we need [TypeScript Vue Plugin (Volar)](https://marketplace.visualstudio.com/items?itemName=Vue.vscode-typescript-vue-plugin) to make the TypeScript language service aware of `.vue` types.

If the standalone TypeScript plugin doesn't feel fast enough to you, Volar has also implemented a [Take Over Mode](https://github.com/johnsoncodehk/volar/discussions/471#discussioncomment-1361669) that is more performant. You can enable it by the following steps:

1. Disable the built-in TypeScript Extension
    1) Run `Extensions: Show Built-in Extensions` from VSCode's command palette
    2) Find `TypeScript and JavaScript Language Features`, right click and select `Disable (Workspace)`
2. Reload the VSCode window by running `Developer: Reload Window` from the command palette.

## Customize configuration

See [Vite Configuration Reference](https://vitejs.dev/config/).

## Project Setup
```sh
npm install
npm run dev
npm run build
```
# 遇到的问题记录及解决办法
### 首次登陆菜单仅有配置中的
    分析：home进入时候，store未加载完成
    使用complute解决：const routes=computed(()=>store.menuRoutes);
    或同步，初始化完成后再进入
### 点击子页报错cannot use 'in' ...for 'catch' in...
    由于是子页，当时代码里没有处理/home的路由，加上即可
### 子页可访问后，但No match found for location with path "/emp/basic"
    原因：有两个name：‘员工资料’，在导入时只会有一个生效（router不允许name值重复，后覆盖前？）
        将数据库中第一级的‘员工资料’改成‘员工信息即可’
### 直接从地址栏处访问子页面，会出现空白页。“No match found for location with path "/emp/basic”
    原因：没有路由
    强行让 菜单初始化以后 再挂在mount('#app').
## vue3中关于数据的更新
    不能直接更新，ref需要使用.value 进行更新，否则相当于改变了指针。丢失了链接性
###  组件中使用v-for。导致ref获得不到应得的实例。从而使得组件的方法不可用
    由于v-for的存在，使得const treeRef = ref<InstanceType<typeof ElTree>>()实际上是一个数组。
    那么就不能直接调用方法。使用方法时，应treeRef.value[id].func();来调用。即获得数组里的元素再调用方法
### 关于v-for循环v-model绑定变量
    v-model="stepmsg.salary[key]"
    即可绑定   stepmsg.salary:{name:'test',basic:0, } 
    等价于 v-model="salary.name"  v-model="...."  可参见 `SalSob.uve`

    实际上：可用对象的字段名访问该字段信息（类似map）， 如stepmsg.salary[name] 即'test' 。故而stepmsg.salary[key]即key对应下的字段值
    但似乎只能用于template内，不能用于script
### vue插槽
    <slot :name=""> name是vue保留的一个属性。不作为props传递
    <template #header="xxx">     </template> 是拿到子组件的信息xxx
    <template #header      > xxx </template>  是把xxx给插槽用      ####参见SalSobCfg中有同时使用以上两种
    当然也有 动态插槽 <tempate #[dynamicSlotName] >
### 关于ts使用socket
    https://blog.csdn.net/qq_51553982/article/details/128273861
    另外注意，import SockJS from 'sockjs-client/dist/sockjs.min.js';而不是直接的sockjs（会报错）
## 一个疑问：一个关于map是引用还是拷贝的问题
    index.ts 中 
    let s='string'
    ms=this.sessions[s];//注意，ms和session[]不指向同一对象了?非引用，在ms上push后session不会变化
    if(ms==null){
        this.sessions[s]=[]
    }
    this.sessions[s].push({
        content:msg.content,
        data:new Date(),
        self:!msg.notSelf
    })
    如果对ms.push 则this.session[s] 不会变化