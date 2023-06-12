import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.slf4j.LoggerFactory

open class BaseTest  {
    private val logger = LoggerFactory.getLogger(BaseTest::class.java)

    @BeforeEach
    fun setup(testInfo: TestInfo) {
        logger.info("\nExecuting Test : ${testInfo.displayName} **********\n")
    }

    @AfterEach
    fun tearDown(testInfo: TestInfo) {
        logger.info("Execution of Test ${testInfo.displayName} completed!\n")
    }
}
