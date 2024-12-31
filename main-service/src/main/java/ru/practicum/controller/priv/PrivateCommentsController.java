package ru.practicum.controller.priv;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.dto.comment.UpdateCommentDto;

import java.util.List;

@RestController
@RequestMapping("comment")
@AllArgsConstructor
@Validated
@Slf4j
public class PrivateCommentsController {

    @PostMapping("/event/{eventId}")
    public CommentDto createComment(@PathVariable @NotNull Long eventId,
                                    @RequestParam @NotNull Long userId,
                                    @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("");
        return null;
    }

    @GetMapping("/{commentId}")
    public CommentDto getComment(@PathVariable @NotNull Long commentId,
                                 @RequestParam @NotNull Long userId) {
        log.info("");
        return null;
    }


    @PatchMapping("/{commentId}")
    public CommentDto updateComment(@PathVariable @NotNull Long commentId,
                                    @RequestParam @NotNull Long userId,
                                    @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        log.info("");
        return null;
    }


    @GetMapping("/user/{userId}")
    public List<CommentDto> getAllCommentsForUser(@PathVariable @NotNull Long userId,
                                                  @RequestParam(defaultValue = "0", required = false) Integer from,
                                                  @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("");
        return null;
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable @NotNull Long commentId,
                              @RequestParam @NotNull Long userId) {
        log.info("");
    }

}
