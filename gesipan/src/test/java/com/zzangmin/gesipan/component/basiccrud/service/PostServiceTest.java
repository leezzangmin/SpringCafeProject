package com.zzangmin.gesipan.component.basiccrud.service;

import com.zzangmin.gesipan.component.basiccrud.dto.post.*;
import com.zzangmin.gesipan.component.basiccrud.entity.*;
import com.zzangmin.gesipan.component.basiccrud.repository.*;
import com.zzangmin.gesipan.component.login.entity.Users;
import com.zzangmin.gesipan.component.login.repository.UsersRepository;

import java.util.Optional;

import com.zzangmin.gesipan.testfactory.EntityFactory;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
@ExtendWith(MockitoExtension.class)
@Transactional
class PostServiceTest {

    @Autowired private PostRepository postRepository;
    @Autowired private UsersRepository usersRepository;
    @Autowired private PostCategoryRepository postCategoryRepository;
    @Autowired private TemporaryPostRepository temporaryPostRepository;
    @Autowired private PostService postService;
    @Autowired private CommentRepository commentRepository;
    @Autowired private PostRecommendRepository postRecommendRepository;


    @DisplayName("게시물 단건조회를 요청하면 올바른 내용을 가진 게시물 DTO가 반환되어야 한다. ")
    @Test
    void findOne() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();

        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        //when
        PostResponse postResponse = postService.findOne(postId, Optional.empty());
        //then
        Assertions.assertThat(postResponse.getPostId()).isEqualTo(post.getPostId());
        Assertions.assertThat(postResponse.getPostContent()).isEqualTo(post.getPostContent());
    }

    @DisplayName("존재하지 않는 id를 가진 게시물 단건조회를 요청하면 오류가 발생해야 한다.")
    @Test
    void findOne_noPostId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();

        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        Long invalidPostId = 9991231435145L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.findOne(invalidPostId, Optional.empty()));
    }

    @DisplayName("단건조회를 요청하면 댓글이 최대 10개까지만 조회되어야 한다.")
    @Test
    void findOne_commentSizeMaximum10() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();
        Comment comment1 = EntityFactory.generateCommentObject(post, user);
        Comment comment2 = EntityFactory.generateCommentObject(post, user);
        Comment comment3 = EntityFactory.generateCommentObject(post, user);
        Comment comment4 = EntityFactory.generateCommentObject(post, user);
        Comment comment5 = EntityFactory.generateCommentObject(post, user);
        Comment comment6 = EntityFactory.generateCommentObject(post, user);
        Comment comment7 = EntityFactory.generateCommentObject(post, user);
        Comment comment8 = EntityFactory.generateCommentObject(post, user);
        Comment comment9 = EntityFactory.generateCommentObject(post, user);
        Comment comment10 = EntityFactory.generateCommentObject(post, user);
        Comment comment11 = EntityFactory.generateCommentObject(post, user);
        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
        commentRepository.save(comment6);
        commentRepository.save(comment7);
        commentRepository.save(comment8);
        commentRepository.save(comment9);
        commentRepository.save(comment10);
        commentRepository.save(comment11);
        //when
        PostResponse singlePost = postService.findOne(post.getPostId(),Optional.empty());
        //then
        Assertions.assertThat(singlePost.getComments().size()).isLessThan(11);
    }

    @DisplayName("게시글 단건조회를 짧은시간 내에 두 번 요청해도 캐시관련 오류가 발생하지 않아야 한다.")
    @Test
    void findOne_cache_noError() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        PostCategory postCategory = post.getPostCategory();

        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        //when
        PostResponse postResponse1 = postService.findOne(postId, Optional.empty());
        PostResponse postResponse2 = postService.findOne(postId, Optional.empty());
        //then
        Assertions.assertThat(postResponse1.getPostId()).isEqualTo(post.getPostId());
        Assertions.assertThat(postResponse1.getPostContent()).isEqualTo(post.getPostContent());
        Assertions.assertThat(postResponse2.getPostId()).isEqualTo(post.getPostId());
        Assertions.assertThat(postResponse2.getPostContent()).isEqualTo(post.getPostContent());
    }

    @DisplayName("Post가 저장되어야 한다.")
    @Test
    void save() {
        //given
        PostCategory postCategory = EntityFactory.generatePostCategoryObject(Categories.FREE);
        Users user = EntityFactory.generateRandomUsersObject();

        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        PostSaveRequest postSaveRequest = new PostSaveRequest("test제목1", "test내용1", postCategoryId, LocalDateTime.now(), null);
        //when
        Long saveId = postService.save(userId, postSaveRequest);
        //then
        Post findPost = postRepository.findById(saveId).get();
        Assertions.assertThat(findPost.getPostSubject()).isEqualTo(postSaveRequest.getPostSubject());
    }

    @DisplayName("올바르지 않은 userId로 게시글 저장 요청을 보내면 오류가 발생해야 한다.")
    @Test
    void save_invalidUserId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        PostSaveRequest postSaveRequest = new PostSaveRequest("asdf", "dddd", post.getPostCategory().getPostCategoryId(), LocalDateTime.now(), null);
        Long invalidUserId = 1345987569834759L;
        //when

        //then
        Assertions.assertThatThrownBy(() -> postService.save(invalidUserId, postSaveRequest));
    }

    @DisplayName("올바르지 않은 postCategoryId로 게시글 저장 요청을 보내면 오류가 발생해야 한다.")
    @Test
    void save_invalidPostCategoryId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        Long invalidPostCategoryId = 13849137489999999L;
        PostSaveRequest postSaveRequest = new PostSaveRequest("asdf", "dddd", invalidPostCategoryId, LocalDateTime.now(), null);
        //when

        //then
        Assertions.assertThatThrownBy(() -> postService.save(user.getUserId(), postSaveRequest));
    }

    @Test
    @DisplayName("임시 게시물을 불러와서 저장하면 임시 게시물은 삭제되어야 한다.")
    void save2() {
        //given
        PostCategory postCategory = EntityFactory.generatePostCategoryObject(Categories.FREE);
        Users user = EntityFactory.generateRandomUsersObject();
        TemporaryPost temporaryPost = EntityFactory.generateRandomTemporaryPostObject(user);

        Long postCategoryId = postCategoryRepository.save(postCategory).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long tempPostId = temporaryPostRepository.save(temporaryPost).getTempPostId();
        PostSaveRequest postSaveRequest = new PostSaveRequest("test제목1", "test내용1", postCategoryId, LocalDateTime.now(), tempPostId);
        //when
        Long savedPostId = postService.save(userId, postSaveRequest);
        //then
        List<TemporaryPost> temporaryPosts = temporaryPostRepository.findByUserId(userId);
        Assertions.assertThat(temporaryPosts.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Post가 삭제되어야 한다.")
    void delete() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Long postCategoryId = postCategoryRepository.save(post.getPostCategory()).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        //when
        postService.delete(postId, userId);
        //then
        Assertions.assertThatThrownBy(() -> postService.delete(postId, 100000L));
        Assertions.assertThatThrownBy(() -> postRepository.findById(postId).get());
    }

    @DisplayName("존재하지 않는 userId로 삭제 요청을 하면 오류가 발생해야 한다.")
    @Test
    void delete_invalidUserId() {
        //given
        Long invalidUserId = 23478659134999L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.delete( 1L, invalidUserId));
    }

    @DisplayName("존재하지 않는 postId로 삭제 요청을 하면 오류가 발생해야 한다.")
    @Test
    void delete_invalidPostId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Long invalidPostId = 9998659134999L;
        usersRepository.save(user);
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.delete(invalidPostId, user.getUserId()));
    }

    @DisplayName("게시글의 주인이 아닌 유저가 삭제를 요청하면 오류가 발생해야 한다.")
    @Test
    void delete_notOwner() {
        //given
        Users notOwner = EntityFactory.generateRandomUsersObject();
        Users postOwner = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(postOwner);
        usersRepository.save(postOwner);
        usersRepository.save(notOwner);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.delete(post.getPostId(), notOwner.getUserId()));
    }

    @DisplayName("post가 업데이트 되어야 한다.")
    @Test
    void update() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Long postCategoryId = postCategoryRepository.save(post.getPostCategory()).getPostCategoryId();
        Long userId = usersRepository.save(user).getUserId();
        Long postId = postRepository.save(post).getPostId();
        LocalDateTime updateTime = LocalDateTime.parse("2022-11-10T12:39:00");
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정제목", "수정내용", updateTime);
        //when
        postService.update(postId, postUpdateRequest, userId);
        //then
        Post findPost = postRepository.findById(postId).get();
        Assertions.assertThat(findPost.getPostSubject()).isEqualTo("수정제목");
        Assertions.assertThat(findPost.getPostContent()).isEqualTo("수정내용");
        Assertions.assertThat(findPost.getUpdatedAt()).isEqualTo(updateTime);
    }

    @DisplayName("존재하지 않는 postId로 갱신 요청을 하면 오류가 발생해야 한다.")
    @Test
    void update_invalidPostId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Long invalidPostId = 999999999L;
        LocalDateTime updateTime = LocalDateTime.parse("2022-11-10T12:39:00");
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정제목", "수정내용", updateTime);
        usersRepository.save(user);
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.update(invalidPostId, postUpdateRequest, user.getUserId()));
    }

    @DisplayName("게시글의 주인이 아닌 유저가 갱신을 요청하면 오류가 발생해야 한다.")
    @Test
    void update_notOwner() {
        //given
        Users notOwner = EntityFactory.generateRandomUsersObject();
        Users postOwner = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(postOwner);
        usersRepository.save(postOwner);
        usersRepository.save(notOwner);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        PostUpdateRequest postUpdateRequest = new PostUpdateRequest("수정제목", "수정내용", LocalDateTime.now());
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.update(post.getPostId(), postUpdateRequest, notOwner.getUserId()));
    }

    @DisplayName("게시글 페이징이 정상적으로 수행되어야 한다.")
    @Test
    void paginationTest() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        PostCategory postCategory = EntityFactory.generatePostCategoryObject(Categories.FREE);
        Post post1 = EntityFactory.generateRandomPostObject(user, postCategory);
        Post post2 = EntityFactory.generateRandomPostObject(user, postCategory);
        Post post3 = EntityFactory.generateRandomPostObject(user, postCategory);
        Comment comment1 = EntityFactory.generateCommentObject(post1, user);
        Comment comment2 = EntityFactory.generateCommentObject(post1, user);
        Comment comment3 = EntityFactory.generateCommentObject(post1, user);
        usersRepository.save(user);
        postCategoryRepository.save(postCategory);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        int pageSize = 2;
        PageRequest pageable_1 = PageRequest.of(0, pageSize);
        PageRequest pageable_2 = PageRequest.of(1, pageSize);
        //when
        PostsPageResponse page_1 = postService.pagination(postCategory.getPostCategoryId(), pageable_1);
        PostsPageResponse page_2 = postService.pagination(postCategory.getPostCategoryId(), pageable_2);
        //then
        Assertions.assertThat(page_1.getPostPageResponseList().size()).isEqualTo(2);
        Assertions.assertThat(page_1.getPostPageResponseList().get(0).getPostId()).isEqualTo(post1.getPostId());
        Assertions.assertThat(page_1.getPostPageResponseList().get(1).getPostId()).isEqualTo(post2.getPostId());
        Assertions.assertThat(page_1.getCategoryId()).isEqualTo(postCategory.getPostCategoryId());
        Assertions.assertThat(page_1.getPostPageResponseList().get(0).getCommentCount()).isEqualTo(3);
        Assertions.assertThat(page_1.getPostPageResponseList().get(1).getCommentCount()).isEqualTo(0);

        Assertions.assertThat(page_2.getPostPageResponseList().size()).isEqualTo(1);
    }

    @DisplayName("너무 많은(100개 초과) 게시글을 페이징 요청하면 오류가 발생해야 한다.")
    @Test
    void pagination_tooBigRequestSize() {
        //given
        PageRequest pageable = PageRequest.of(0, 101);
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.pagination(1L, pageable));
    }

    @DisplayName("추천하려는 postId의 게시물이 존재하지 않으면 오류가 발생해야 한다.")
    @Test
    void postRecommend_invalidPostId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        usersRepository.save(user);
        Long invalidPostId = 9999999999L;

        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.postRecommend(user.getUserId(), invalidPostId));
    }

    @DisplayName("추천하려는 게시물이 이미 추천한 게시물이면 오류가 발생해야 한다.")
    @Test
    void postRecommend_alreadyRecommended() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        postService.postRecommend(user.getUserId(), post.getPostId());
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.postRecommend(user.getUserId(), post.getPostId()));
    }

    @DisplayName("추천하려는 user의 userId가 존재하지 않으면 오류가 발생해야 한다.")
    @Test
    void postRecommend_invalidUserId() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);

        Long invalidUserId = 9999999999999L;
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.postRecommend(invalidUserId, post.getPostId()));
    }

    @DisplayName("게시글 추천이 수행되어야 한다.")
    @Test
    void postRecommend() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        //when
        postService.postRecommend(user.getUserId(), post.getPostId());
        //then
        Assertions.assertThat(postRecommendRepository.countByPostId(post.getPostId())).isEqualTo(1);
    }


    @DisplayName("유저 개인 게시글 조회가 수행되어야 한다.")
    @Test
    void userPosts() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post0 = EntityFactory.generateRandomPostObject(user);
        Post post1 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        Post post2 = EntityFactory.generateRandomPostObject(user, post0.getPostCategory());
        usersRepository.save(user);
        postCategoryRepository.save(post1.getPostCategory());
        postRepository.save(post0);
        postRepository.save(post1);
        postRepository.save(post2);
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        PersonalPostsResponse userPosts = postService.userPosts(user.getUserId(), pageable);
        //then
        Assertions.assertThat(userPosts.getUserId()).isEqualTo(user.getUserId());
        Assertions.assertThat(userPosts.getUserNickname()).isEqualTo(user.getUserNickname());
        Assertions.assertThat(userPosts.getPosts().size()).isEqualTo(3);

        Assertions.assertThat(userPosts.getPosts().get(0).getPostId()).isEqualTo(post0.getPostId());
        Assertions.assertThat(userPosts.getPosts().get(1).getPostId()).isEqualTo(post1.getPostId());
        Assertions.assertThat(userPosts.getPosts().get(2).getPostId()).isEqualTo(post2.getPostId());

        Assertions.assertThat(userPosts.getPosts().get(0).getPostSubject()).isEqualTo(post0.getPostSubject());
        Assertions.assertThat(userPosts.getPosts().get(1).getPostSubject()).isEqualTo(post1.getPostSubject());
        Assertions.assertThat(userPosts.getPosts().get(2).getPostSubject()).isEqualTo(post2.getPostSubject());

    }


    @DisplayName("존재하지 않는 userId로 개인 게시글 조회를 요청하면 오류가 발생해야 한다.")
    @Test
    void userPosts_invalidUserId() {
        //given
        Long invalidUserId = 99999999999123123L;
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.userPosts(invalidUserId, pageable));
    }

    @DisplayName("개인 게시글 조회를 수행하면 각 게시글의 댓글 count가 정확하게 반환되어야 한다.")
    @Test
    void userPosts_commentCount() {
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Comment comment0 = EntityFactory.generateCommentObject(post, user);
        Comment comment1 = EntityFactory.generateCommentObject(post, user);
        Comment comment2 = EntityFactory.generateCommentObject(post, user);
        Comment comment3 = EntityFactory.generateCommentObject(post, user);
        Comment comment4 = EntityFactory.generateCommentObject(post, user);
        Comment comment5 = EntityFactory.generateCommentObject(post, user);
        Comment comment6 = EntityFactory.generateCommentObject(post, user);
        Comment comment7 = EntityFactory.generateCommentObject(post, user);
        Comment comment8 = EntityFactory.generateCommentObject(post, user);
        Comment comment9 = EntityFactory.generateCommentObject(post, user);
        Comment comment10 = EntityFactory.generateCommentObject(post, user);
        Comment comment11 = EntityFactory.generateCommentObject(post, user);

        usersRepository.save(user);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);
        commentRepository.save(comment0);
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        commentRepository.save(comment5);
        commentRepository.save(comment6);
        commentRepository.save(comment7);
        commentRepository.save(comment8);
        commentRepository.save(comment9);
        commentRepository.save(comment10);
        commentRepository.save(comment11);
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        PersonalPostsResponse userPosts = postService.userPosts(user.getUserId(), pageable);
        //then
        Assertions.assertThat(userPosts.getPosts().get(0).getCommentCount()).isEqualTo(12);
    }

    @DisplayName("개인 게시글 조회를 수행하면 각 게시글의 추천 count가 정확하게 반환되어야 한다.")
    @Test
    void userPosts_recommendCount() {
        //given
        Users user = EntityFactory.generateRandomUsersObject();
        Post post = EntityFactory.generateRandomPostObject(user);
        Users recommendUser0 = EntityFactory.generateRandomUsersObject();
        Users recommendUser1 = EntityFactory.generateRandomUsersObject();
        Users recommendUser2 = EntityFactory.generateRandomUsersObject();
        usersRepository.save(user);
        usersRepository.save(recommendUser0);
        usersRepository.save(recommendUser1);
        usersRepository.save(recommendUser2);
        postCategoryRepository.save(post.getPostCategory());
        postRepository.save(post);

        postService.postRecommend(recommendUser0.getUserId(), post.getPostId());
        postService.postRecommend(recommendUser1.getUserId(), post.getPostId());
        postService.postRecommend(recommendUser2.getUserId(), post.getPostId());

        PageRequest pageable = PageRequest.of(0, 10);
        //when
        PersonalPostsResponse userPosts = postService.userPosts(user.getUserId(), pageable);
        //then
        Assertions.assertThat(userPosts.getPosts().get(0).getRecommendCount()).isEqualTo(3);
    }

    @DisplayName("게시글 검색이 정상적으로 수행되어야 한다.")
    @Test
    void searchPosts() {
        //given
        Users users = EntityFactory.generateRandomUsersObject();
        Users recommendUser = EntityFactory.generateRandomUsersObject();
        Post post0 = EntityFactory.generateRandomPostObject(users);
        Post post1 = EntityFactory.generateRandomPostObject(users, post0.getPostCategory());
        Post post2 = EntityFactory.generateRandomPostObject(users, post0.getPostCategory());
        Post post3 = EntityFactory.generateRandomPostObject(users, post0.getPostCategory());
        Comment comment = EntityFactory.generateCommentObject(post0, recommendUser);

        usersRepository.save(users);
        usersRepository.save(recommendUser);
        postCategoryRepository.save(post0.getPostCategory());
        postRepository.save(post0);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        commentRepository.save(comment);

        postService.postRecommend(recommendUser.getUserId(), post0.getPostId());
        PostSearchRequest postSearchRequest = new PostSearchRequest(users.getUserNickname(), LocalDateTime.of(1900, 01, 01, 01, 01, 01), LocalDateTime.of(2022, 12, 31, 01, 01, 01), post0.getPostCategory().getPostCategoryId());
        //when
        PostSearchResponse posts = postService.searchPosts(postSearchRequest);
        //then
        Assertions.assertThat(posts.getSearchCount()).isEqualTo(4);
        Assertions.assertThat(posts.getPosts().size()).isEqualTo(4);

        Assertions.assertThat(posts.getPosts().get(0).getPostId()).isEqualTo(post0.getPostId());
        Assertions.assertThat(posts.getPosts().get(1).getPostId()).isEqualTo(post1.getPostId());
        Assertions.assertThat(posts.getPosts().get(2).getPostId()).isEqualTo(post2.getPostId());
        Assertions.assertThat(posts.getPosts().get(3).getPostId()).isEqualTo(post3.getPostId());

        Assertions.assertThat(posts.getPosts().get(0).getRecommendCount()).isEqualTo(1);

        Assertions.assertThat(posts.getPosts().get(0).getCommentCount()).isEqualTo(1);

    }

    @DisplayName("존재하지 않는 userId로 추천한 게시물 조회를 요청하면 오류가 발생해야 한다.")
    @Test
    void findRecommendedPost_invalidUserId() {
        //given
        Long invalidUserId = 999999999999L;
        PageRequest pageable = PageRequest.of(0, 10);
        //when
        //then
        Assertions.assertThatThrownBy(() -> postService.findRecommendedPost(invalidUserId, pageable));
    }

    @DisplayName("추천한 게시물 조회를 수행해야 한다.")
    @Test
    void findRecommendedPost() {
        //given
        Users recommendSearchUser = EntityFactory.generateRandomUsersObject();

        Users postOwner0 = EntityFactory.generateRandomUsersObject();
        Users postOwner1 = EntityFactory.generateRandomUsersObject();
        Users postOwner2 = EntityFactory.generateRandomUsersObject();
        Users postOwner3 = EntityFactory.generateRandomUsersObject();
        Users postOwner4 = EntityFactory.generateRandomUsersObject();
        Users postOwner5 = EntityFactory.generateRandomUsersObject();
        Users postOwner6 = EntityFactory.generateRandomUsersObject();
        Users postOwner7 = EntityFactory.generateRandomUsersObject();
        Users postOwner8 = EntityFactory.generateRandomUsersObject();
        Users postOwner9 = EntityFactory.generateRandomUsersObject();
        Users postOwner10 = EntityFactory.generateRandomUsersObject();
        Users postOwner11 = EntityFactory.generateRandomUsersObject();

        Post post0 = EntityFactory.generateRandomPostObject(postOwner0);
        Post post1 = EntityFactory.generateRandomPostObject(postOwner1, post0.getPostCategory());
        Post post2 = EntityFactory.generateRandomPostObject(postOwner2, post0.getPostCategory());
        Post post3 = EntityFactory.generateRandomPostObject(postOwner3, post0.getPostCategory());
        Post post4 = EntityFactory.generateRandomPostObject(postOwner4, post0.getPostCategory());
        Post post5 = EntityFactory.generateRandomPostObject(postOwner5, post0.getPostCategory());
        Post post6 = EntityFactory.generateRandomPostObject(postOwner6, post0.getPostCategory());
        Post post7 = EntityFactory.generateRandomPostObject(postOwner7, post0.getPostCategory());
        Post post8 = EntityFactory.generateRandomPostObject(postOwner8, post0.getPostCategory());
        Post post9 = EntityFactory.generateRandomPostObject(postOwner9, post0.getPostCategory());
        Post post10 = EntityFactory.generateRandomPostObject(postOwner10, post0.getPostCategory());
        Post post11 = EntityFactory.generateRandomPostObject(postOwner11, post0.getPostCategory());

        usersRepository.save(recommendSearchUser);
        usersRepository.save(postOwner0);
        usersRepository.save(postOwner1);
        usersRepository.save(postOwner2);
        usersRepository.save(postOwner3);
        usersRepository.save(postOwner4);
        usersRepository.save(postOwner5);
        usersRepository.save(postOwner6);
        usersRepository.save(postOwner7);
        usersRepository.save(postOwner8);
        usersRepository.save(postOwner9);
        usersRepository.save(postOwner10);
        usersRepository.save(postOwner11);

        postCategoryRepository.save(post0.getPostCategory());
        postRepository.save(post0);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);
        postRepository.save(post5);
        postRepository.save(post6);
        postRepository.save(post7);
        postRepository.save(post8);
        postRepository.save(post9);
        postRepository.save(post10);
        postRepository.save(post11);

        postService.postRecommend(recommendSearchUser.getUserId(), post0.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post1.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post2.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post3.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post4.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post5.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post6.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post7.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post8.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post9.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post10.getPostId());
        postService.postRecommend(recommendSearchUser.getUserId(), post11.getPostId());

        int pageSize = 10;
        PageRequest pageable = PageRequest.of(0, pageSize);
        //when
        PostRecommendsResponse recommendedPost = postService.findRecommendedPost(recommendSearchUser.getUserId(), pageable);
        //then
        List<PostRecommendsResponse.SinglePost> posts = recommendedPost.getPosts();
        for (PostRecommendsResponse.SinglePost post : posts) {
            System.out.println("post = " + post);
        }
        Assertions.assertThat(recommendedPost.getSearchCount()).isEqualTo(pageSize);

        Assertions.assertThat(recommendedPost.getPosts().get(0).getPostId()).isEqualTo(post0.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(1).getPostId()).isEqualTo(post1.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(2).getPostId()).isEqualTo(post2.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(3).getPostId()).isEqualTo(post3.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(4).getPostId()).isEqualTo(post4.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(5).getPostId()).isEqualTo(post5.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(6).getPostId()).isEqualTo(post6.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(7).getPostId()).isEqualTo(post7.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(8).getPostId()).isEqualTo(post8.getPostId());
        Assertions.assertThat(recommendedPost.getPosts().get(9).getPostId()).isEqualTo(post9.getPostId());

        Assertions.assertThat(recommendedPost.getPosts().get(0).getPostSubject()).isEqualTo(post0.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(1).getPostSubject()).isEqualTo(post1.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(2).getPostSubject()).isEqualTo(post2.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(3).getPostSubject()).isEqualTo(post3.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(4).getPostSubject()).isEqualTo(post4.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(5).getPostSubject()).isEqualTo(post5.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(6).getPostSubject()).isEqualTo(post6.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(7).getPostSubject()).isEqualTo(post7.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(8).getPostSubject()).isEqualTo(post8.getPostSubject());
        Assertions.assertThat(recommendedPost.getPosts().get(9).getPostSubject()).isEqualTo(post9.getPostSubject());


        Assertions.assertThat(recommendedPost.getPosts().get(0).getUserId()).isEqualTo(post0.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(1).getUserId()).isEqualTo(post1.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(2).getUserId()).isEqualTo(post2.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(3).getUserId()).isEqualTo(post3.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(4).getUserId()).isEqualTo(post4.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(5).getUserId()).isEqualTo(post5.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(6).getUserId()).isEqualTo(post6.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(7).getUserId()).isEqualTo(post7.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(8).getUserId()).isEqualTo(post8.getUser().getUserId());
        Assertions.assertThat(recommendedPost.getPosts().get(9).getUserId()).isEqualTo(post9.getUser().getUserId());

        Assertions.assertThat(recommendedPost.getPosts().get(0).getUserNickname()).isEqualTo(post0.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(1).getUserNickname()).isEqualTo(post1.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(2).getUserNickname()).isEqualTo(post2.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(3).getUserNickname()).isEqualTo(post3.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(4).getUserNickname()).isEqualTo(post4.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(5).getUserNickname()).isEqualTo(post5.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(6).getUserNickname()).isEqualTo(post6.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(7).getUserNickname()).isEqualTo(post7.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(8).getUserNickname()).isEqualTo(post8.getUser().getUserNickname());
        Assertions.assertThat(recommendedPost.getPosts().get(9).getUserNickname()).isEqualTo(post9.getUser().getUserNickname());
    }





}
