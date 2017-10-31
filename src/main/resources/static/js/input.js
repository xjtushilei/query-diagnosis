var vm = new Vue({
    el: "#app",
    data: {
        sym: "",
        symList: [],
        selectSyms: []
    },
    methods: {
        init: function () {
            vm.selectSyms = JSON.parse(getCookie('nowsym'))
        },
        search: function () {
            $.ajax({
                type: "post",
                url: "/api/search",
                data: {input: vm.sym, num: 15},
                success: function (data) {
                    vm.symList = data
                }
            });
        },
        add: function (x) {
            if ($.inArray(x, vm.selectSyms) !== -1) {
                $('#error1').text("该症状已经在列表中")
                $("#dialog1").show()
                return
            }
            else {
                vm.selectSyms.push(x)
            }
        },
        next: function () {
            if (vm.selectSyms.length === 0) {
                $('#error1').text("您未选择任何症状！")
                $("#dialog1").show()
                return
            }
            temp = []
            temp.push("呕吐")
            temp.push("上吐下泻")
            $("#loadingToast").show()
            $.ajax({
                type: 'post',
                // url: 'a.txt',
                url: '/api/symRecommend',
                data: {input: vm.selectSyms},
                // type of data we are expecting in return:
                dataType: 'json',
                timeout: 10000,
                success: function (data) {
                    console.log(data)
                    $("#loadingToast").show()
                    setCookie("recommendSyms", JSON.stringify(data), 'd20')
                    setCookie("nowsym", JSON.stringify(vm.selectSyms), 'd20')
                    window.location = 'recommend.html'
                },
                error: function (data) {
                    setTimeout("$('#error1').text('服务器发生未知错误，请联系管理员');$('#dialog1').show()", 400);
                }
            })
        }

    }
})
vm.init()