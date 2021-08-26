package com.gamsung.api.push

import com.gamsung.domain.push.FirebaseCloudMessageService
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/push")
class FirebaseCloudMessageController(
    private val firebaseCloudMessageService: FirebaseCloudMessageService
) {
    @ApiOperation(value = "Test용 푸시")
    @PostMapping("")
    fun create() {
        val tokenList = mutableListOf(
            "cOGogWzdTIuVS1bnpaRmPy:APA91bFeeH7_ODC-ydSmAM2clTcFOhu_zGVXBKnu57bHP2Ni_RY-_JcqaWju0_TsGDSJAy3U3iJMqD_DulUm0FUFoTtiQ1lXH3V1iulBIy27XlQ32AcPDsmn3iifxFRwZcFXPQ0MmOSF",
            "fhkWKHaqYUOtv8dEFLJfH1:APA91bHAhX_uEtZ6VRTCEuN7oPXBb6DArrVIaNSWfWPWP03vj8U8ZOUEveLjXwSUsWh37F64We7RNIh523zkqakoSXUX7St4x8PDetEgY4KhQUVjdShs1EgH99ekxuZZst7zrM3QyURe",
            "dL5j9eoJSk2FLveenvX_c7:APA91bG0IFyxXoCSj8C6klzdyeesIxPAgpLuB34766FT7LlSW53jgtCuirEOXvtM7Wqxe2V5DpovLJmXzns77V-dETiEC6XbtU-KwCXS5ky0fibiv5r43xxnqisWKXiobJHy0R338PCG",
            "fgAyHcpcTzi6JUBRo8AGIG:APA91bEDEoAO8ha9Sr6Q7zqCMK9VcIO1HzNAS1z2Iho99iXO5_2VkXOMBVcx0X5Goh2yPRIRETI-KBj3VHkWNcsNBT36uKCyuIbjjHNuRNdP1RW5fflqIFU_oVZGv0RxfVco3ZeP3_g5",
            "dguId3QjTeaRtqwUV1gQTa:APA91bHhvPoO3-7AABBqV2D1KhbmVbUSNSh9_A8buheYqe6AyE3Q632lLD-zzYV-uU1pn79xn1x8AlUG-A9wrf2kjTsmS4d1oxuPKpC0fE6huC1uXj45Z7XKFGjVhyL1qtg5z0ziYQh5",
            "doOsYH50ShC_1iSnNKgiJN:APA91bHYyGGjjoRLORBfBK30m35UGaeoH0ZDo9dFVVRst7JL51Ia4gJG3SaRLNK39OTwdo6pV5JYnVrbRJn-BvdC-1xxHld_WMOuUUNuZtSrg3C8XwZv6wRPhxREHSuFgnXhJbBsO-IE",
            "doOsYH50ShC_1iSnNKgiJN:APA91bHYyGGjjoRLORBfBK30m35UGaeoH0ZDo9dFVVRst7JL51Ia4gJG3SaRLNK39OTwdo6pV5JYnVrbRJn-BvdC-1xxHld_WMOuUUNuZtSrg3C8XwZv6wRPhxREHSuFgnXhJbBsO-IE",
            "cn3l2OtwSb2tnVxHcGs3pM:APA91bGaOyBhuLpJ4ItTg9bDjdPWur7C-ReoRvGniXjVctGsqI-jFCJDDuECv_ITIh_jThee_I4_e8tPg7-4z86dJIaIkzAhimaUjpaXQSlCz0PD6YzBZIw6w8kvb_-brMFWb4ws5pRw"
        )
        firebaseCloudMessageService.sendMulticastMessage(tokenList, "26일 테스트", "손인턴의 테스트 푸시다 하 하 하 하 하 하 하 하 하 손인턴의 테스트 푸시다 하 하 하 하 하 하 하 하 하 손인턴의 테스트 푸시다 하 하 하 하 하 하 하 하 하")
    }
}