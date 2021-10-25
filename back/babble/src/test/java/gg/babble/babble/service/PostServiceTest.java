package gg.babble.babble.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gg.babble.babble.ApplicationTest;
import gg.babble.babble.domain.post.Post;
import gg.babble.babble.dto.request.post.PostCreateRequest;
import gg.babble.babble.dto.request.post.PostDeleteRequest;
import gg.babble.babble.dto.request.post.PostUpdateRequest;
import gg.babble.babble.dto.response.PostBaseResponse;
import gg.babble.babble.dto.response.PostResponse;
import gg.babble.babble.dto.response.PostWithoutContentResponse;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class PostServiceTest extends ApplicationTest {

    private static final Pageable PAGEABLE = PageRequest.of(0, 100);

    @Autowired
    private PostService postService;

    private Post post1;
    private Post post2;
    private Post post3;
    private Post post4;
    private Post post5;
    private Post post6;

    @BeforeEach
    public void setUp() {
        post1 = postRepository.save(new Post("이것은 처음쓴 글",
            "안녕하세요~!!!\n저는 첫 번째 글의 주인공이랍니다.",
            "자유",
            "조용히좋아요를눌러라",
            "123456"));
        post2 = postRepository.save(new Post("나랑 밥먹을 사람?",
            "하이루 방가링가\n오늘은 중식이 땡기는구만\n어디 좋은 중국집 아는데 있는사람 추천좀 ㅎㅎ",
            "건의",
            "멘보샤",
            "234567"));
        post3 = postRepository.save(new Post("니들이 게맛을 알아?",
            "간장게장 먹고싶다.\n누가 나 좀 간장게장좀 사줘~",
            "자유",
            "밥도둑",
            "345678"));
        post4 = postRepository.save(new Post("게임 좀 추가해줘요.",
            "",
            "게임",
            "게임이좋아",
            "123456"));
        post5 = postRepository.save(new Post("간장게장보단 양념게장이지~",
            "간장게장은 비린맛 때매 호불호 갈림 ㅋㅋ\n우리의 킹념개장을 찬양하라!!",
            "자유",
            "멘보샤",
            "234567"));
        post6 = postRepository.save(new Post("게임할 사람 구함",
            "오늘은 날씨가 좋네요.\n저랑 같이 게임할 사람?",
            "게임",
            "킹장게장",
            "123123"));
    }

    @DisplayName("게시글을 추가한다.")
    @Test
    void create() {
        //given
        PostCreateRequest request = new PostCreateRequest("테스트를 위해 작성된 게시글",
            "테스트가 과연 성공할 수 있을까요?\n이 테스트가 성공한다면 엄청난 일이 일어날지도...?",
            "자유",
            "테스터",
            "123456");

        //when
        PostResponse response = postService.create(request).toPostResponse();
        PostResponse expected = new PostResponse(1L,
            "테스트를 위해 작성된 게시글",
            "테스트가 과연 성공할 수 있을까요?\n이 테스트가 성공한다면 엄청난 일이 일어날지도...?",
            "자유",
            "테스터",
            LocalDateTime.now().toString(),
            LocalDateTime.now().toString(),
            false,
            0L,
            0L);

        //then
        assertThat(response).usingRecursiveComparison().ignoringFields("id", "createdAt", "updatedAt").isEqualTo(expected);
    }

    @DisplayName("게시글을 수정한다.")
    @Test
    void update() {
        //given
        PostResponse expected = new PostResponse(post1.getId(),
            "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "조용히좋아요를눌러라",
            post1.createdAt(),
            post1.updatedAt(),
            post1.isNotice(),
            0L,
            0L);
        PostUpdateRequest request = new PostUpdateRequest(post1.getId(), "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "123456");

        //when
        PostResponse response = postService.update(request).toPostResponse();

        //then
        assertThat(response).usingRecursiveComparison().ignoringFields("updatedAt").isEqualTo(expected);
    }

    @DisplayName("잘못된 비밀번호로 수정 요청을 하면 예외가 발생한다.")
    @Test
    void updateWithWrongPassword() {
        PostResponse expected = new PostResponse(post1.getId(),
            "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "조용히좋아요를눌러라",
            post1.createdAt(),
            post1.updatedAt(),
            post1.isNotice(),
            0L,
            0L);
        PostUpdateRequest request = new PostUpdateRequest(post1.getId(), "이것은 수정된 글",
            "수정된 글이지롱~",
            "건의",
            "1234567");

        //then
        assertThatThrownBy(() -> postService.update(request)).isInstanceOf(BabbleIllegalArgumentException.class);
    }

    @DisplayName("조회를 하면 view 가 증가한다.")
    @Test
    void viewCount() {
        //when
        postService.findByIdAndIncreaseView(post1.getId());
        postService.findByIdAndIncreaseView(post1.getId());
        postService.findByIdAndIncreaseView(post1.getId());
        postService.findByIdAndIncreaseView(post1.getId());
        PostResponse response = postService.findByIdAndIncreaseView(post1.getId()).toPostResponse();

        //then
        assertThat(response.getView()).isEqualTo(5L);
    }

    @DisplayName("좋아요를 하면 like 가 증가한다.")
    @Test
    void likeCount() {
        //when
        postService.increaseLike(post1.getId());
        postService.increaseLike(post1.getId());
        PostResponse response = postService.increaseLike(post1.getId()).toPostResponse();

        //then
        assertThat(response.getLike()).isEqualTo(3L);
    }

    @DisplayName("원하는 제목로 검색을 한다.")
    @Test
    void findByTitle() {
        //when
        PostBaseResponse postBaseResponse = postService.search("제목", "간장게장");

        //then
        List<PostWithoutContentResponse> results = postBaseResponse.toPostSearchResponse().getResults();
        assertThat(results).hasSize(1);
        PostWithoutContentResponse response = results.get(0);

        assertThat(response).usingRecursiveComparison().isEqualTo(PostResponse.from(post5));
    }

    @DisplayName("원하는 내용으로 검색을 한다.")
    @Test
    void findByContent() {
        //when
        PostBaseResponse response = postService.search("제목, 내용", "간장게장");

        //then
        assertThat(response.toPostSearchResponse().getResults()).hasSize(2);
    }

    @DisplayName("원하는 작성자로 검색을 한다.")
    @Test
    void findByAuthor() {
        //when
        PostBaseResponse response = postService.search("작성자", "멘보샤");

        //then
        assertThat(response.toPostSearchResponse().getResults()).hasSize(2);
    }

    @DisplayName("제목 + 내용 + 작성자를 모두 포함한 단어로 검색을 한다.")
    @Test
    void findByAll() {
        //when
        PostBaseResponse response = postService.search("제목, 내용, 작성자", "게장");

        //then
        assertThat(response.toPostSearchResponse().getResults()).hasSize(3);
    }

    @DisplayName("카테고리로 검색을 한다.")
    @Test
    void findByCategory() {
        //when
        List<PostWithoutContentResponse> responses = postService.findByCategory("자유").toPostWithoutContentResponse();

        //then
        assertThat(responses).hasSize(3);
    }

    @DisplayName("게시글을 조회한다.")
    @Test
    void findById() {
        //when
        PostResponse response = postService.findByIdAndIncreaseView(post4.getId()).toPostResponse();

        //then
        assertThat(response).usingRecursiveComparison().isEqualTo(PostResponse.from(post4));
    }

    @DisplayName("전체 게시글을 조회한다.")
    @Test
    void findAll() {
        //when
        List<PostWithoutContentResponse> responses = postService.findAllWithPagination(PAGEABLE).toPostWithoutContentResponse();

        //then
        assertThat(responses).hasSize(6);
    }

    @DisplayName("페이지네이션된 첫 번째 페이지 게시글을 조회한다.")
    @Test
    void findWithPagination() {
        //when
        List<PostWithoutContentResponse> responses = postService.findAllWithPagination(PageRequest.of(0, 5)).toPostWithoutContentResponse();

        //then
        assertThat(responses).hasSize(5);
    }

    @DisplayName("페이지네이션된 두 번째 페이지 게시글을 조회한다.")
    @Test
    void findWithPaginationSecondPage() {
        //when
        List<PostWithoutContentResponse> responses = postService.findAllWithPagination(PageRequest.of(1, 5)).toPostWithoutContentResponse();

        //then
        assertThat(responses).hasSize(1);
    }

    @DisplayName("게시글 삭제 후 페이지네이션된 게시글을 조회한다.")
    @Test
    void findWithPaginationAfterDeletedPost() {
        //when
        postService.delete(new PostDeleteRequest(post1.getId(), "123456"));
        postService.delete(new PostDeleteRequest(post2.getId(), "234567"));
        List<PostWithoutContentResponse> responses = postService.findAllWithPagination(PageRequest.of(0, 5)).toPostWithoutContentResponse();

        //then
        assertThat(responses).hasSize(4);
    }

    @DisplayName("게시글을 삭제한다.")
    @Test
    void delete() {
        //given
        PostDeleteRequest request = new PostDeleteRequest(post2.getId(), "234567");

        //when
        postService.delete(request);

        //then
        List<PostWithoutContentResponse> responses = postService.findAllWithPagination(PAGEABLE).toPostWithoutContentResponse();
        assertThat(responses).hasSize(5);
    }

    @DisplayName("잘못된 비밀번호로 삭제 요청을 하면 예외가 발생한다.")
    @Test
    void deleteWithWrongPassword() {
        //given
        PostDeleteRequest request = new PostDeleteRequest(post2.getId(), "1234567");

        //then
        assertThatThrownBy(() -> postService.delete(request)).isExactlyInstanceOf(BabbleIllegalArgumentException.class);
    }
}
