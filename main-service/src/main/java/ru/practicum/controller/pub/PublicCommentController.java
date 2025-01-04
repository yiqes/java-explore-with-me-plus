package ru.practicum.controller.pub;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
@Validated
@Slf4j
public class PublicCommentController {

    @GetMapping("/event/{eventId}")
    public List<CommentDto> getAllCommentsForEvent(@PathVariable @NotNull Long eventId,
                                                   @RequestParam(defaultValue = "0", required = false) Integer from,
                                                   @RequestParam(defaultValue = "10", required = false) Integer size) {
        return new ArrayList<>();
    }
}
