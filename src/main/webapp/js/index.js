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
            updatedPost: {
                postId: null,
                title: "",
                content: "",
                fileList: []
            },
            searchContent: {
                authorName: "",
                tags: "",
                startDate: "",
                endDate: ""
            },
            userInfo: {
                userName: "",
                userId: ""
            }
            // userUpdate: {
            //     email: "",
            //     password: "",
            //     passwordRepeat: ""
            // }
        }
    },
    computed: {},
    methods: {
        signout: function () {
            axios.get('auth').then(function (res) {
                if (res.status === 200) {
                    location = 'login.html'
                }
            })
        },
        moreOptions: function (postId) {
            new mdui.Menu("#post-" + postId + "-btn", "#post-" + postId + "-menu", {align: 'right'});
        },
        submitPost: function () {
            var self = this;
            var data = {
                postTitle: self.newPost.title,
                postContent: self.newPost.content,
            }
            if (this.newPost.fileList[0]) {
                data.attachId = this.newPost.fileList[0].attachId;
            }
            axios({
                url: 'post',
                method: 'post',
                data: data,
                transformRequest: [
                    self.json2Form
                ],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (data) {
                console.log(data)
                console.log(self.newPost.fileList)
                if (data.data.status === 200) {
                    var newPost = {
                        "postID": data.data.data.postID,
                        "postTitle": self.newPost.title,
                        "postAuthorName": self.userInfo.userName,
                        "postPublishedDate": new Date().toString(),
                        "postContent": self.newPost.content
                    }

                    if (self.newPost.fileList[0]) {
                        newPost.attachment = {
                            "attachID": self.newPost.fileList[0].attachId,
                            "attachName": self.newPost.fileList[0].attachName
                        }
                    }
                    console.log(newPost)
                    self.postList = [newPost].concat(self.postList)
                    self.newPost = {
                        title: "",
                        content: "",
                        fileList: []
                    }
                }

            })
        },
        updatePost: function (postId) {
            var self = this;
            new mdui.Dialog("#update-post-dialog").open();
            this.postList.forEach(function (el) {
                if (el.postID === postId) {
                    self.updatedPost = {
                        postId: postId,
                        title: el.postTitle,
                        content: el.postContent
                    }
                    if (el.attachment) {
                        self.updatedPost = {
                            ...self.updatePost,
                            fileList: {
                                name: el.attachment.attachName,
                                attachId: el.attachment.attachID
                            }
                        }
                    }
                }
            })
        },
        submitUpdatedPost: function () {
            var self = this;
            var postId = this.updatedPost.postId;
            var dataJson = {
                postId: postId,
                postTitle: self.updatedPost.title,
                postContent: self.updatedPost.content
            }
            if (self.newPost.fileList.length !== 0) {
                dataJson.attachId = self.newPost.fileList[0].attachId
            }
            axios({
                url: 'post',
                method: 'put',
                data: dataJson,
                transformRequest: [
                    self.json2Form
                ],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (data) {
                if (data.data.status === 200) {
                    self.postList.forEach(function (el) {
                        if (el.postID === postId) {
                            el.postTitle = dataJson.postTitle
                            el.postContent = dataJson.postContent
                            el.postPublishedDate = new Date().toString() + " (Modified)"
                            if (dataJson.attachId) {
                                el.attachment.attachID = dataJson.attachId;
                                el.attachment.attachName = self.updatedPost.fileList.name;
                            }
                        }
                    })
                }
            })
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
        searchPost: function () {
            var self = this;
            var searchObj = {}
            if (this.searchContent.authorName) {
                searchObj.authorName = this.searchContent.authorName;
            }
            if (this.searchContent.tags) {
                searchObj.tags = this.searchContent.tags;
            }
            if (this.searchContent.startDate) {
                searchObj.startDate = new Date(this.searchContent.startDate).getTime();
                searchObj.endDate = new Date(this.searchContent.endDate).getTime();
            }
            axios({
                url: "search",
                method: "get",
                params: searchObj
            }).then(function (res) {
                var result = res.data;
                if (result.status === 200) {
                    self.postList = result.data.map(function (el) {
                        if (el.postCreatedDate === el.postModifiedDate) {
                            el.postPublishedDate = new Date(el.postModifiedDate);
                        } else {
                            el.postPublishedDate = new Date(el.postModifiedDate) + " (Modified)";
                        }
                        return el;
                    });
                }
                self.searchContent = {
                    authorName: "",
                    tags: "",
                    startDate: "",
                    endDate: ""
                }
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
            mdui.dialog({
                title: "Notice",
                content: "User modification is not yet available.",
                buttons: [{
                    text: "OK"
                }]
            })
            // this.tab.handleUpdate()
        },
        // updateEmail: function () {
        // },
        // updatePassword: function () {
        //     if (this.userUpdate.password === this.userUpdate.passwordRepeat) {
        //         mdui.dialog({
        //             title: "Update Successfully",
        //             content: "The password is succefully updated.",
        //             buttons: [{
        //                 text: "OK"
        //             }]
        //         })
        //     } else {
        //         mdui.dialog({
        //             title: "Update Failed",
        //             content: "The password entered twice is different.",
        //             buttons: [{
        //                 text: "OK"
        //             }]
        //         })
        //     }
        // }
    },
    mounted: function () {
        var self = this;
        this.userInfo = window.userInfo;
        this.tab = new mdui.Tab('#update-user-tabs');
        axios.get("post").then(function (res) {
            var result = res.data;
            if (result.status === 200) {
                self.postList = result.data.map(function (el) {
                    if (el.postCreatedDate === el.postModifiedDate) {
                        el.postPublishedDate = new Date(el.postModifiedDate);
                    } else {
                        el.postPublishedDate = new Date(el.postModifiedDate) + " (Modified)";
                    }
                    return el;
                });
            }
        })
    }
})