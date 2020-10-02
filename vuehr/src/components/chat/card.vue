<template>
    <div id="card">
        <header>
            <div class="closeBtn">
                <i @click="closeChat"
                   style="cursor: pointer"
                   class="fa fa-times"
                   aria-hidden="true"></i>
            </div>
            <div class="avatarDiv">
                <img class="avatar" v-bind:src="user.userface" v-bind:alt="user.name">
            </div>
            <div class="name">{{user.name}}</div>
        </header>
        <footer>
            <input class="search" type="text"  placeholder="search user...">
        </footer>
    </div>
</template>

<script>
    export default {
        name: 'card',
        data() {
            return {
                user: JSON.parse(window.sessionStorage.getItem("user")),
            }
        },
        methods:{
            closeChat(){
                this.$router.push("/home");
            }
        }
    }
</script>

<style lang="scss" scoped>

    .avatarDiv{
        display: flex;
        justify-content: center;
    }

    #card {
        padding: 12px;

        .avatar {
            width: 40px;
            height: 40px;
            border-radius: 5px;
            vertical-align: middle; /*这个是图片和文字居中对齐*/
        }

        .name {
            display: flex;
            justify-content: center;
            font-family: 华文行楷;
            padding: 10px;
            margin-bottom: 10px;
            font-size: 16px;
        }

        .search {
            background: #26292E;
            height: 30px;
            line-height: 30px;
            padding: 0 10px;
            border: 1px solid #3a3a3a;
            border-radius: 4px;
            outline: none; /*鼠标点击后不会出现蓝色边框*/
            color: #FFF;
        }
    }
    .closeBtn {
        margin-bottom: 5px;
    }
</style>
