import static net.grinder.script.Grinder.grinder
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*
import net.grinder.script.GTest
import net.grinder.script.Grinder
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
// import static net.grinder.util.GrinderUtils.* // You can use this if you're using nGrinder after 3.2.3
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

import org.ngrinder.http.HTTPRequest
import org.ngrinder.http.HTTPRequestControl
import org.ngrinder.http.HTTPResponse
import org.ngrinder.http.cookie.Cookie
import org.apache.commons.lang.RandomStringUtils

import org.ngrinder.http.cookie.CookieManager
import java.util.Random
import java.time.LocalDateTime


@RunWith(GrinderRunner)
class TestRunner {

    public static GTest testRecord1
    public static GTest testRecord2
    public static GTest testRecord3
    public static GTest testRecord4
    public static GTest testRecord5
    public static GTest testRecord6
    public static GTest testRecord7
    public static GTest testRecord8
    public static GTest testRecord9
    public static GTest testRecord10
    public static GTest testRecord11
    public static GTest testRecord12
    public static GTest testRecord13

    public static HTTPRequest request = new HTTPRequest()
    public Map<String, String> headers = [:]
    public Map<String, Object> params = [:]
    public List<Cookie> cookies = []
    public Cookie cookie
    public Long temp

    public static String ip = System.getProperty('param')
    //public static String ip = "43.201.9.60"
    public static String host = "http://"+ip



    @BeforeProcess
    public static void beforeProcess() {
        HTTPRequestControl.setConnectionTimeout(300000)
        testRecord1 = new GTest(1, ip)
        testRecord2 = new GTest(2, ip)
        testRecord3 = new GTest(3, ip)
        testRecord4 = new GTest(4, ip)
        testRecord5 = new GTest(5, ip)
        testRecord6 = new GTest(6, ip)
        testRecord7 = new GTest(7, ip)
        testRecord8 = new GTest(8, ip)
        testRecord9 = new GTest(9, ip)
        testRecord10 = new GTest(10, ip)
        testRecord11 = new GTest(11, ip)
        testRecord12 = new GTest(12, ip)
        testRecord13 = new GTest(13, ip)
        grinder.logger.info("before process.")
    }

    @BeforeThread
    public void beforeThread() {
        testRecord1.record(this, "test01")
        testRecord2.record(this, "test02")
        testRecord3.record(this, "test03")
        testRecord4.record(this, "test04")
        testRecord5.record(this, "test05")
        testRecord6.record(this, "test06")
        testRecord7.record(this, "test07")
        testRecord8.record(this, "test08")
        testRecord9.record(this, "test09")
        testRecord10.record(this, "test10")
        testRecord11.record(this, "test11")
        testRecord12.record(this, "test12")
        testRecord13.record(this, "test13")

        grinder.statistics.delayReports = true
        grinder.logger.info("before thread.")
    }

    @Before
    public void before() {
        request = new HTTPRequest()
        request.setHeaders(headers)
        //Cookie(java.lang.String name, java.lang.String value, java.lang.String domain, java.lang.String path, java.util.Date expires, boolean secure)
        //http://grinder.sourceforge.net/g3/script-javadoc/HTTPClient/Cookie.html#constructor_summary
        //cookie = new Cookie("X-AUTH-TOKEN", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjYWZlX3BheWxvYWRfc3ViamVjdCIsInVzZXJJZCI6MzMsImlhdCI6MTY2MTQyNTM5NCwiZXhwIjoxNjc5NDI1Mzk0fQ.0i0BWhwk6AbKiAQ1_kNbZCzzxNWdwchf4uoKHKPnbTM")//,"http://127.0.0.1:8080","/", new Date(123456L),false)
        CookieManager.addCookies(cookies)
        grinder.logger.info("before. init headers and cookies")
    }

    // 1.로그인 - 2.개인 댓글 조회 - 3.질문게시판 {x}페이지 조회 - 4.질문글 1개 조회 - 5.추천 - 6.댓글남기기 - 7.방금 남긴 댓글 삭제 - 8.자유게시판 {x}페이지 조회 - 9.글 작성 - 10.자유게시판 1페이지 조회 - 11.개인 게시글 조회 - 12.글 수정 - 13. 랜덤 글 조회
    @Test
    public void test01() {
        grinder.logger.info("test1")
        String url1 = host+"/oauth/github-url";
        String url2 = host+"/login?code=this_is_fake_code";

        HTTPResponse response = request.GET(url1, params)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }

