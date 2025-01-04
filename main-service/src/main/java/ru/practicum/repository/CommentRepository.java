package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(
            value = """
                    SELECT *
                    FROM comments c
                    WHERE c.event_id = :eventId
                    ORDER BY c.created DESC
                    LIMIT :size OFFSET :from
                    """,
            nativeQuery = true
    )
    List<Comment> findAllByEventId(@Param("eventId") Long eventId,
                                   @Param("from") int from,
                                   @Param("size") int size);
}
