import { getRequest } from "./api";
import { useRouter } from "vue-router";
import { useStore } from "@/store";



export async function initMenu(router,store){
    //console.log("进入了initMenu函数")
    if(store.menuRoutes.length>0){
        //console.log("已经有store存在，不需要再次store")
        return true;
    }else{
        //console.log("store为空，需要初始化,并开始请求")
        await getRequest('/system/config/menu','').then(data=>{
            if(data){
                //参考route配置，需要把component项的值变为对象（其他字段的参数本来就是字串）
                let fmtRoutes=formatRoutes(data);
                //console.log('规格化后的数据:',fmtRoutes)
                fmtRoutes.forEach(element=>{
                    let fmtRouter={
                        name :element.name,
                        path: element.path,
                        component: element.component,
                        children: element.children
                    }
                    //console.log('各个router信息',fmtRouter)
                    router.addRoute(fmtRouter)

                })
                store.initMenuStore(fmtRoutes);
                //console.log("动态菜单store、router刚刚加载完成");
                //console.log('initMenu中的router：',router.getRoutes())
            }
        })
    }
    return true
}
export default function formatRoutes(routes){
    let fmtRoutes=[];
    routes.forEach(router => {
        let{
            id,url,path,component,name,iconCls,meta,parentId,enabled,children,roles
        }=router;
        if(children && children instanceof Array){
            children=formatRoutes(children);
        }
        let fmtRouter={
            path: path,
            name: name,
            iconCls: iconCls,
            meta: meta,
            children: children,
            //  注意此处引入是反单引号、不是引号
            component: ()=>{
                if(component.startsWith("Home")){
                    return import(`../views/${component}.vue`)
                }//以上home处理不能少，因为有的页面属于/home。如果不处理，则导致路由出错（大概是因为主路由设置出问题，子路由也无法使用）
                //报错如下：Cannot use 'in' operator to search for 'catch' in undefined at extractComponentsGuard
                else if(component.startsWith("Emp")){
                    return import(`../views/emp/${component}.vue`)
                }
                else if(component.startsWith("Per")){
                    return import(`../views/per/${component}.vue`)
                }
                else if(component.startsWith("Sal")){
                    return import(`../views/sal/${component}.vue`)
                }
                else if(component.startsWith("Sta")){
                    return import(`../views/sta/${component}.vue`)
                }
                else if(component.startsWith("Sys")){
                    return import(`../views/sys/${component}.vue`)
                }
            }
        }
        fmtRoutes.push(fmtRouter);
    });
    return fmtRoutes;
}