package com.douunderstandapi.post.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.BOOLEAN;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.douunderstandapi.annotation.WithUserPrincipals;
import com.douunderstandapi.post.dto.request.PostAddRequest;
import com.douunderstandapi.post.dto.request.PostUpdateRequest;
import com.douunderstandapi.post.dto.response.PostAddResponse;
import com.douunderstandapi.post.dto.response.PostGetResponse;
import com.douunderstandapi.post.dto.response.PostUpdateResponse;
import com.douunderstandapi.post.service.PostService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

@DisplayName("[컨트롤러] - 포스트")
@WebMvcTest(PostController.class)
@AutoConfigureRestDocs
class PostControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    private PostService postService;

    @DisplayName("{POST} 포스트 등록 - 정상호출")
    @WithUserPrincipals
    @Test
    void addPost() throws Exception {
        String title = "RESTful API";
        String content = "Restful API란.....";
        String link = "https://abcdefssss/2in2";
        JSONArray categoryNames = new JSONArray();
        categoryNames.put("java");

        JSONObject request = new JSONObject();
        request.put("title", title);
        request.put("content", content);
        request.put("link", link);
        request.put("categoryNames", categoryNames);

        when(postService.addPost(anyString(), any(PostAddRequest.class)))
                .thenReturn(createPostAddResponse(title, content, link));

        mvc.perform(
                        RestDocumentationRequestBuilders.post("/api/v1/posts")
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "add-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("content").type(STRING).description("컨텐츠"),
                                        fieldWithPath("link").type(STRING).description("관련링크"),
                                        fieldWithPath("categoryNames").type(ARRAY).description("카테고리 목록")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("포스트 ID"),
                                        fieldWithPath("userEmail")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("commentCount")
                                                .type(NUMBER)
                                                .description(0),
                                        fieldWithPath("categoryName")
                                                .type(STRING)
                                                .description("카테고리 이름")
                                )
                        )
                )
                .andExpect(status().isCreated());
    }

    @DisplayName("{GET} 포스트 조회(상세) - 정상호출")
    @WithUserPrincipals
    @Test
    void getPost() throws Exception {
        String title = "RESTful API";
        String content = "Restful API란.....";
        String link = "https://abcdefssss/2in2";
        when(postService.findPost(anyString(), any(Long.class)))
                .thenReturn(createPostGetResponse(title, content, link));

        mvc.perform(
                        RestDocumentationRequestBuilders.get("/api/v1/posts/{postId}", 1)
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString()))
                .andDo(print())
                .andDo(
                        document(
                                "get-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("게시글 ID"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("userEmail")
                                                .type(STRING)
                                                .description("작성자 이메일"),
                                        fieldWithPath("userId")
                                                .type(NUMBER)
                                                .description("작성자 ID"),
                                        fieldWithPath("createdAt")
                                                .type(STRING)
                                                .description("작성일시"),
                                        fieldWithPath("commentCount")
                                                .type(NUMBER)
                                                .description("코멘트 카운트"),
                                        fieldWithPath("categoryName")
                                                .type(STRING)
                                                .description("카테고리 이름")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{PUT} 포스트 업데이트 - 정상호출")
    @WithUserPrincipals
    @Test
    void updatePost() throws Exception {
        String title = "RESTful API";
        String content = "Restful API란.....";
        String link = "https://abcdefssss/2in2";
        String categoryName = "spring";

        JSONObject request = new JSONObject();
        request.put("title", title);
        request.put("content", content);
        request.put("link", link);
        request.put("categoryName", categoryName);

        when(postService.update(anyString(), any(Long.class), any(PostUpdateRequest.class)))
                .thenReturn(createKnowledgeUpdateResponse(title, content, link, categoryName));

        mvc.perform(
                        RestDocumentationRequestBuilders.put("/api/v1/posts/{postId}", 1)
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(request.toString()))
                .andDo(print())
                .andDo(
                        document(
                                "update-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint()),
                                requestFields(
                                        fieldWithPath("title").type(STRING).description("제목"),
                                        fieldWithPath("content").type(STRING).description("컨텐츠"),
                                        fieldWithPath("link").type(STRING).description("관련링크"),
                                        fieldWithPath("categoryName").type(STRING).description("카테고리 이름")
                                ),
                                responseFields(
                                        fieldWithPath("id")
                                                .type(NUMBER)
                                                .description("포스트 ID"),
                                        fieldWithPath("title")
                                                .type(STRING)
                                                .description("제목"),
                                        fieldWithPath("content")
                                                .type(STRING)
                                                .description("컨텐츠"),
                                        fieldWithPath("link")
                                                .type(STRING)
                                                .description("관련링크"),
                                        fieldWithPath("commentCount")
                                                .type(NUMBER)
                                                .description("코멘트 카운트"),
                                        fieldWithPath("createdAt")
                                                .type(STRING)
                                                .description("작성일시"),
                                        fieldWithPath("userId")
                                                .type(NUMBER)
                                                .description("유저 ID"),
                                        fieldWithPath("userEmail")
                                                .type(STRING)
                                                .description("이메일"),
                                        fieldWithPath("categoryName")
                                                .type(STRING)
                                                .description("카테고리 이름"),
                                        fieldWithPath("subscribeMe")
                                                .type(BOOLEAN)
                                                .description("구독여부")
                                )
                        )
                )
                .andExpect(status().isOk());
    }

    @DisplayName("{DELETE} 포스트삭제 - 정상호출")
    @WithUserPrincipals
    @Test
    void deletePost() throws Exception {
        when(postService.delete(anyString(), any(Long.class)))
                .thenReturn(Boolean.TRUE);

        mvc.perform(
                        RestDocumentationRequestBuilders.delete("/api/v1/posts/{postId}", 1)
                                .with(csrf().asHeader())
                                .header(HttpHeaders.AUTHORIZATION, UUID.randomUUID().toString()))
                .andDo(print())
                .andDo(
                        document(
                                "delete-post",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                )
                .andExpect(status().isOk());
    }

    private PostAddResponse createPostAddResponse(String title, String content, String link) {
        return PostAddResponse.of(1L, "test@gmail.com", title, content, link, "java");
    }

    private PostGetResponse createPostGetResponse(String title, String content, String link) {
        return PostGetResponse.builder()
                .id(1L)
                .userId(1L)
                .userEmail("test@gmail.com")
                .title(title)
                .content(content)
                .link(link)
                .createdAt(LocalDateTime.now().toString())
                .commentCount(1L)
                .categoryName("java")
                .build();
    }

    private PostUpdateResponse createKnowledgeUpdateResponse(String title, String content, String link,
                                                             String categoryName) {
        return PostUpdateResponse.builder()
                .id(1L)
                .title(title)
                .content(content)
                .link(link)
                .commentCount(1L)
                .createdAt(LocalDateTime.now().toString())
                .userId(1L)
                .userEmail("test@gmail.com")
                .subscribeMe(true)
                .categoryName(categoryName)
                .build();
    }
}