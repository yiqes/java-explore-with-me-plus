package ru.practicum.controller.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentFullDto;

import java.util.List;

@RestController
@RequestMapping("/admin/comment")
@AllArgsConstructor
@Validated
@Slf4j
public class AdminCommentController {

    @GetMapping("/{commentId}")
    public CommentFullDto getCommentForAdmin(@PathVariable @NotNull Long commentId) {
        log.info("==> Comment with id={} for Admin was asked", commentId);
        return null;
    }

    @GetMapping("/user/{userId}")
    public List<CommentFullDto> getAllUserCommentsForAdmin(@PathVariable @NotNull Long userId,
                                                           @RequestParam(defaultValue = "0", required = false) Integer from,
                                                           @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("==> Comments for user with id={} from={} size={} for Admin was asked", userId, from, size);
        return null;
    }

    @GetMapping
    public List<CommentFullDto> findAllCommentsByTextForAdmin(@RequestParam @NotBlank String text,
                                                              @RequestParam(defaultValue = "0", required = false) Integer from,
                                                              @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("==> Comments with text={} from={} size={} for Admin was asked", text, from, size);
        return null;
    }

    @DeleteMapping("/{commentId}")
    public void deleteCommentByAdmin(@PathVariable @NotNull Long commentId) {
        log.info("==> Comments with id={} was deleted by Admin", commentId);
    }

}
