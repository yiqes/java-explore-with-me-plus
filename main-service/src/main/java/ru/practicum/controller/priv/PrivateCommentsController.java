package ru.practicum.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;
import ru.practicum.service.comment.CommentService;

import java.util.List;

@RestController
@RequestMapping("comment")
@AllArgsConstructor
@Validated
@Slf4j
public class PrivateCommentsController {

    private final CommentService commentService;

    @PostMapping("/event/{event-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentFullDto createComment(@PathVariable("event-id") @NotNull Long eventId,
                                        @RequestParam @NotNull Long userId,
                                        @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Private: post comment {}, userId {}, eventId {}", newCommentDto, userId, eventId);

        return commentService.createComment(newCommentDto, eventId, userId);
    }

    @GetMapping("/{comment-id}")
    public CommentFullDto getComment(@PathVariable("comment-id") @NotNull Long commentId,
                                 @RequestParam @NotNull Long userId) {
        log.info("Private: get comment {}, userId {}", commentId, userId);
        return commentService.getComment(commentId, userId);
    }


    @PatchMapping("/{comment-id}")
    public CommentDto updateComment(@PathVariable("comment-id") @NotNull Long commentId,
                                    @RequestParam @NotNull Long userId,
                                    @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("Private: patch comment {}, userId {}", updateCommentDto, userId);
        return commentService.updateComment(commentId, userId, updateCommentDto);
    }


    @GetMapping("/user/{user-id}")
    public List<CommentDto> getAllCommentsForUser(@PathVariable("user-id") @NotNull Long userId,
                                                  @RequestParam(defaultValue = "0", required = false) Integer from,
                                                  @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("Private: get comments for user {}, from {}, size {}", userId, from, size);
        return commentService.getAllCommentsForUser(userId, from, size);
    }

    @DeleteMapping("/{comment-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("comment-id") @NotNull Long commentId,
                              @RequestParam @NotNull Long userId) {
        log.info("Private: delete comment {}, userId {}", commentId, userId);
        commentService.deleteComment(commentId, userId);
    }

}
