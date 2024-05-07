import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ZipCodeService_0 {

    public static String findZipCode(String address) {
        StringBuilder result = new StringBuilder();
        try {
            // 주소를 UTF-8로 인코딩
            String encodedAddress = URLEncoder.encode(address, "UTF-8");

            // 요청할 API URL
            String apiUrl = "http://www.juso.go.kr/addrlink/addrLinkApi.do?" +
                    "currentPage=1&countPerPage=1&resultType=json&" +
                    "keyword=" + encodedAddress + "&confmKey=CONFIRM_KEY";

            // API에 연결
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 응답 데이터 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();

            // 연결 해제
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
