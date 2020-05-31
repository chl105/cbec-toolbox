import Vue from 'vue'
import Vuex from 'vuex'
import Cookies from 'js-cookie'

Vue.use(Vuex)

const token = Cookies.get('token')
const tokenHeader = token ? `Bearer ${token}` : ''

const store = new Vuex.Store({
    state: {
        token: token,
        tokenHeader: tokenHeader,
        user: Cookies.get('user')
    },
    mutations: {
        setToken(state, v) {
            if(v){
                state.token = v
                state.tokenHeader = `Bearer ${v}`
                Cookies.set('token', v)
            }else{
                Cookies.remove('token')
            }
        },
        setUser(state, user) {
            state.user = user
            Cookies.set('user', user)
        }
    }
})

export default store