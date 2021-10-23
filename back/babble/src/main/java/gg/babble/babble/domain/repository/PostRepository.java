package gg.babble.babble.domain.repository;

import gg.babble.babble.domain.post.Category;
import gg.babble.babble.domain.post.Post;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndDeletedFalse(final Long id);

    List<Post> findByCategory(Category category);

    List<Post> findByTitleLikeAndDeletedFalseOrderByCreatedAtDesc(final String keyword);

    @Query("select p "
        + "from Post p "
        + "where p.deleted = false and (p.title like ?1 or p.content like ?1) "
        + "order by p.createdAt desc ")
    List<Post> findByTitleLikeOrContentLikeAndDeletedFalseOrderByCreatedAtDesc(final String keyword);

    List<Post> findByAccount_NicknameLikeAndDeletedFalseOrderByCreatedAtDesc(final String keyword);

    @Query("select p "
        + "from Post p "
        + "where p.deleted = false and (p.title like ?1 or p.content like ?1 or p.account.nickname like ?1) "
        + "order by p.createdAt desc ")
    List<Post> findByTitleLikeOrContentLikeAndDeletedFalseOrderByCreatedAt(final String keyword);
}
