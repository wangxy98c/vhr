import { defineStore } from "pinia";

export const useStore=defineStore('menuStore',{
    state:()=>({
        menuRoutes:[]
    }),
    actions:{
        initMenuStore(data:any){
            this.menuRoutes=data;
        }
    }
})