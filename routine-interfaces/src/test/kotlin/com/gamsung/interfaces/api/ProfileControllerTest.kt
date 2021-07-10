package com.gamsung.interfaces.api

import com.gamsung.domain.profile.Profile
import com.gamsung.domain.profile.ProfileService
import com.gamsung.interfaces.api.profile.ProfileController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
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
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.util.*

@ExtendWith(RestDocumentationExtension::class, SpringExtension::class)
@WebMvcTest(ProfileController::class)
@AutoConfigureRestDocs
class ProfileControllerTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var profileService: ProfileService

    @BeforeEach
    fun setUp(webApplicationContext: WebApplicationContext, restDocumentation: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun `Profile Get Api Test Docs`() {
        // given
        `when`(profileService.get()).thenReturn(
            Profile(
                id = UUID.randomUUID().toString(),
                name = "Test",
                profileImageUrl = "http://test.com"
            )
        )

        // when
        val resultActions = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/profile")
        ).andDo(MockMvcResultHandlers.print())

        // then
        resultActions
            .andExpect { MockMvcResultMatchers.status().isOk }
            .andDo {
                document(
                    "profile",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    responseFields(*common(), fieldWithPath("data.id").description("Profile Id"))
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