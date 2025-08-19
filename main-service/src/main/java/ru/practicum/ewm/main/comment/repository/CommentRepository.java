package ru.practicum.ewm.main.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.comment.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByAuthor_IdOrderByPublishedOnDesc(Long userId, Pageable pageable);

    Page<Comment> findAllByAuthor_IdAndEvent_IdOrderByPublishedOnDesc(Long userId, Long eventId, Pageable pageable);

    Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

}
