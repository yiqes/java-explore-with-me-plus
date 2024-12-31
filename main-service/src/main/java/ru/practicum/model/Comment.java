package ru.practicum.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    Long Id;
    @ManyToOne
    @JoinColumn(name = "author_id")
    User author;
    @ManyToOne
    @JoinColumn(name = "event_id")
    Event event;
    String text;
    LocalDateTime created;
    LocalDateTime updated;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    Comment parent;
}