        HTTPResponse response2 = request.GET(url2, params)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test
    public void test02() {
        grinder.logger.info("test2")
        String url = host+"/comments/my";

        HTTPResponse response = request.GET(url, params)

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test
    public void test03() {
        grinder.logger.info("test3")
        String frontURL = host+"/posts?page="
        String backURL = "&size=10&categoryName=QNA&sort=postId,desc"
        String url = frontURL + RandomPostIdIssuer.getRandomNumbers() + backURL;

        HTTPResponse response = request.GET(url, params)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test
    public void test04() {
        grinder.logger.info("test4")
        String url = host+"/post/";
        String randomPostNumber = RandomPostIdIssuer.getRandomNumbers();

        HTTPResponse response = request.GET(url + randomPostNumber, params)

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test
    public void test05() {
        grinder.logger.info("test5")
        String url = host+"/post/recommend"
        String postId=RandomPostIdIssuer.getRandomNumbers();
        String userId=RandomPostIdIssuer.getRandomNumbers();
        String body = "{\"postId\":"+postId+",\n \"userId\":"+userId+"}"
        grinder.logger.info(body);
        HTTPResponse response = request.POST(url, body.getBytes())

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }

    }

    @Test
    public void test06() {
        grinder.logger.info("test6")
        String url = host+"/comment"
        String postId = RandomPostIdIssuer.getRandomNumbers();
        String commentId = RandomPostIdIssuer.getRandomNumbers();
        String charset = (('A'..'Z') + ('0'..'9')).join()
        Integer length = 500
        String commentContent = RandomStringUtils.random(length, charset.toCharArray())
        String body = "{\"referencePostId\":"+postId+",\n \"commentContent\":\""+commentContent+"\",\n \"createdAt\":\""+LocalDateTime.now()+"\"}"
        grinder.logger.info(body)
        HTTPResponse response = request.POST(url, body.getBytes())

        temp = response.getBodyText().toLong()



        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }

    }

    @Test //방금 남긴 댓글 삭제
    public void test07() {
        grinder.logger.info("test7")
        Long deleteCommentId = temp;
        String url = host+"/comment/"+deleteCommentId
        HTTPResponse response = request.DELETE(url,params)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test //자유게시판 조회
    public void test08() {
        grinder.logger.info("test8")
        String frontURL = host+"/posts?page="
        String backURL = "&size=10&categoryName=FREE&sort=postId,desc"
        String url = frontURL + RandomPostIdIssuer.getRandomNumbers() + backURL;

        HTTPResponse response = request.GET(url, params)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test //글작성
    public void test09() {
        grinder.logger.info("test9")
        String url = host+"/post"
        String userId=RandomPostIdIssuer.getRandomNumbers();
        String charset = (('A'..'Z') + ('0'..'9')).join()
        Integer length = 500
        String postSubject = RandomStringUtils.random(30, charset.toCharArray())
        String postContent = RandomStringUtils.random(700, charset.toCharArray())

        String body = "{\"postSubject\":\""+postSubject+"\",\n \"userId\":"+userId+",\n \"postContent\":\""+postContent+"\",\n \"createdAt\":\""+LocalDateTime.now()+"\",\"postCategoryId\":1}"
        grinder.logger.info(body)
        HTTPResponse response = request.POST(url, body.getBytes())
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
        temp = response.getBodyText().toLong()
        grinder.logger.info("temp!:" + temp)
    }

    @Test
    public void test10() {
        grinder.logger.info("test10")
        String frontURL = host+"/posts?page="
        String backURL = "&size=10&categoryName=FREE&sort=postId,desc"
        String url = frontURL + RandomPostIdIssuer.getRandomNumbers() + backURL;

        HTTPResponse response = request.GET(url, params)
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test
    public void test11() {
        grinder.logger.info("test11")
        String url = host+"/posts/my";

        HTTPResponse response = request.GET(url, params)

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test //글수정
    public void test12() {
        grinder.logger.info("test12")
        String url = host+"/post/" + temp
        String charset = (('A'..'Z') + ('0'..'9')).join()

        String postSubject = RandomStringUtils.random(30, charset.toCharArray())
        String postContent = RandomStringUtils.random(700, charset.toCharArray())

        String body = "{\"postSubject\":\""+postSubject+"\",\n\"postContent\":\""+postContent+"\",\n \"updatedAt\":\""+LocalDateTime.now()+"\"}"
        HTTPResponse response = request.PATCH(url, body.getBytes())
        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }

    @Test
    public void test13() {
        grinder.logger.info("test13")
        String url = host+"/post/";
        String randomPostNumber = RandomPostIdIssuer.getRandomNumbers();

        HTTPResponse response = request.GET(url + randomPostNumber, params)

        if (response.statusCode == 301 || response.statusCode == 302) {
            grinder.logger.warn("Warning. The response may not be correct. The response code was {}.", response.statusCode)
        } else {
            assertThat(response.statusCode, is(200))
        }
    }



    class RandomPostIdIssuer {
        private static int NUMBER_LENGTH=4;
        private static int NUMBER_BOUND=10;

        public static String getRandomNumbers(){
            Random random = new Random();
            StringBuilder sb= new StringBuilder();
            for(int i=0; i<NUMBER_LENGTH; i++){
                sb.append(String.valueOf(random.nextInt(NUMBER_BOUND)));
            }
            String temp = sb.toString()
            if(temp.charAt(0)=="0") {
                return RandomPostIdIssuer.getRandomNumbers();
            }
            return temp;
        }
    }
}
