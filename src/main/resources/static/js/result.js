var vm = new Vue({
    el: "#app",
    data: {
        disease: ""
    },
    methods: {
        init: function () {
            vm.disease = JSON.parse(getCookie('getDisease'))
        },
        search: function () {

        }
    }
})
vm.init()

function setnull() {
    setCookie("recommendSyms", JSON.stringify([]), 'd20')
    setCookie("nowsym", JSON.stringify([]), 'd20')
    window.location = 'input.html'

}