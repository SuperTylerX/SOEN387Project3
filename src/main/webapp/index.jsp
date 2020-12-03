<%@ page import="model.UserManager" %>
<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <link rel="stylesheet" href="css/mdui.min.css">
    <link rel="stylesheet" href="css/element-ui.css">
    <link rel="stylesheet" href="css/index.css">

</head>


<body class="mdui-theme-layout-light mdui-theme-accent-red mdui-theme-primary-red">


<div id="vue">
    <!-- Header -->
    <header class="mdui-appbar mdui-appbar-fixed">
        <div class="mdui-toolbar mdui-color-theme">
                <span class="mdui-btn mdui-btn-icon mdui-ripple mdui-ripple-white">
                    <i class="mdui-icon material-icons">menu</i>
                </span>
            <a href="./" class="mdui-typo-headline mdui-hidden-xs">Concordia Forum</a>
            <div class="mdui-toolbar-spacer"></div>
            <button class="mdui-btn mdui-btn-icon mdui-ripple mdui-ripple-white"
                    mdui-dialog="{target: '#search-post-dialog'}">
                <i class="mdui-icon material-icons">search</i>
            </button>
            <button class="mdui-btn mdui-btn-icon mdui-ripple mdui-ripple-white"
                    mdui-menu="{target: '#user-dropdown-menu',position : 'bottom'}">
                <i class="mdui-icon material-icons">person</i>
            </button>
            <ul class="mdui-menu" id="user-dropdown-menu">
                <li class="mdui-menu-item"><a class="mdui-ripple" @click="updateUser"><i
                        class="mdui-menu-item-icon mdui-icon material-icons">person</i>User</a></li>
                <li class="mdui-menu-item"><a class="mdui-ripple" @click="signout"><i
                        class="mdui-menu-item-icon mdui-icon material-icons">exit_to_app</i>Sign out</a></li>
            </ul>

        </div>
    </header>

    <!-- Post List -->
    <section class="container">
        <div class="post-list">
            <div class="mdui-shadow-2 post-item" v-for="item in postList">
                <div class="mdui-card-menu" v-if="userInfo.userId == item.postAuthorID || userInfo.isAdmin">
                    <button class="mdui-btn mdui-btn-icon mdui-text-color-black" :id="'post-'+ item.postID +'-btn'"
                            @click="moreOptions(item.postID)">
                        <i class="mdui-icon material-icons">more_vert</i>
                    </button>
                    <ul class="mdui-menu" :id="'post-'+ item.postID +'-menu'">
                        <li class="mdui-menu-item" @click="updatePost(item.postID)"><a class="mdui-ripple"><i
                                class="mdui-menu-item-icon mdui-icon material-icons">edit</i>Edit</a></li>
                        <li class="mdui-menu-item" @click="deletePost(item.postID)"><a class="mdui-ripple delete-btn"><i
                                class="mdui-menu-item-icon mdui-icon material-icons">delete</i>Delete</a></li>
                        <li class="mdui-menu-item"><a class="mdui-ripple delete-btn"
                                                      :href="'postdownload?postId=' + item.postID"><i
                                class="mdui-menu-item-icon mdui-icon material-icons">file_download</i>Download</a></li>
                    </ul>
                </div>
                <div class="mdui-card-primary">
                    <div class="mdui-card-primary-title">{{item.postTitle}}</div>
                    <div class="mdui-card-primary-subtitle">
                        <span class="item"><i class="el-icon-user"></i>{{item.postAuthorName}}</span>
                        <span class="item"><i class="el-icon-chat-line-square"></i>{{item.postGroupName}}</span>
                        <span class="item"><i class="el-icon-time"></i>{{item.postPublishedDate}}</span>
                    </div>
                </div>
                <div class="content">{{item.postContent}}</div>
                <a v-if="item.attachment"
                   :href="'file?attachId=' + item.attachment.attachID + '&postId=' + item.postID" target="_blank">
                    <div class="mdui-chip attach-file">
                        <span class="mdui-chip-icon"><i class="mdui-icon material-icons">attach_file</i></span>
                        <span class="mdui-chip-title">{{item.attachment.attachName}}</span>
                    </div>
                </a>
            </div>
        </div>
    </section>

    <!-- Create New Post -->
    <button class="mdui-fab mdui-fab-fixed mdui-ripple mdui-color-theme-accent"
            mdui-dialog="{target:'#create-post-dialog'}"><i class="mdui-icon material-icons">add</i></button>
    <div class="mdui-dialog post-area" id="create-post-dialog">
        <div class="mdui-dialog-title">Post Something...</div>
        <div class="mdui-dialog-content">

            <div class="mdui-textfield mdui-textfield-floating-label">
                <label class="mdui-textfield-label">Title</label>
                <input type="text" class="mdui-textfield-input" v-model="newPost.title"/>
            </div>
            <div class="mdui-textfield mdui-textfield-floating-label">
                <label class="mdui-textfield-label">Content</label>
                <textarea class="mdui-textfield-input" rows="5" v-model="newPost.content"></textarea>
            </div>


            <select class="mdui-select" v-model="newPost.groupId">
                <option :value="item.groupId" v-for="(item,index) in userInfo.userGroup">
                    {{item.groupName}}
                </option>
            </select>

            <el-upload action="file" :file-list="newPost.fileList" :limit="1"
                       :on-remove="removeAttach" :on-success="submitFileCallback">
                <el-button size="small">Click to upload</el-button>
            </el-upload>

            <button type="button" class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-red submit-btn"
                    mdui-dialog-confirm :disabled="!this.newPost.title || !this.newPost.content" @click="submitPost">
                Submit
            </button>
        </div>
    </div>


    <!-- Update Post -->
    <div class="mdui-dialog post-area" id="update-post-dialog">
        <div class="mdui-dialog-title">Post Something...</div>
        <div class="mdui-dialog-content">

            <div class="mdui-textfield">
                <label class="mdui-textfield-label">Title</label>
                <input type="text" class="mdui-textfield-input" v-model="updatedPost.title"/>
            </div>
            <div class="mdui-textfield">
                <label class="mdui-textfield-label">Content</label>
                <textarea class="mdui-textfield-input" rows="5" v-model="updatedPost.content"></textarea>
            </div>

            <el-upload action="file" :file-list="updatedPost.fileList" :limit="1"
                       :on-remove="removeAttach" :on-success="submitFileCallback2">
                <el-button size="small">Click to upload</el-button>
            </el-upload>

            <button type="button" class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-red submit-btn"
                    mdui-dialog-confirm :disabled="!this.updatedPost.title || !this.updatedPost.content"
                    @click="submitUpdatedPost">
                Submit
            </button>
        </div>
    </div>


    <!-- Search Post -->
    <div class="mdui-dialog post-area" id="search-post-dialog">
        <div class="mdui-dialog-title">Search</div>
        <div class="mdui-dialog-content">

            <div class="mdui-textfield">
                <label class="mdui-textfield-label">Author Name</label>
                <input type="text" class="mdui-textfield-input" v-model="searchContent.authorName"/>
            </div>
            <div class="mdui-textfield">
                <label class="mdui-textfield-label">Tags</label>
                <input type="text" class="mdui-textfield-input" v-model="searchContent.tags"
                       placeholder="#tag1,#tag2..."/>
            </div>
            <div class="mdui-textfield">
                <label class="mdui-textfield-label">Start Date</label>
                <input type="date" class="mdui-textfield-input" v-model="searchContent.startDate"/>
            </div>
            <div class="mdui-textfield">
                <label class="mdui-textfield-label">End Date</label>
                <input type="date" class="mdui-textfield-input" v-model="searchContent.endDate"/>
            </div>
            <button type="button" class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-red submit-btn"
                    mdui-dialog-confirm @click="searchPost"
                    :disabled="!((searchContent.endDate && searchContent.startDate)
                || (!this.searchContent.endDate && !this.searchContent.startDate))"
            >
                Search
            </button>
        </div>
    </div>

</div>

<script src="js/mdui.min.js "></script>
<%--<script src="js/vue-2.6.12.min.js "></script>--%>
<script src="js/vue.js"></script>
<script src="js/element-ui@2.13.2.js"></script>
<script src="js/axios.min.js"></script>
<script src="js/index.js "></script>
</body>

</html>