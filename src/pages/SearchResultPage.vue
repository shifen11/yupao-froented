<template>
  <user-card-list :user-list="userList" :loading="loading" />
  <van-empty v-if="!userList || userList.length < 1" description="搜索结果为空" />
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { useRoute } from "vue-router";
import myAxios from "../plugins/myAxios";
import { Toast } from "vant";
import qs from 'qs';
import UserCardList from "../components/UserCardList.vue";
import { UserType } from '../models/user';

const route = useRoute();
const { tags } = route.query;

const userList = ref<UserType[]>([]);
const loading = ref(true);

onMounted(async () => {
  try {
    const response = await myAxios.get<UserType[]>('/user/search/tags', {
      params: {
        tagNameList: tags
      },
      paramsSerializer: params => qs.stringify(params, { indices: false })
    });
    console.log('/user/search/tags succeed', response);
    const userListData = response.data;
    userListData.forEach(user => {
      if (user.tags) {
        user.tags = JSON.parse(user.tags);
      }
    });
    userList.value = userListData;
  } catch (error) {
    console.error('/user/search/tags error', error);
    Toast.fail('请求失败');
  } finally {
    loading.value = false;
  }
});


</script>

<style scoped></style>
