<%
    if (session.getAttribute("userId") == null) {
        response.sendRedirect("login.html");
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
            <a href="../" class="mdui-typo-headline mdui-hidden-xs">Concordia Forum</a>
            <div class="mdui-toolbar-spacer"></div>
            <span class="mdui-btn mdui-btn-icon mdui-ripple mdui-ripple-white">
                    <i class="mdui-icon material-icons">search</i>
                </span>
            <button class="mdui-btn mdui-btn-icon mdui-ripple mdui-ripple-white"
                    mdui-menu="{target: '#user-dropdown-menu',position : 'bottom'}">
                <i class="mdui-icon material-icons">person</i>
            </button>
            <ul class="mdui-menu" id="user-dropdown-menu">
                <li class="mdui-menu-item"><a class="mdui-ripple" @click="updateUser"
                                              mdui-dialog="{target: '#update-user-dialog'}"><i
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
                <div class="mdui-card-menu">
                    <button class="mdui-btn mdui-btn-icon mdui-text-color-black" :id="'post-'+ item.postId +'-btn'"
                            @click="moreOptions(item.postId)">
                        <i class="mdui-icon material-icons">more_vert</i>
                    </button>
                    <ul class="mdui-menu" :id="'post-'+ item.postId +'-menu'">
                        <li class="mdui-menu-item"><a class="mdui-ripple"><i
                                class="mdui-menu-item-icon mdui-icon material-icons">edit</i>Edit</a></li>
                        <li class="mdui-menu-item" @click="deletePost(item.postId)"><a class="mdui-ripple delete-btn"><i
                                class="mdui-menu-item-icon mdui-icon material-icons">delete</i>Delete</a></li>
                    </ul>
                </div>
                <div class="mdui-card-primary">
                    <div class="mdui-card-primary-title">{{item.postTitle}}</div>
                    <div class="mdui-card-primary-subtitle">
                        <span class="item"><i class="el-icon-user"></i>{{item.postAuthor}}</span>
                        <span class="item"><i class="el-icon-time"></i>{{item.postPublishedDate}}</span>
                    </div>
                </div>
                <div class="content">{{item.postContent}}</div>
                <div class="mdui-chip attach-file">
                    <span class="mdui-chip-icon"><i class="mdui-icon material-icons">attach_file</i></span>
                    <span class="mdui-chip-title">{{item.attachment.attachTitle}}</span>
                </div>
            </div>
        </div>
    </section>

    <!-- Create New Post -->
    <button class="mdui-fab mdui-fab-fixed mdui-ripple mdui-color-theme-accent"
            mdui-dialog="{target:'#create_post-dialog'}"><i class="mdui-icon material-icons">add</i></button>
    <div class="mdui-dialog post-area" id="create_post-dialog">
        <div class="mdui-dialog-title">Post Something...</div>
        <div class="mdui-dialog-content">

            <div class="mdui-textfield mdui-textfield-floating-label">
                <label class="mdui-textfield-label">Title</label>
                <input type="text" class="mdui-textfield-input" v-model="newPost.title"></input>
            </div>
            <div class="mdui-textfield mdui-textfield-floating-label">
                <label class="mdui-textfield-label">Content</label>
                <textarea class="mdui-textfield-input" rows="5" v-model="newPost.content"></textarea>
            </div>

            <el-upload action="https://httpbin.org/post" :file-list="newPost.fileList" :limit="1"
                       :on-remove="handleRemove">
                <el-button size="small">Click to upload</el-button>
            </el-upload>

            <button type="button" class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-red submit-btn"
                    mdui-dialog-confirm :disabled="!this.newPost.title || !this.newPost.content" @click="submitPost">
                Submit
            </button>
        </div>
    </div>

    <!-- Update User info -->
    <div class="mdui-dialog" id="update-user-dialog">
        <div class="mdui-tab mdui-tab-full-width" id="update-user-tabs">
            <a href="#email-tab" class="mdui-ripple">Email</a>
            <a href="#password-tab" class="mdui-ripple">Password</a>
        </div>
        <div id="email-tab" class="mdui-p-a-2">
            <div class="mdui-textfield">
                <i class="mdui-icon material-icons">email</i>
                <input class="mdui-textfield-input" type="email" placeholder="Email" v-model="userUpdate.email"/>
            </div>
            <button class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-theme-accent" mdui-dialog-confirm
                    @click="updateEmail">Update
            </button>
        </div>
        <div id="password-tab" class="mdui-p-a-2">
            <div class="mdui-textfield">
                <i class="mdui-icon material-icons">lock</i>
                <input class="mdui-textfield-input" type="password" placeholder="Enter your new password"
                       v-model="userUpdate.password"/>
            </div>
            <div class="mdui-textfield">
                <i class="mdui-icon material-icons">lock</i>
                <input class="mdui-textfield-input" type="password" placeholder="Enter your new password again"
                       v-model="userUpdate.passwordRepeat"/>
            </div>
            <button class="mdui-btn mdui-btn-raised mdui-ripple mdui-color-theme-accent" mdui-dialog-confirm
                    @click="updatePassword">Update
            </button>
        </div>
    </div>
</div>

<script src="js/mdui.min.js "></script>
<script src="js/vue-2.6.12.min.js "></script>
<script src="js/element-ui@2.13.2.js"></script>
<script src="js/axios.min.js"></script>
<script src="js/index.js "></script>
</body>

</html>