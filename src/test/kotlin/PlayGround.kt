import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat

class PlayGround {

    @Test
    fun testTimeFormat() {
        val sample1 = "13:00"
        val sample2 = "33:00"
        val format = SimpleDateFormat("HH:mm")
        val date1 = format.parse(sample1)
        val date2 = format.parse(sample2)

        println(">>>>> " + format.parse(sample1))
        println(">>>>> " + format.parse(sample2))
        println(">>>>> " + format.format(date1))
        println(">>>>> " + format.format(date2))
        println(">>>>> " + (sample1 == format.format(date1)))
        println(">>>>> " + (sample2 == format.format(date2)))
    }

}