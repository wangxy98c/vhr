import { getRequest } from "./api";

export function initMenu(router,store){
    return new Promise(()=>{
    console.log("进入了initMenu函数")
    if(store.menuRoutes.length>0){
        console.log("已经有store存在，不需要再次store")
        return true;
    }else{
        console.log("store为空，需要初始化,并开始请求")
        getRequest('/system/config/menu','').then(data=>{
            console.log("请求完成，后端返回data:",data)
            if(data){
                //参考route配置，需要把component项的值变为对象（其他字段的参数本来就是字串）
                let fmtRoutes=formatRoutes(data);
                fmtRoutes.forEach(fmtRoute=>{
                    router.addRoute(fmtRoute)
                })
                store.initMenuStore(fmtRoutes);
                console.log('initMenu中的router：',router.getRoutes())
                console.log("已经添加完route以及store。。。")
            }
        })
    }
    return true
    })
}
export default function formatRoutes(routes){
    let fmtRoutes=[];
    routes.forEach(router => {
        let{
            path,component,name,meta,iconCls,children
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
            //  注意此处是反单引号、不是引号
            component:()=>{
                if(component.startsWith("Emp")){
                    import(`../views/emp/${component}.vue`)
                }
                if(component.startsWith("Per")){
                    import(`../views/per/${component}.vue`)
                }
                if(component.startsWith("Sal")){
                    import(`../views/Sal/${component}.vue`)
                }
                if(component.startsWith("Sta")){
                    import(`../views/sta/${component}.vue`)
                }if(component.startsWith("Sys")){
                    import(`../views/sys/${component}.vue`)
                }
            }
        }
        fmtRoutes.push(fmtRouter);
    });
    return fmtRoutes;
}