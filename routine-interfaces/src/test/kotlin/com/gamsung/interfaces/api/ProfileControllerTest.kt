package com.gamsung.interfaces.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.payload.FieldDescriptor
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@WebMvcTest(ProfileController::class)
@AutoConfigureRestDocs
class ProfileControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `Profile Get Api Test Docs`() {
        // given

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/profile")
        ).andDo(MockMvcResultHandlers.print())

        // then
        resultActions
            .andExpect { MockMvcResultMatchers.status().isOk }
            .andDo {
                document(
                    "Profile",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    responseFields(*common())
                )
            }
    }

    private fun common(): Array<FieldDescriptor> {
        return arrayOf(
            fieldWithPath("code").type(JsonFieldType.NUMBER).description("응답 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
            subsectionWithPath("error").type(JsonFieldType.OBJECT).description("에러 Data").optional(),
            subsectionWithPath("data").type(JsonFieldType.OBJECT).description("응답 Data").optional()
        )
    }
}

fun getDocumentRequest(): OperationRequestPreprocessor {
    return Preprocessors.preprocessRequest(
        modifyUris()
            .scheme("http")
            .host("gamsung.com")
            .removePort(),
        Preprocessors.prettyPrint()
    )
}

fun getDocumentResponse(): OperationResponsePreprocessor {
    return Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
}