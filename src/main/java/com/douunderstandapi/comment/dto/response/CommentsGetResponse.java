package com.douunderstandapi.comment.dto.response;

import com.douunderstandapi.comment.dto.CommentDTO;
import java.util.List;

public record CommentsGetResponse(
        List<CommentDTO> comments
) {
}
