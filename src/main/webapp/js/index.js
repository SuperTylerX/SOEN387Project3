Date.prototype.format = function (format) {
    var date = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate().toString(),
        "h+": this.getHours().toString().padStart(2, '0'),
        "m+": this.getMinutes().toString().padStart(2, '0'),
        "s+": this.getSeconds().toString().padStart(2, '0'),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S+": this.getMilliseconds().toString()
    };
    if (/(y+)/i.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    for (var k in date) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length === 1 ?
                date[k] : ("00" + date[k]).substr(("" + date[k]).length));
        }
    }
    return format;
}
new Vue({
    el: '#vue',
    data: function () {
        return {
            postList: [],
            newPost: {
                title: "",
                content: "",
                fileList: [],
                groupId: 0
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
                userId: "",
                userGroup: []
            }
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
            new mdui.Menu("#post-" + postId + "-btn", "#post-" + postId + "-menu", {align: 'right'}).open();
        },
        submitFileCallback: function (response, file, fileList) {
            if (response.status === 200) {
                fileList[0].attachId = response.data.attachID;
                this.newPost.fileList = fileList;
            }
        },
        submitFileCallback2: function (response, file, fileList) {
            if (response.status === 200) {
                fileList[0].attachId = response.data.attachID;
                this.updatedPost.fileList = fileList;
            }
        },
        submitPost: function () {
            var self = this;
            var data = {
                postTitle: self.newPost.title,
                postContent: self.newPost.content,
                postGroup: self.newPost.groupId
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
                if (data.data.status === 200) {
                    var newPost = {
                        "postID": data.data.data.postID,
                        "postTitle": self.newPost.title,
                        "postAuthorID": self.userInfo.userId,
                        "postAuthorName": self.userInfo.userName,
                        "postPublishedDate": new Date().format('yyyy-MM-dd h:m:s'),
                        "postContent": self.newPost.content
                    }

                    if (self.newPost.fileList.length === 1) {
                        newPost.attachment = {
                            "attachID": self.newPost.fileList[0].attachId,
                            "attachName": self.newPost.fileList[0].name
                        }
                    }
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
                        content: el.postContent,
                        fileList: []
                    }
                    if (el.attachment) {
                        self.updatedPost.fileList = [{
                            name: el.attachment.attachName,
                            attachId: el.attachment.attachID
                        }]
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
            if (self.updatedPost.fileList.length !== 0) {
                dataJson.attachId = self.updatedPost.fileList[0].attachId;
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
                            el.postTitle = dataJson.postTitle;
                            el.postContent = dataJson.postContent;
                            el.postPublishedDate = new Date().format('yyyy-MM-dd h:m:s') + " (Modified)";
                            if (dataJson.attachId) {
                                if (!el.attachment) {
                                    el.attachment = {};
                                }
                                el.attachment.attachID = dataJson.attachId;
                                el.attachment.attachName = self.updatedPost.fileList[0].name;
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
            var searchObj = {};
            if (this.searchContent.authorName) {
                searchObj.authorName = this.searchContent.authorName;
            }
            if (this.searchContent.tags) {
                searchObj.tags = this.searchContent.tags;
            }
            if (this.searchContent.startDate) {
                searchObj.startDate = new Date(this.searchContent.startDate + " 00:00:00").getTime();
                searchObj.endDate = new Date(this.searchContent.endDate + " 00:00:00").getTime() + 1000 * 60 * 60 * 24;
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
                            el.postPublishedDate = new Date(el.postModifiedDate).format('yyyy-MM-dd h:m:s');
                        } else {
                            el.postPublishedDate = new Date(el.postModifiedDate).format('yyyy-MM-dd h:m:s') + " (Modified)";
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
        removeAttach(file, fileList) {
            var self = this;
            var attachId = file.attachId;
            axios({
                url: "file",
                method: "delete",
                data: {
                    attachId: attachId
                },
                transformRequest: [
                    self.json2Form
                ],
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(function (res) {
                    self.newPost.fileList = [];
                    self.updatedPost.fileList = [];
                    self.postList.forEach(function (el) {
                        if (el.attachment && el.attachment.attachID === attachId) {
                            el.attachment = null;
                        }
                    })
                    if (res.data.status !== 200) {
                        alert("Fail!");
                    }

                }
            )
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
        }
    },
    mounted: function () {
        var self = this;
        // Get user info

        axios.get("user").then(function (res) {
            if (res.status === 200 && res.data.status === 200) {
                self.userInfo.userName = res.data.data.userName
                self.userInfo.userId = res.data.data.userId
                self.userInfo.userGroup = [{
                    groupId: 0,
                    groupName: "public"
                }].concat(res.data.data.userGroup)
            }
            axios.get("post").then(function (res) {
                var result = res.data;
                if (result.status === 200) {
                    self.postList = result.data.map(function (el) {
                        if (el.postCreatedDate === el.postModifiedDate) {
                            el.postPublishedDate = new Date(el.postModifiedDate).format('yyyy-MM-dd h:m:s');
                        } else {
                            el.postPublishedDate = new Date(el.postModifiedDate).format('yyyy-MM-dd h:m:s') + " (Modified)";
                        }
                        return el;
                    });
                }
            })
        })

    }
})