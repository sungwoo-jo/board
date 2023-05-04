package board.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import board.vo.UserVo;
import board.mapper.UserMapper;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;

@Repository
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void insertUser(UserVo userVo) throws Exception {
        messageDigest(userVo, userVo.getPassword());
        int result = userMapper.insertUser(userVo);
    }

    @Override
    public int idCheck(String username) throws Exception {
        int result = userMapper.idCheck(username);
        return result;
    }

    @Override
    public Integer checkUser(UserVo userVo) throws Exception {
        messageDigest(userVo, userVo.getPassword());
        return userMapper.checkUser(userVo);
    }

    @Override
    public UserVo getUserInfo(int count) {
        return userMapper.getUserInfo(count);
    }

    @Override
    public void updateUser(UserVo userVo) throws Exception {
        messageDigest(userVo, userVo.getPassword());
        userMapper.updateUser(userVo);
    }

    @Override
    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=69eddbebb2b07d6a316fc057c32fdbdf"); // REST API KEY
            sb.append("&redirect_url=http://localhost/user/kakao"); // redirect uri
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            // 결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메시지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            //Gson 라이브러리에 포함된 클래스로 JSON 파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            System.out.println("accessToken : " + accessToken);
            System.out.println("refreshToken : " + refreshToken);

            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public void messageDigest(UserVo userVo, String oldPassword) throws Exception { // SHA-512 해시함수
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(oldPassword.getBytes("UTF8"));
        userVo.setPassword(String.format("%0128x", new BigInteger(1, md.digest())));
    }
}
