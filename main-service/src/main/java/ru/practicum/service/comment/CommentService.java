package ru.practicum.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.mapper.comment.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public List<CommentDto> getAllCommentsForEvent(Long eventId, int from, int size) {
        // Проверка корректности параметров
        if (from < 0 || size <= 0) {
            throw new IllegalArgumentException("'from' должен быть >= 0, а 'size' > 0");
        }

        // Получение комментариев из репозитория
        List<Comment> comments = commentRepository.findAllByEventId(eventId, from, size);

        // Преобразование комментариев в DTO
        return comments.stream()
                .map(commentMapper::toDto)
                .toList();
    }
}
