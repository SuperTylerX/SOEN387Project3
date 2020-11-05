new Vue({
    el: '#vue',
    data: function () {
        return {
            postList: [],
            newPost: {
                title: "",
                content: "",
                fileList: []
            },
            userUpdate: {
                email: "",
                password: "",
                passwordRepeat: ""
            }
        }
    },
    computed: {},
    methods: {
        moreOptions: function (postId) {
            new mdui.Menu("#post-" + postId + "-btn", "#post-" + postId + "-menu", {align: 'right'});
        },
        deletePost: function (postId) {
            var self = this;
            mdui.confirm("Are You Sure?", "This post will be deleted", function () {
                axios({
                    method: 'delete',
                    url: 'post',
                    data: {
                        postId: postId
                    },
                    transformRequest: [
                        self.json2Form
                    ],
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                }).then(function (data) {
                    if (data.data.status === 200) {
                        self.postList = self.postList.filter(function (item) {
                            return item.postID !== postId;
                        })
                    }
                })


            })
        },
        handleRemove(file, fileList) {
            console.log(file, fileList);
        },
        json2Form: function (data) {
            let ret = ''
            for (let it in data) {
                ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
            }
            ret = ret.substring(0, ret.lastIndexOf('&'));
            return ret
        },
        updateUser: function () {
            this.tab.handleUpdate()
        },
        signout: function () {
            axios.get('auth').then(function (res) {
                if (res.status === 200) {
                    location = 'login.html'
                }
            })
        },
        submitPost: function () {
            var self = this;
            axios.post('http://httpbin.org/post', {
                data: {
                    postTitle: self.newPost.title,
                    postContent: self.newPost.content,
                },
                transformRequest: [
                    self.json2Form
                ],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (data) {
                console.log(data)
                var newPost = {
                    "postId": 9,
                    "postTitle": self.newPost.title,
                    "postAuthor": "Bob",
                    "postPublishedDate": "2020-10-23 23:32:32(Updated)",
                    "postContent": self.newPost.content,
                    "attachment": {
                        "attachID": 1,
                        "attachTitle": "hello.txt"
                    }
                }
                console.log(newPost)
                self.postList = [newPost].concat(self.postList)
                self.newPost = {
                    title: "",
                    content: "",
                    fileList: []
                }


            })
        },
        updateEmail: function () {
            this.userUpdate.email
        },
        updatePassword: function () {
            if (this.userUpdate.password === this.userUpdate.passwordRepeat) {
                mdui.dialog({
                    title: "Update Successfully",
                    content: "The password is succefully updated.",
                    buttons: [{
                        text: "OK"
                    }]
                })
            } else {
                mdui.dialog({
                    title: "Update Failed",
                    content: "The password entered twice is different.",
                    buttons: [{
                        text: "OK"
                    }]
                })
            }
        }
    },
    mounted: function () {
        var self = this;
        this.tab = new mdui.Tab('#update-user-tabs');
        axios.get("post").then(function (res) {
            var result = res.data;
            if (result.status === 200) {
                self.postList = result.data;
            }
        })
    }
})